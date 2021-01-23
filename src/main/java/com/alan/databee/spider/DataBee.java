package com.alan.databee.spider;

import com.alan.databee.builder.DebugResultBuilder;
import com.alan.databee.common.TaskStatus;
import com.alan.databee.common.util.log.LoggerUtil;
import com.alan.databee.model.DebugResult;
import com.alan.databee.model.ResultEnum;
import com.alan.databee.service.Task;
import com.alan.databee.spider.downloader.Downloader;
import com.alan.databee.spider.downloader.HttpClientDownloader;
import com.alan.databee.spider.exception.SpiderErrorEnum;
import com.alan.databee.spider.exception.SpiderTaskException;
import com.alan.databee.spider.model.Request;
import com.alan.databee.spider.page.Page;
import com.alan.databee.spider.pipeline.ConsolePipeline;
import com.alan.databee.spider.pipeline.Pipeline;
import com.alan.databee.spider.processor.PageProcessor;
import com.alan.databee.spider.scheduler.Scheduler;
import com.alan.databee.spider.thread.CountableThreadPool;
import org.apache.commons.collections.CollectionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Deque;
import java.util.List;
import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;


public class DataBee {

    private static final Logger LOGGER = LoggerFactory.getLogger("dataBee");


    /**
     * 标志任务之间是否开启同步。默认是关闭的
     */
    private volatile boolean sync = false;

    /**
     * 总的线程池
     */
    protected CountableThreadPool threadPool;

    protected ExecutorService executorService;

    /**
     * 线程池的线程数
     */
    protected int threadNum = 1;

    /**
     * dataBee的状态
     */
    protected AtomicInteger stat = new AtomicInteger(STAT_INIT);

    protected final static int STAT_INIT = 0;

    protected final static int STAT_RUNNING = 1;

    protected final static int STAT_STOPPED = 2;

    private ReentrantLock statLock = new ReentrantLock();

    private Condition statCondition = statLock.newCondition();

    /**
     * 此处为异步执行种子任务添加监听器
     */
    private List<SpiderListener> spiderListeners;

    /**
     * 种子的阻塞队列。
     */
    private BlockingQueue<FutureTask> taskBlockingQueue = null;




    public DataBee() {

    }

    public DataBee setSync(boolean sync) {
        this.sync = sync;
        // 如果异步运行
        if (!sync) {
            if (threadPool == null || threadPool.isShutdown()) {
                if (executorService != null && !executorService.isShutdown()) {
                    threadPool = new CountableThreadPool(threadNum, executorService);
                } else {
                    threadPool = new CountableThreadPool(threadNum);
                }
            }
        }
        return this;
    }

    public int run(Task task) {

        return runSync(task);
    }

    private void onDownloadSuccess(Page page, Deque<Site.Component> pageProcessors, Deque<Site.Component> pipelines, Site site) {

        if (site.getAcceptStatCode().contains(page.getStatusCode())) {

            // 1、处理下载好的元数据，分离出有用的数据
            for (Site.Component component : pageProcessors) {
                PageProcessor processor = (PageProcessor) component.getCom();
                processor.process(page, site);
            }

            // 2、是否需要添加额外的链接
            extractAndAddRequests(page, true, site);

            // 3、持久化所分离出来的有效数据
            if (!page.getResultItems().isSkip()) {
                for (Site.Component component : pipelines) {
                    Pipeline pipeline = (Pipeline) component.getCom();
                    // task参数实际上不需要
                    pipeline.process(page.getResultItems(), null);
                }
            }
            site.successPageNumInc();
        } else {
            // 错误日志格式： 任务名字，是否成功，页面链接，失败原因， 详细信息
            LoggerUtil.error(LOGGER, site.getTaskName(), TaskStatus.FAILED, page.getUrl(),
                    SpiderErrorEnum.Unexpect_Status_Code,
                    page.getStatusCode());
        }
    }

    private void extractAndAddRequests(Page page, boolean spawnUrl, Site site) {
        if (spawnUrl && CollectionUtils.isNotEmpty(page.getTargetRequests())) {
            for (Request request : page.getTargetRequests()) {
                site.pageCountAdd(1);
                addRequest(request, site);
            }
        }
        sleep(site.getSleepTime());
    }

    private void onDownloadFail(Page page, Site site) {
        Request request = page.getRequest();
        if (site.getRetryTimes() > 0) {
            if(request.getRetryTimes()>site.getRetryTimes()){
                request.setRetryTimes(site.getRetryTimes());
            }
            doCycleRetry(request, site);
        } else {
            // 错误日志格式： 任务名称， 任务状态， 页面链接， 状态描述， 详细信息
            LoggerUtil.error(LOGGER, site.getTaskName(), TaskStatus.FAILED, request.getUrl(),
                    SpiderErrorEnum.Download_Error, page.getStatusCode());
        }
    }

    private void doCycleRetry(Request request, Site site) {
        int retryTimes = request.getRetryTimes();
        if (retryTimes >= 0) {
            Scheduler scheduler = site.getScheduler();
            request.setRetryTimes(retryTimes - 1);
            scheduler.push(request, null);
        }
        sleep(site.getSleepTime());
    }

    protected void sleep(int time) {
        try {
            Thread.sleep(time);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    /**
     * 同步运行，即对于由一个种子派生出来的所有url,都采用单线程处理，
     */
    private int runSync(Task task) throws SpiderTaskException {

        // 检查是否运行于同步模式
        if (!sync) {
            return 0;
        }

        Site site = task.getSite();

        // 同步运行，同步DataBee状态
        statSync();

        if (!componentInit(site)) {
            // 错误日志格式： 任务名， 状态， 页面链接， 状态描述， 详细信息
            LoggerUtil.error(LOGGER, task.taskName, TaskStatus.FAILED, site.getSeed(),
                    SpiderErrorEnum.Component_Incomplete, "pageProcessor缺失");
            return 0;
        }

        Scheduler scheduler = site.getScheduler();
        Downloader downloader = site.getDownloader();
        Deque<Site.Component> pageProcessors = site.getPageProcessors();
        Deque<Site.Component> pipelines = site.getPipelines();

        int res = 1;
        if (stat.get() == STAT_RUNNING) {
            long start = System.currentTimeMillis();
            Request request = scheduler.poll(null);
            while (request != null) {
                Page page = downloader.download(request, site);
                if (page.isDownloadSuccess()) {
                    onDownloadSuccess(page, pageProcessors, pipelines, site);
                } else {
                    onDownloadFail(page, site);
                    res = 0;
                }
                request = scheduler.poll(null);
            }

            long finish = System.currentTimeMillis();
            // 统计日志格式：状态，任务名称，种子，成功的页面数量，总页面数量，耗时
            LoggerUtil.info(LOGGER, TaskStatus.FINISH, task.taskName, site.getSeed(), site.getSuccessPageNum(),
                    site.getPageCount(), finish - start);
        } else {
            // 错误日志格式： 任务名称， 状态， 页面， 状态描述， 详细信息
            LoggerUtil.error(LOGGER, task.taskName, TaskStatus.FAILED, site.getSeed(),
                    SpiderErrorEnum.DataBee_Status_Error, "DataBee不在运行态");
            res = 0;
        }
        taskFinish();
        return res;
    }

    /**
     * 异步运行
     */
    private Future<Task> runAsync(Task task) {
        // 此处需要额外实现
        Callable<Task> callable = new RunnerTask(task);
        FutureTask<Task> futureTask = new FutureTask<Task>(callable);
        threadPool.execute(futureTask);
        return futureTask;
    }

    private void statSync() {
        try {
            statLock.lock();
            while (!stat.compareAndSet(STAT_INIT, STAT_RUNNING)) {
                statCondition.await();
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        } finally {
            statLock.unlock();
        }
    }

    private void taskFinish() {
        try {
            statLock.lock();
            if (!stat.compareAndSet(STAT_RUNNING, STAT_INIT)) {
                // 任务名， 状态， 页面， 状态描述， 详细信息
                LoggerUtil.error(LOGGER, "mate-taskFinish", TaskStatus.FAILED, "Function: taskFinish()",
                        SpiderErrorEnum.DataBee_Status_Error, "将DataBee从运行态设置为就绪态时发生错误");
                close();
            }
            statCondition.signalAll();
        } finally {
            statLock.unlock();
        }
    }

    private boolean componentInit(Site site) {
        if (site.getPageProcessors() == null) {
            return false;
        }
        if (site.getDownloader() == null) {
            site.setDownloader(new HttpClientDownloader());
        }
        if (site.isPipelineEmpty()) {
            site.pipelineAddLast("consolePipeline", new ConsolePipeline());
        }
        if (threadPool == null || threadPool.isShutdown()) {
            if (executorService != null && !executorService.isShutdown()) {
                threadPool = new CountableThreadPool(threadNum, executorService);
            } else {
                threadPool = new CountableThreadPool(threadNum);
            }
        }
        return true;
    }

    protected void addRequest(Request request, Site site) {
        Scheduler scheduler = site.getScheduler();
        scheduler.push(request, null);
    }

    private void close() {

    }

    private class RunnerTask implements Callable {
        Task task;

        public RunnerTask(Task task) {
            this.task = task;
        }

        @Override
        public DebugResult call() throws Exception {
            DebugResult result = null;
            if (task == null) {
                throw new NullPointerException("task is null");
            }

            Site site = task.getSite();
            if (!componentInit(site)) {
                // 错误日志格式： 任务名， 状态， 页面链接， 状态描述， 详细信息
                LoggerUtil.error(LOGGER, task.taskName, TaskStatus.FAILED, site.getSeed(),
                        SpiderErrorEnum.Component_Incomplete, "pageProcessor缺失");
                return result;
            }
            Scheduler scheduler = site.getScheduler();
            Downloader downloader = site.getDownloader();
            Deque<Site.Component> pageProcessors = site.getPageProcessors();
            Deque<Site.Component> pipelines = site.getPipelines();
            long start = System.currentTimeMillis();
            Request request = scheduler.poll(null);
            while (request != null) {
                Page page = downloader.download(request, site);
                if (page.isDownloadSuccess()) {
                    onDownloadSuccess(page, pageProcessors, pipelines, site);
                } else {
                    onDownloadFail(page, site);
                    result = DebugResultBuilder.buildError(ResultEnum.Downloader_Error);
                    result.setDownloadStat(ResultEnum.Downloader_Error);
                }
                request = scheduler.poll(null);
            }

            long finish = System.currentTimeMillis();
            // 统计日志格式：状态，任务名称，种子，成功的页面数量，总页面数量，耗时
            LoggerUtil.info(LOGGER, TaskStatus.FINISH, task.taskName, site.getSeed(), site.getSuccessPageNum(),
                    site.getPageCount(), finish - start);
            if (result == null) {
                result = DebugResultBuilder.buildSuccess();
            }
            return result;
        }
    }

}

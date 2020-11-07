package com.alan.databee.spider;

import com.alan.databee.spider.downloader.Downloader;
import com.alan.databee.spider.downloader.HttpClientDownloader;
import com.alan.databee.spider.model.Request;
import com.alan.databee.spider.page.Page;
import com.alan.databee.spider.pipeline.ConsolePipeline;
import com.alan.databee.spider.pipeline.Pipeline;
import com.alan.databee.spider.processor.PageProcessor;
import com.alan.databee.spider.scheduler.Scheduler;
import com.alan.databee.spider.thread.CountableThreadPool;
import org.apache.commons.collections.CollectionUtils;
import com.alan.databee.spider.utils.UrlUtils;


import java.util.Deque;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;


public class TaskRunner {

    private Site site;

    private boolean sync;

    protected CountableThreadPool threadPool;

    protected ExecutorService executorService;

    protected int threadNum = 1;

    protected AtomicInteger stat = new AtomicInteger(STAT_INIT);

    protected final static int STAT_INIT = 0;

    protected final static int STAT_RUNNING = 1;

    protected final static int STAT_STOPPED = 2;

    protected boolean spawnUrl = true;

    private ReentrantLock newUrlLock = new ReentrantLock();

    private Condition newUrlCondition = newUrlLock.newCondition();

    private List<SpiderListener> spiderListeners;

    private final AtomicLong pageCount = new AtomicLong(0);


    public static TaskRunner create(Site site) {
        return new TaskRunner(site);
    }

    public TaskRunner(Site site) {
        this.site = site;
    }

    public TaskRunner setUrl(String url) {
        Request request = new Request(url);
        Scheduler scheduler = this.site.getScheduler();
        scheduler.push(request, null);
        return this;
    }

    public TaskRunner setSync(boolean sync) {
        this.sync = sync;
        return this;
    }

    public void run() {
        if (sync) {
            runSync();
        } else {
            runAsync();
        }
    }

    private void onDownloadSuccess(Page page, Deque<Site.Component> pageProcessors, Deque<Site.Component> pipelines) {

        if (site.getAcceptStatCode().contains(page.getStatusCode())) {

            // 1、处理下载好的元数据，分离出有用的数据
            for (Site.Component component : pageProcessors) {
                PageProcessor processor = (PageProcessor) component.getCom();
                processor.process(page, site);
            }

            // 2、是否需要添加额外的链接
            extractAndAddRequests(page, spawnUrl);

            // 3、持久化所分离出来的有效数据
            if (!page.getResultItems().isSkip()) {
                for (Site.Component component : pipelines) {
                    Pipeline pipeline = (Pipeline) component.getCom();
                    // task参数实际上不需要
                    pipeline.process(page.getResultItems(), null);
                }
            }
        } else {
            // 此处需要打日志
        }
    }

    private void extractAndAddRequests(Page page, boolean spawnUrl) {
        if (spawnUrl && CollectionUtils.isNotEmpty(page.getTargetRequests())) {
            for (Request request : page.getTargetRequests()) {
                pageCount.incrementAndGet();
                addRequest(request);
            }
        }
        sleep(site.getSleepTime());
    }

    private void onDownloadFail(Page page) {
        if (site.getRetryTimes() != 0) {
            Request request = page.getRequest();
            doCycleRetry(request);
        } else {
            // 打日志
        }
    }

    private void doCycleRetry(Request request) {
        int retryTimes = request.getRetryTimes();
        if (retryTimes > site.getRetryTimes()) {
            retryTimes = site.getRetryTimes();
        }
        if (retryTimes < 0) {
            // 打日志
        } else {
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
            // 打日志
        }
    }

    /**
     * 同步运行
     */
    private void runSync() {
        checkRunningStat();
        componentInit();
        Scheduler scheduler = site.getScheduler();
        Downloader downloader = site.getDownloader();
        Deque<Site.Component> pageProcessors = site.getPageProcessors();
        Deque<Site.Component> pipelines = site.getPipelines();
        if (stat.get() == STAT_RUNNING) {
            Request request = scheduler.poll(null);
            while (request != null) {
                Page page = downloader.download(request, site);
                if (page.isDownloadSuccess()) {
                    onDownloadSuccess(page, pageProcessors, pipelines);
                } else {
                    onDownloadFail(page);
                }
                request = scheduler.poll(null);
            }
        }

    }

    /**
     * 异步运行
     */
    private void runAsync() {

    }

    private void checkRunningStat() {
        while (true) {
            int statNow = stat.get();
            if (statNow == STAT_RUNNING) {
                throw new IllegalStateException("Spider is already running!");
            }
            if (stat.compareAndSet(statNow, STAT_RUNNING)) {
                break;
            }
        }
    }

    private void componentInit() {
        if (this.site.getDownloader() == null) {
            this.site.setDownloader(new HttpClientDownloader());
        }
        if (this.site.isPipelineEmpty()) {
            this.site.pipelineAddLast("consolePipeline", new ConsolePipeline());
        }
        if (threadPool == null || threadPool.isShutdown()) {
            if (executorService != null && !executorService.isShutdown()) {
                threadPool = new CountableThreadPool(threadNum, executorService);
            } else {
                threadPool = new CountableThreadPool(threadNum);
            }
        }
    }

    protected void addRequest(Request request) {
        if (site.getDomain() == null && request != null && request.getUrl() != null) {
            site.setDomain(UrlUtils.getDomain(request.getUrl()));
        }
        Scheduler scheduler = site.getScheduler();
        scheduler.push(request, null);
    }

    private void close() {

    }


}

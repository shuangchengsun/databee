package com.alan.databee.spider;


import com.alan.databee.spider.downloader.Downloader;
import com.alan.databee.spider.model.Request;
import com.alan.databee.spider.pipeline.Pipeline;
import com.alan.databee.spider.processor.PageProcessor;
import com.alan.databee.spider.scheduler.Scheduler;
import com.alan.databee.spider.utils.HttpConstant;

import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * 每个site和一个task一一对应，标志着task的运行上下文以及运行环境。
 */
public class Site {

    /**
     * 任务名称，一个site和一个task一一对应
     */
    private String taskName;

    /**
     * 本task的浏览器Agent
     */
    private String userAgent;

    /**
     * task初始默认的cookies
     */
    private Map<String, String> defaultCookies = new LinkedHashMap<String, String>();

    /**
     * 网络交互的cookies
     */
    private Map<String, Map<String, String>> cookies = new HashMap<String, Map<String, String>>();

    /**
     * 本次网络交互的字符集
     */
    private String charset;

    /**
     * 每次网络交互中间的间隔时间
     */
    private int sleepTime = 2000;

    /**
     * 网络请求失败重试次数
     */
    private int retryTimes = 3;

    /**
     * 任务的执行周期
     */
    private int taskCircle = 1;

    /**
     * 重试睡眠的时间
     */
    private int retrySleepTime = 1000;

    /**
     * 网络请求超时时间
     */
    private int timeOut = 10000;

    /**
     * 状态码集合
     */
    private static final Set<Integer> DEFAULT_STATUS_CODE_SET = new HashSet<Integer>();

    /**
     * 网络请求所允许的状态码集合
     */
    private Set<Integer> acceptStatCode = DEFAULT_STATUS_CODE_SET;

    /**
     * 请求头
     */
    private Map<String, String> headers = new HashMap<String, String>();

    /**
     * 是否使用GZip压缩网络数据
     */
    private boolean useGzip = true;

    /**
     * 禁止cookie
     */
    private boolean disableCookieManagement = false;

    /**
     * 下载器
     */
    private Downloader downloader;

    /**
     * 页面的处理器
     */
    private Deque<Component> pageProcessors = new LinkedList<>();

    /**
     * 持久化器
     */
    private Deque<Component> pipelines = new LinkedList<>();

    /**
     * 存放processor和pipeline的名字
     */
    private final Set<String> nameSet = new HashSet<>();

    /**
     * 调度器
     */
    private Scheduler scheduler;

    /**
     * 描述一个任务中一共爬取了多少页面
     */
    private AtomicInteger pageCount = new AtomicInteger(1);

    private AtomicInteger successPageNum = new AtomicInteger(0);

    /**
     * 根种子，一个seed唯一对应于一个task
     */
    private String seed;

    private Request seedRequest;

    /**
     * 主域
     */
    private String Domain;


    static {
        DEFAULT_STATUS_CODE_SET.add(HttpConstant.StatusCode.CODE_200);
    }


    public static Site me() {
        return new Site();
    }


    public Site addCookie(String name, String value) {
        defaultCookies.put(name, value);
        return this;
    }


    public Site addCookie(String domain, String name, String value) {
        if (!cookies.containsKey(domain)) {
            cookies.put(domain, new HashMap<String, String>());
        }
        cookies.get(domain).put(name, value);
        return this;
    }


    public Site setUserAgent(String userAgent) {
        this.userAgent = userAgent;
        return this;
    }


    public Map<String, String> getCookies() {
        return defaultCookies;
    }


    public Map<String, Map<String, String>> getAllCookies() {
        return cookies;
    }


    public String getUserAgent() {
        return userAgent;
    }


    public Site setCharset(String charset) {
        this.charset = charset;
        return this;
    }


    public String getCharset() {
        return charset;
    }

    public int getTimeOut() {
        return timeOut;
    }


    public Site setTimeOut(int timeOut) {
        this.timeOut = timeOut;
        return this;
    }


    public Site setAcceptStatCode(Set<Integer> acceptStatCode) {
        this.acceptStatCode = acceptStatCode;
        return this;
    }


    public Set<Integer> getAcceptStatCode() {
        return acceptStatCode;
    }


    public Site setSleepTime(int sleepTime) {
        this.sleepTime = sleepTime;
        return this;
    }


    public int getSleepTime() {
        return sleepTime;
    }


    public int getRetryTimes() {
        return retryTimes;
    }

    public Map<String, String> getHeaders() {
        return headers;
    }


    public Site addHeader(String key, String value) {
        headers.put(key, value);
        return this;
    }


    public Site setRetryTimes(int retryTimes) {
        this.retryTimes = retryTimes;
        return this;
    }

    public int getTaskCircle() {
        return taskCircle;
    }

    public Site setTaskCircle(int taskCircle) {
        this.taskCircle = taskCircle;
        return this;
    }

    public boolean isUseGzip() {
        return useGzip;
    }

    public int getRetrySleepTime() {
        return retrySleepTime;
    }


    public Site setRetrySleepTime(int retrySleepTime) {
        this.retrySleepTime = retrySleepTime;
        return this;
    }


    public Site setUseGzip(boolean useGzip) {
        this.useGzip = useGzip;
        return this;
    }

    public boolean isDisableCookieManagement() {
        return disableCookieManagement;
    }


    public Site setDisableCookieManagement(boolean disableCookieManagement) {
        this.disableCookieManagement = disableCookieManagement;
        return this;
    }


    public Site processorAddLast(PageProcessor processor) {
        Component component = new Component(processor);
        this.pageProcessors.addLast(component);
        return this;
    }

    public Site processorAddLast(String name, PageProcessor processor) {
        if (nameSet.contains(name)) {
            throw new IllegalArgumentException("已经存在了名称为" + name + "的组件");
        }
        nameSet.add(name);
        Component component = new Component(name, processor);
        this.pageProcessors.addLast(component);
        return this;
    }

    public Site processorAddFirst(PageProcessor processor) {
        Component component = new Component(processor);
        this.pageProcessors.addFirst(component);
        return this;
    }

    public Site processorAddFirst(String name, PageProcessor processor) {
        if (nameSet.contains(name)) {
            throw new IllegalArgumentException("已经存在了名称为" + name + "的组件");
        }
        nameSet.add(name);
        Component component = new Component(name, processor);
        this.pageProcessors.addFirst(component);
        return this;
    }

    public Site removeProcessor(String name) {
        if (!nameSet.contains(name)) {
            throw new IllegalArgumentException("未找到名称为" + name + "的Pageprocessor");
        }
        for (Component component : this.pageProcessors) {
            if (name.equals(component.name)) {
                this.pageProcessors.remove(component);
                return this;
            }
        }
        return this;
    }

    public Site removeProcessor(PageProcessor processor) {
        for (Component component : this.pageProcessors) {
            PageProcessor com = (PageProcessor) component.getCom();
            if (com == processor) {
                this.pageProcessors.remove(component);
            }
        }
        return this;
    }

    public boolean isProcessorEmpty() {
        return this.pageProcessors.isEmpty();
    }

    /**
     * 此方法并不会删除对应的命名空间中的名字，导致此部分会一直得不到释放
     * @return
     */
    @Deprecated
    public Site processorClear() {
        this.pageProcessors = new LinkedList<>();
        return this;
    }

    public Site pipelineAddLast(Pipeline pipeline) {
        Component component = new Component(pipeline);
        this.pipelines.addLast(component);
        return this;
    }

    public Site pipelineAddLast(String name, Pipeline pipeline) {
        if (nameSet.contains(name)) {
            throw new IllegalArgumentException("已经存在了名称为" + name + "的组件");
        }
        nameSet.add(name);
        Component component = new Component(name, pipeline);
        this.pipelines.addLast(component);
        return this;
    }

    public Site pipelineAddFirst(Pipeline pipeline) {
        Component component = new Component(pipeline);
        this.pipelines.addFirst(component);
        return this;
    }

    public Site pipelineAddFirst(String name, Pipeline pipeline) {
        if (nameSet.contains(name)) {
            throw new IllegalArgumentException("已经存在了名称为" + name + "的组件");
        }
        nameSet.add(name);
        Component component = new Component(name, pipeline);
        this.pipelines.addFirst(component);
        return this;
    }

    public Site removePipeline(String name) {
        if (!nameSet.contains(name)) {
            throw new IllegalArgumentException("未找到名称为" + name + "的Pageprocessor");
        }
        for (Component component : this.pipelines) {
            if (name.equals(component.name)) {
                this.pageProcessors.remove(component);
                return this;
            }
        }
        return this;
    }

    public boolean isPipelineEmpty() {
        return this.pipelines.isEmpty();
    }

    @Deprecated
    public Site pipelineClear() {
        this.pipelines = new LinkedList<>();
        return this;
    }

    public Downloader getDownloader() {
        return downloader;
    }

    public Site setDownloader(Downloader downloader) {
        this.downloader = downloader;
        return this;
    }

    /**
     * 一定要保证seed或者seedRequest在设置调度器之前
     * @param scheduler
     * @return
     */
    public Site setScheduler(Scheduler scheduler) {
        this.scheduler = scheduler;
        if(seed == null || seedRequest == null){
            throw new IllegalArgumentException("没有初始化seed或seedRequest");
        }else{
            this.scheduler.push(seedRequest,null);
        }
        return this;
    }

    public Scheduler getScheduler() {
        return scheduler;
    }

    public Deque<Component> getPageProcessors() {
        return pageProcessors;
    }

    public Deque<Component> getPipelines() {
        return pipelines;
    }

    public int pageCountAdd(int num){
        return this.pageCount.addAndGet(num);
    }

    public int pageCountInc(){
        return this.pageCount.incrementAndGet();
    }


    public int getPageCount() {
        return pageCount.get();
    }

    public String getTaskName() {
        return taskName;
    }

    public Site setTaskName(String taskName) {
        this.taskName = taskName;
        return this;
    }

    public String getSeed() {
        return seed;
    }

    public Site setSeed(String seed) {
        this.seed = seed;
        return this;
    }

    @Override
    public String toString() {
        return "Site{" +
                "taskName='" + taskName + '\'' +
                ", userAgent='" + userAgent + '\'' +
                ", charset='" + charset + '\'' +
                ", sleepTime=" + sleepTime +
                ", retryTimes=" + retryTimes +
                ", retrySleepTime=" + retrySleepTime +
                ", timeOut=" + timeOut +
                ", acceptStatCode=" + acceptStatCode +
                ", headers=" + headers +
                ", useGzip=" + useGzip +
                ", pageCount=" + pageCount +
                ", seed='" + seed + '\'' +
                '}';
    }

    protected static class Component {
        private String name;
        private Object com;

        public Component(String name, Object com) {
            this.name = name;
            this.com = com;
        }

        public Component(Object com) {
            this.com = com;
            this.name = "";
        }

        protected Object getCom() {
            return com;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            Component component = (Component) o;
            return Objects.equals(name, component.name);
        }

        @Override
        public int hashCode() {
            return Objects.hash(name);
        }
    }

    public Request getSeedRequest() {
        return seedRequest;
    }

    public Site setSeedRequest(Request seedRequest) {
        if(this.scheduler == null) {
            this.seedRequest = seedRequest;
        }else {
            this.scheduler.push(seedRequest,null);
        }
        return this;
    }

    public String getDomain() {
        return Domain;
    }

    public void setDomain(String domain) {
        Domain = domain;
    }

    public int successPageNumInc(){
        return this.successPageNum.incrementAndGet();
    }

    public int successPageAdd(int num){
        return this.successPageNum.addAndGet(num);
    }

    public int getSuccessPageNum(){
        return this.successPageNum.get();
    }
}

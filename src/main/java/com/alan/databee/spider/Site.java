package com.alan.databee.spider;


import com.alan.databee.spider.downloader.Downloader;
import com.alan.databee.spider.model.Request;
import com.alan.databee.spider.pipeline.Pipeline;
import com.alan.databee.spider.processor.PageProcessor;
import com.alan.databee.spider.scheduler.Scheduler;
import com.alan.databee.spider.utils.HttpConstant;

import java.util.*;

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
    private int retryTimes = 0;

    /**
     * 网络请求失败循环重试次数
     */
    private int cycleRetryTimes = 0;

    /**
     * 重试睡眠的时间
     */
    private int retrySleepTime = 1000;

    /**
     * 网络请求超时时间
     */
    private int timeOut = 5000;

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
     * 命名空间
     */
    private final Set<String> nameSet = new HashSet<>();

    /**
     * 调度器
     */
    private Scheduler scheduler;

    /**
     * 描述一个种子最终所有下载处理的页面
     */
    private int pageCount;

    /**
     * 根种子，一个seed唯一对应于一个task
     */
    private String seed;


    static {
        DEFAULT_STATUS_CODE_SET.add(HttpConstant.StatusCode.CODE_200);
    }

    /**
     * new a Site
     *
     * @return new site
     */
    public static Site me() {
        return new Site();
    }

    /**
     * Add a cookie with domain
     *
     * @param name  name
     * @param value value
     * @return this
     */
    public Site addCookie(String name, String value) {
        defaultCookies.put(name, value);
        return this;
    }

    /**
     * Add a cookie with specific domain.
     *
     * @param domain domain
     * @param name   name
     * @param value  value
     * @return this
     */
    public Site addCookie(String domain, String name, String value) {
        if (!cookies.containsKey(domain)) {
            cookies.put(domain, new HashMap<String, String>());
        }
        cookies.get(domain).put(name, value);
        return this;
    }

    /**
     * set user agent
     *
     * @param userAgent userAgent
     * @return this
     */
    public Site setUserAgent(String userAgent) {
        this.userAgent = userAgent;
        return this;
    }

    /**
     * get cookies
     *
     * @return get cookies
     */
    public Map<String, String> getCookies() {
        return defaultCookies;
    }

    /**
     * get cookies of all domains
     *
     * @return get cookies
     */
    public Map<String, Map<String, String>> getAllCookies() {
        return cookies;
    }

    /**
     * get user agent
     *
     * @return user agent
     */
    public String getUserAgent() {
        return userAgent;
    }

    /**
     * Set charset of page manually.<br>
     * When charset is not set or set to null, it can be auto detected by Http header.
     *
     * @param charset charset
     * @return this
     */
    public Site setCharset(String charset) {
        this.charset = charset;
        return this;
    }

    /**
     * get charset set manually
     *
     * @return charset
     */
    public String getCharset() {
        return charset;
    }

    public int getTimeOut() {
        return timeOut;
    }

    /**
     * set timeout for downloader in ms
     *
     * @param timeOut timeOut
     * @return this
     */
    public Site setTimeOut(int timeOut) {
        this.timeOut = timeOut;
        return this;
    }

    /**
     * Set acceptStatCode.<br>
     * When status code of http response is in acceptStatCodes, it will be processed.<br>
     * {200} by default.<br>
     * It is not necessarily to be set.<br>
     *
     * @param acceptStatCode acceptStatCode
     * @return this
     */
    public Site setAcceptStatCode(Set<Integer> acceptStatCode) {
        this.acceptStatCode = acceptStatCode;
        return this;
    }

    /**
     * get acceptStatCode
     *
     * @return acceptStatCode
     */
    public Set<Integer> getAcceptStatCode() {
        return acceptStatCode;
    }

    /**
     * Set the interval between the processing of two pages.<br>
     * Time unit is milliseconds.<br>
     *
     * @param sleepTime sleepTime
     * @return this
     */
    public Site setSleepTime(int sleepTime) {
        this.sleepTime = sleepTime;
        return this;
    }

    /**
     * Get the interval between the processing of two pages.<br>
     * Time unit is milliseconds.<br>
     *
     * @return the interval between the processing of two pages,
     */
    public int getSleepTime() {
        return sleepTime;
    }

    /**
     * Get retry times immediately when download fail, 0 by default.<br>
     *
     * @return retry times when download fail
     */
    public int getRetryTimes() {
        return retryTimes;
    }

    public Map<String, String> getHeaders() {
        return headers;
    }

    /**
     * Put an Http header for downloader. <br>
     * Use {@link #addCookie(String, String)} for cookie and {@link #setUserAgent(String)} for user-agent. <br>
     *
     * @param key   key of http header, there are some keys constant in {@link HttpConstant.Header}
     * @param value value of header
     * @return this
     */
    public Site addHeader(String key, String value) {
        headers.put(key, value);
        return this;
    }

    /**
     * Set retry times when download fail, 0 by default.<br>
     *
     * @param retryTimes retryTimes
     * @return this
     */
    public Site setRetryTimes(int retryTimes) {
        this.retryTimes = retryTimes;
        return this;
    }

    /**
     * When cycleRetryTimes is more than 0, it will add back to scheduler and try download again. <br>
     *
     * @return retry times when download fail
     */
    public int getCycleRetryTimes() {
        return cycleRetryTimes;
    }

    /**
     * Set cycleRetryTimes times when download fail, 0 by default. <br>
     *
     * @param cycleRetryTimes cycleRetryTimes
     * @return this
     */
    public Site setCycleRetryTimes(int cycleRetryTimes) {
        this.cycleRetryTimes = cycleRetryTimes;
        return this;
    }

    public boolean isUseGzip() {
        return useGzip;
    }

    public int getRetrySleepTime() {
        return retrySleepTime;
    }

    /**
     * Set retry sleep times when download fail, 1000 by default. <br>
     *
     * @param retrySleepTime retrySleepTime
     * @return this
     */
    public Site setRetrySleepTime(int retrySleepTime) {
        this.retrySleepTime = retrySleepTime;
        return this;
    }

    /**
     * Whether use gzip. <br>
     * Default is true, you can set it to false to disable gzip.
     *
     * @param useGzip useGzip
     * @return this
     */
    public Site setUseGzip(boolean useGzip) {
        this.useGzip = useGzip;
        return this;
    }

    public boolean isDisableCookieManagement() {
        return disableCookieManagement;
    }

    /**
     * Downloader is supposed to store response cookie.
     * Disable it to ignore all cookie fields and stay clean.
     * Warning: Set cookie will still NOT work if disableCookieManagement is true.
     *
     * @param disableCookieManagement disableCookieManagement
     * @return this
     */
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

    public Site setScheduler(Scheduler scheduler) {
        this.scheduler = scheduler;
        if (seed != null) {
            scheduler.push(new Request(seed), null);
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

    public Site addSeed(String url) {
        if (scheduler == null) {
            this.seed = url;
        } else {
            this.seed = url;
            scheduler.push(new Request(url), null);
        }
        return this;
    }

    public void pageCountAdd(int num){
        this.pageCount += num;
    }

    public int getPageCount() {
        return pageCount;
    }

    public String getTaskName() {
        return taskName;
    }

    public void setTaskName(String taskName) {
        this.taskName = taskName;
    }

    public String getSeed() {
        return seed;
    }

    public void setSeed(String seed) {
        this.seed = seed;
    }

    @Override
    public String toString() {
        return "Site{" +
                "taskName='" + taskName + '\'' +
                ", userAgent='" + userAgent + '\'' +
                ", charset='" + charset + '\'' +
                ", sleepTime=" + sleepTime +
                ", retryTimes=" + retryTimes +
                ", cycleRetryTimes=" + cycleRetryTimes +
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
}

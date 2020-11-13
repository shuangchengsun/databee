package com.alan.databee.model;

import java.util.List;
import java.util.Map;

public class BusyReqModel {

    /**
     * 业务码
     */
    private int busyCode;

    /**
     * 业务描述
     */
    private String busyMsg;

    /**
     * 种子
     */
    private String seed;

    /**
     * 深度，默认为1
     */
    private int depth = 1;

    /**
     * 页面处理器
     */
    private List<String> pageProcessor;

    /**
     * 调度器
     */
    private String scheduler;

    /**
     * 下载器
     */
    private String downloader;

    /**
     * 持久化
     */
    private List<String> pipeline;

    /**
     * 额外的参数
     */
    private Map<String, Object> param;

    public int getBusyCode() {
        return busyCode;
    }

    public void setBusyCode(int busyCode) {
        this.busyCode = busyCode;
    }

    public String getBusyMsg() {
        return busyMsg;
    }

    public void setBusyMsg(String busyMsg) {
        this.busyMsg = busyMsg;
    }

    public String getSeed() {
        return seed;
    }

    public void setSeed(String seed) {
        this.seed = seed;
    }

    public int getDepth() {
        return depth;
    }

    public void setDepth(int depth) {
        this.depth = depth;
    }

    public List<String> getPageProcessor() {
        return pageProcessor;
    }

    public void setPageProcessor(List<String> pageProcessor) {
        this.pageProcessor = pageProcessor;
    }

    public String getScheduler() {
        return scheduler;
    }

    public void setScheduler(String scheduler) {
        this.scheduler = scheduler;
    }

    public String getDownloader() {
        return downloader;
    }

    public void setDownloader(String downloader) {
        this.downloader = downloader;
    }

    public List<String> getPipeline() {
        return pipeline;
    }

    public void setPipeline(List<String> pipeline) {
        this.pipeline = pipeline;
    }

    public Map<String, Object> getParam() {
        return param;
    }

    public void setParam(Map<String, Object> param) {
        this.param = param;
    }
}

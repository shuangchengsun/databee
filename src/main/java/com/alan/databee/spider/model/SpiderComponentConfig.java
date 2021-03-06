package com.alan.databee.spider.model;


import com.alan.databee.spider.downloader.Downloader;
import com.alan.databee.spider.pipeline.Pipeline;
import com.alan.databee.spider.processor.PageProcessor;
import com.alan.databee.spider.scheduler.Scheduler;

import java.util.List;

/**
 * @ClassName ComponentConfig
 * @Author sunshuangcheng
 * @Date 2020/10/31 4:49 下午
 * @Version -V1.0
 */
public class SpiderComponentConfig {
    /**
     * 配置版本
     */
    private int version;

    /**
     * 下载器
     */
    private Downloader downloader;

    /**
     * 页面模型
     */
    private PageModel pageModel;

    /**
     * 用于持久化
     */
    private List<Pipeline> pipelines;

    /**
     * 调度器
     */
    private Scheduler scheduler;

    private PageProcessor pageProcessor;

    public int getVersion() {
        return version;
    }

    public void setVersion(int version) {
        this.version = version;
    }

    public Downloader getDownloader() {
        return downloader;
    }

    public void setDownloader(Downloader downloader) {
        this.downloader = downloader;
    }

    public PageModel getPageModel() {
        return pageModel;
    }

    public void setPageModel(PageModel pageModel) {
        this.pageModel = pageModel;
    }

    public List<Pipeline> getPipelines() {
        return pipelines;
    }

    public void setPipelines(List<Pipeline> pipelines) {
        this.pipelines = pipelines;
    }

    public PageProcessor getPageProcessor() {
        return pageProcessor;
    }

    public void setPageProcessor(PageProcessor pageProcessor) {
        this.pageProcessor = pageProcessor;
    }

    public Scheduler getScheduler() {
        return scheduler;
    }

    public void setScheduler(Scheduler scheduler) {
        this.scheduler = scheduler;
    }
}

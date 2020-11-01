package com.alan.databee.spider.model;

import us.codecraft.webmagic.downloader.Downloader;
import us.codecraft.webmagic.pipeline.Pipeline;
import us.codecraft.webmagic.processor.PageProcessor;

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
    private AbstractPageModel pageModel;

    /**
     * 用于持久化
     */
    private List<Pipeline> pipelines;

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

    public AbstractPageModel getPageModel() {
        return pageModel;
    }

    public void setPageModel(AbstractPageModel pageModel) {
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
}

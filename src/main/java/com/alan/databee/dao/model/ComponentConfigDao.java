package com.alan.databee.dao.model;

/**
 * @ClassName ComponentConfig
 * @Author sunshuangcheng
 * @Date 2020/10/31 4:56 下午
 * @Version -V1.0
 */
public class ComponentConfigDao {
    private int id;

    private int version;

    private String pageProcessor;

    private String downloader;

    private String pipelines;

    private String pageModel;

    private String Schedule;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getVersion() {
        return version;
    }

    public void setVersion(int version) {
        this.version = version;
    }

    public String getPageProcessor() {
        return pageProcessor;
    }

    public void setPageProcessor(String pageProcessor) {
        this.pageProcessor = pageProcessor;
    }

    public String getDownloader() {
        return downloader;
    }

    public void setDownloader(String downloader) {
        this.downloader = downloader;
    }

    public String getPipelines() {
        return pipelines;
    }

    public void setPipelines(String pipelines) {
        this.pipelines = pipelines;
    }

    public String getPageModel() {
        return pageModel;
    }

    public void setPageModel(String pageModel) {
        this.pageModel = pageModel;
    }

    public String getSchedule() {
        return Schedule;
    }

    public void setSchedule(String schedule) {
        Schedule = schedule;
    }
}

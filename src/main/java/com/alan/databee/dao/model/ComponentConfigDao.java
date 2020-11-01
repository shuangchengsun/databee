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

    private String parser;

    private String downloader;

    private String persistenceHandler;

    private String pageModel;

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

    public String getParser() {
        return parser;
    }

    public void setParser(String parser) {
        this.parser = parser;
    }

    public String getDownloader() {
        return downloader;
    }

    public void setDownloader(String downloader) {
        this.downloader = downloader;
    }

    public String getPersistenceHandler() {
        return persistenceHandler;
    }

    public void setPersistenceHandler(String persistenceHandler) {
        this.persistenceHandler = persistenceHandler;
    }

    public String getPageModel() {
        return pageModel;
    }

    public void setPageModel(String pageModel) {
        this.pageModel = pageModel;
    }
}

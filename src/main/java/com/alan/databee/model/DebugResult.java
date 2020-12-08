package com.alan.databee.model;

import java.util.HashMap;
import java.util.Map;

public class DebugResult {

    /**
     * 结果，SUCCESS or FAIL
     */
    private int Stat;

    /**
     * 状态描述
     */
    private String msg;

    /**
     * 耗时
     */
    private long time;

    /**
     * 总共爬取了多少的页面
     */
    private int pageCount;

    /**
     * 种子名称
     */
    private String seed;

    /**
     * 页面下载状态 success or fail
     */
    private String downloadStat;

    /**
     * 持久化状态，success or fail
     */
    private String pipelineStat;

    private Map<String,String> extMsg = new HashMap<>();


    public int getStat() {
        return Stat;
    }

    public void setStat(int stat) {
        Stat = stat;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public long getTime() {
        return time;
    }

    public void setTime(long time) {
        this.time = time;
    }

    public int getPageCount() {
        return pageCount;
    }

    public void setPageCount(int pageCount) {
        this.pageCount = pageCount;
    }

    public String getSeed() {
        return seed;
    }

    public void setSeed(String seed) {
        this.seed = seed;
    }

    public String getDownloadStat() {
        return downloadStat;
    }

    public void setDownloadStat(ResultEnum resultEnum) {
        this.downloadStat = resultEnum.getStatMsg();
    }

    public String getPipelineStat() {
        return pipelineStat;
    }

    public void setPipelineStat(ResultEnum resultEnum) {
        this.pipelineStat = resultEnum.getStatMsg();
    }

    public Map<String, String> getExtMsg() {
        return extMsg;
    }
    public void addExtMsg(String key, String msg){
        extMsg.put(key,msg);
    }

}

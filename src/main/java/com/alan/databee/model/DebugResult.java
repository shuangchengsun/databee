package com.alan.databee.model;

public class DebugResult {

    /**
     * 结果，SUCCESS or FAIL
     */
    private String Stat;

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

}

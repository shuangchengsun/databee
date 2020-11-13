package com.alan.databee.model;

import java.io.Serializable;

public class PageResultNode implements Serializable {

    /**
     * 节点序号
     */
    private String num;

    /**
     * 页面的url
     */
    private String url;

    /**
     * 爬取的数据大小，KB，不足1KB的按照1KB算
     */
    private int bytes;

    /**
     * 数据类型，文本数据，二进制数据
     */
    private String contentType;

    /**
     * 原始数据，指的是经过解析后所得到的数据，经过了base64编码
     */
    private byte[] rowData;

    public String getNum() {
        return num;
    }

    public void setNum(String num) {
        this.num = num;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public int getBytes() {
        return bytes;
    }

    public void setBytes(int bytes) {
        this.bytes = bytes;
    }

    public String getContentType() {
        return contentType;
    }

    public void setContentType(String contentType) {
        this.contentType = contentType;
    }

    public byte[] getRowData() {
        return rowData;
    }

    public void setRowData(byte[] rowData) {
        this.rowData = rowData;
    }
}

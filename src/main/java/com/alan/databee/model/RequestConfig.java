package com.alan.databee.model;

import java.util.Map;

public class RequestConfig {
    /**
     * 请求的url
     */
    private String url;

    /**
     * 请求的方法，GET、POST、UPDATE。。。
     */
    private String method = "GET";

    /**
     * 协议
     */
    private String protocol;

    /**
     * 优先级
     */
    private int priority=0;

    /**
     * 请求头
     */
    private Map<String, String> headers;

    /**
     * 其他参数
     */
    private Map<String, String> extras;




    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getMethod() {
        return method;
    }

    public void setMethod(String method) {
        this.method = method;
    }

    public String getProtocol() {
        return protocol;
    }

    public void setProtocol(String protocol) {
        this.protocol = protocol;
    }

    public Map<String, String> getHeaders() {
        return headers;
    }

    public void setHeaders(Map<String, String> headers) {
        this.headers = headers;
    }

    public Map<String, String> getExtras() {
        return extras;
    }

    public void setExtras(Map<String, String> extras) {
        this.extras = extras;
    }

    public int getPriority() {
        return priority;
    }

    public void setPriority(int priority) {
        this.priority = priority;
    }
}

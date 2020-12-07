package com.alan.databee.spider.model;


import com.alan.databee.spider.utils.Experimental;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

/**
 * @ClassName Request
 * @Author sunshuangcheng
 * @Date 2020/11/1 8:57 下午
 * @Version -V1.0
 */
public class
Request implements Serializable, Comparable<Request> {

    private static final long serialVersionUID = 2062192774891352043L;

    public static final String CYCLE_TRIED_TIMES = "_cycle_tried_times";

    private String url;

    private String method;

    private HttpRequestBody requestBody;

    /**
     * Store additional information in extras.
     */
    private Map<String, String> extras;

    /**
     * cookies for current url, if not set use Site's cookies
     */
    private Map<String, String> cookies = new HashMap<String, String>();

    private Map<String, String> headers = new HashMap<String, String>();


    private long priority = 1;

    /**
     * When it is set to TRUE, the downloader will not try to parse response body to text.
     */
    private boolean binaryContent = false;

    private String charset;

    private int retryTimes = 1;


    public Request(String url) {
        this.url = url;
        this.method = "GET";
    }

    public Request(String url, String method) {
        this.url = url;
        this.method = method;
    }

    public long getPriority() {
        return priority;
    }


    @Experimental
    public Request setPriority(long priority) {
        this.priority = priority;
        return this;
    }

    @SuppressWarnings("unchecked")
    public <T> T getExtra(String key) {
        if (extras == null) {
            return null;
        }
        return (T) extras.get(key);
    }

    public <T> Request putExtra(String key, String value) {
        if (extras == null) {
            extras = new HashMap<String, String>();
        }
        extras.put(key, value);
        return this;
    }

    public String getUrl() {
        return url;
    }

    public Map<String, String> getExtras() {
        return extras;
    }

    public Request setExtras(Map<String, String> extras) {
        this.extras = extras;
        return this;
    }

    public Request setUrl(String url) {
        this.url = url;
        return this;
    }


    public String getMethod() {
        return method;
    }

    public Request setMethod(String method) {
        this.method = method;
        return this;
    }

    public Request addCookie(String name, String value) {
        cookies.put(name, value);
        return this;
    }

    public Request addHeader(String name, String value) {
        headers.put(name, value);
        return this;
    }

    public Map<String, String> getCookies() {
        return cookies;
    }

    public Map<String, String> getHeaders() {
        return headers;
    }

    public HttpRequestBody getRequestBody() {
        return requestBody;
    }

    public void setRequestBody(HttpRequestBody requestBody) {
        this.requestBody = requestBody;
    }

    public boolean isBinaryContent() {
        return binaryContent;
    }

    public Request setBinaryContent(boolean binaryContent) {
        this.binaryContent = binaryContent;
        return this;
    }

    public String getCharset() {
        return charset;
    }

    public Request setCharset(String charset) {
        this.charset = charset;
        return this;
    }

    public int getRetryTimes() {
        return retryTimes;
    }

    public void setRetryTimes(int retryTimes) {
        this.retryTimes = retryTimes;
    }

    public Request setHeaders(Map<String, String> headers) {
        this.headers = headers;
        return this;
    }

    @Override
    public String toString() {
        return "Request{" +
                "url='" + url + '\'' +
                ", method='" + method + '\'' +
                ", extras=" + extras +
                ", priority=" + priority +
                ", headers=" + headers +
                ", cookies=" + cookies +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Request request = (Request) o;
        return url.equals(request.url) &&
                method.equals(request.method);
    }

    @Override
    public int hashCode() {
        return Objects.hash(url, method);
    }

    @Override
    public int compareTo(Request o) {
        if (this.priority == o.priority) {
            return this.hashCode() - o.hashCode();
        } else {
            return (int) (this.priority - o.priority);
        }
    }
}


package com.alan.databee.spider.model;

import java.nio.charset.Charset;
import java.util.HashMap;
import java.util.Map;

/**
 * @ClassName Response
 * @Author sunshuangcheng
 * @Date 2020/11/1 9:03 下午
 * @Version -V1.0
 */
public class Response {

    private String url;

    private String host;

    private int port;

    private byte[] rowData;

    private final Map<String, String> headers = new HashMap<>();

    private String dataType;

    private int stateCode;

    private boolean downloadSuccess;

    private Charset charset;
}

package com.alan.databee.wangyiMusic;

import com.alan.databee.spider.Site;
import com.alan.databee.spider.downloader.SeleniumDownloader;
import com.alan.databee.spider.model.Request;
import com.alan.databee.spider.page.Page;
import org.apache.http.Header;
import org.apache.http.HttpEntity;
import org.apache.http.HttpHost;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.util.EntityUtils;

import java.io.IOException;


public class BinaryDownloader {

    public byte[] downloadByHttpClient(String url) throws IOException {
        HttpHost httpHost = new HttpHost("localhost",1234,"http");
        CloseableHttpClient httpClient = HttpClientBuilder.create().build();
        HttpGet httpGet = new HttpGet(url);
        CloseableHttpResponse response = null;
        // 配置信息
        RequestConfig requestConfig = RequestConfig.custom()
                // 设置连接超时时间(单位毫秒)
                .setConnectTimeout(5000)
                // 设置请求超时时间(单位毫秒)
                .setConnectionRequestTimeout(5000)
                // socket读写超时时间(单位毫秒)
                .setSocketTimeout(5000)
                // 设置是否允许重定向(默认为true)
                .setRedirectsEnabled(true).build();


        // 将上面的配置信息 运用到这个Get请求里
        httpGet.setConfig(requestConfig);
        response = httpClient.execute(httpGet);
        Header[] allHeaders = response.getAllHeaders();
        HttpEntity responseEntity = response.getEntity();
        return EntityUtils.toByteArray(responseEntity);
    }

    public byte[] downloadByJs(String url){
        SeleniumDownloader seleniumDownloader = new SeleniumDownloader();
        Request request = new Request("https://music.163.com/#/playlist?id=510763347");
        Site site = Site.me();
        Page download = seleniumDownloader.download(request, site);
        String rawText = download.getRawText();
        System.out.println(rawText);
        return null;
    }

    public static void main(String[] args) {
        BinaryDownloader downloader = new BinaryDownloader();
        downloader.downloadByJs("https://music.163.com");
    }
}
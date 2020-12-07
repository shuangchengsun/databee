package com.alan.databee.fangyanxu;


import com.alan.databee.fangyanxu.pageProcessor.*;

import com.alan.databee.model.RequestConfig;
import com.alan.databee.service.SpiderManager;
import com.alan.databee.service.Task;
import com.alan.databee.spider.DataBee;
import com.alan.databee.spider.Site;
import com.alan.databee.spider.downloader.HttpClientDownloader;

import com.alan.databee.spider.model.Request;
import com.alan.databee.spider.pipeline.ConsolePipeline;
import com.alan.databee.spider.pipeline.LogPipeline;
import com.alan.databee.spider.scheduler.PriorityScheduler;
import com.alan.databee.spider.selector.Json;
import com.alibaba.fastjson.JSON;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;


import java.io.IOException;
import java.util.*;

@SpringBootTest
public class Main {
    @Autowired
    SpiderManager spiderManager;
    public static void main(String[] args) throws IOException {

        SX();
    }

    public static void SX(){
        Request request = new Request("http://www.shanxi.gov.cn/yw/sxyw/index.shtml");
        request.setPriority(0)
                .setMethod("GET");
        RequestConfig config = new RequestConfig();
        config.setMethod(request.getMethod());
        config.setExtras(request.getExtras());
        config.setHeaders(request.getHeaders());
        config.setPriority((int) request.getPriority());
        config.setUrl(request.getUrl());
        String json = JSON.toJSONString(config);
        System.out.println(json);

        Site site = Site.me()
                .setSeed("http://www.shanxi.gov.cn/yw/sxyw/")
                .setSeedRequest(request)
                .setScheduler(new PriorityScheduler())
                .setTaskName("SX")
                .setDownloader(new HttpClientDownloader())
                .processorAddFirst("SXSeedProcessor",new SXSeedProcessor())
                .pipelineAddFirst(new ConsolePipeline());
        Task task = new Task(site,site.getTaskName(),1);
        DataBee dataBee = new DataBee();
        dataBee.setSync(true);
        dataBee.run(task);
    }
    public static void FJ(){
        Request request = new Request("http://www.fujian.gov.cn/was5/web/search?channelid=291575&templet=docs.jsp&sortfield=-DOCORDER&classsql=chnlid%3D41079&prepage=150&page=1");
        request.setPriority(0)
                .setMethod("GET");
        RequestConfig config = new RequestConfig();
        config.setMethod(request.getMethod());
        config.setExtras(request.getExtras());
        config.setHeaders(request.getHeaders());
        config.setPriority((int) request.getPriority());
        config.setUrl(request.getUrl());
        String json = JSON.toJSONString(config);
        System.out.println(json);

        Site site = Site.me()
                .setSeed("http://www.fujian.gov.cn/was5/web/search?channelid=291575&templet=docs.jsp&sortfield=-DOCORDER&classsql=chnlid%3D41079&prepage=150&page=1")
                .setSeedRequest(request)
                .setScheduler(new PriorityScheduler())
                .setTaskName("FJ")
                .setDownloader(new HttpClientDownloader())
                .processorAddFirst("FJSeedProcessor",new FJSeedProcessor())
                .pipelineAddFirst(new ConsolePipeline());
        Task task = new Task(site,site.getTaskName(),0);
        DataBee dataBee = new DataBee();
        dataBee.setSync(true);
        dataBee.run(task);
    }

    public static void HN(){
        Request request = new Request("http://www.hunan.gov.cn/hnszf/hnyw/sy/hnyw1/gl_fgsjpx.html");
        request.setMethod("GET")
                .setPriority(0);
        RequestConfig config = new RequestConfig();
        config.setMethod(request.getMethod());
        config.setExtras(request.getExtras());
        config.setHeaders(request.getHeaders());
        config.setPriority((int) request.getPriority());
        config.setUrl(request.getUrl());
        String json = JSON.toJSONString(config);
        System.out.println(json);
        Site site = Site.me()
                .setSeed("http://www.hunan.gov.cn")
                .setSeedRequest(request)
                .setScheduler(new PriorityScheduler())
                .setDownloader(new HttpClientDownloader())
                .setTaskName("HN")
                .processorAddFirst("HNSeedProcessor",new HNSeedProcessor())
//                .pipelineAddFirst(new LogPipeline())
                .pipelineAddFirst(new ConsolePipeline());
        Task task = new Task(site,site.getTaskName(),0);
        DataBee dataBee = new DataBee();
        dataBee.setSync(true);
        dataBee.run(task);

    }
    public static void HB(){
        Request request = new Request("http://www.hubei.gov.cn/zwgk/hbyw/hbywqb/index.shtml");
        request.setPriority(0);
        request.setMethod("GET");
        RequestConfig config = new RequestConfig();
        config.setMethod(request.getMethod());
        config.setExtras(request.getExtras());
        config.setHeaders(request.getHeaders());
        config.setPriority((int) request.getPriority());
        config.setUrl(request.getUrl());
        String json = JSON.toJSONString(config);
        System.out.println(json);

        Site site = Site.me()
                .setSeed("http://www.hubei.gov.cn/zwgk/hbyw/hbywqb/")
                .setSeedRequest(request)
                .setScheduler(new PriorityScheduler())
                .setDownloader(new HttpClientDownloader())
                .setTaskName("HB")
                .processorAddFirst("HBSeedProcessor",new HBSeedProcessor())
                .pipelineAddFirst(new LogPipeline())
                .pipelineAddFirst(new ConsolePipeline());
        Task task = new Task(site,site.getTaskName(),0);
        DataBee dataBee = new DataBee();
        dataBee.setSync(true);
        dataBee.run(task);

    }

    public static void AH(){
        Request request = new Request("http://www.ah.gov.cn/content/column/6782061?pageIndex=1")
                .setPriority(0)
                .setMethod("GET");
        Site site = Site.me()
                .setSeed("http://www.ah.gov.cn/content/column/6782061?pageIndex=1")
                .setSeedRequest(request)
                .setScheduler(new PriorityScheduler())
                .setDownloader(new HttpClientDownloader())
                .setTaskName("AH")
                .processorAddFirst("AHSeedProcessor",new AHSeedProcessor())
                .pipelineAddFirst(new ConsolePipeline());


        RequestConfig config = new RequestConfig();
        config.setMethod(request.getMethod());
        config.setUrl(request.getUrl());
        config.setPriority((int) request.getPriority());
        config.setExtras(request.getExtras());
        config.setHeaders(request.getHeaders());
        String json = JSON.toJSONString(config);
        System.out.println(json);


        Task task = new Task(site, site.getTaskName(), 1);
        DataBee dataBee = new DataBee();
        dataBee.setSync(true);
        dataBee.run(task);
    }

    public static void JX(){
        Request request = new Request("http://www.jiangxi.gov.cn/module/web/jpage/dataproxy.jsp?startrecord=1&endrecord=54&perpage=18");
        request.setMethod("POST");
        request.addHeader("Host", "www.jiangxi.gov.cn");
        request.addHeader("User-Agent", "Mozilla/5.0 (Macintosh; Intel Mac OS X 10.16; rv:83.0) Gecko/20100101 Firefox/83.0");
        request.addHeader("Accept", "application/xml, text/xml, */*; q=0.01\n");
        request.addHeader("Accept-Language", "zh-CN,zh;q=0.8,zh-TW;q=0.7,zh-HK;q=0.5,en-US;q=0.3,en;q=0.2\n");
        request.addHeader("Accept-Encoding", "gzip, deflate");
        request.addHeader("Content-Type", "application/x-www-form-urlencoded; charset=UTF-8\n");
        request.addHeader("X-Requested-With", "XMLHttpRequest\n");
//        request.addHeader("Content-Length","188");
        request.addHeader("Origin", "http://www.jiangxi.gov.cn\n");

        Map<String, String> headers = request.getHeaders();

        Map<String, String> extras = new HashMap<>();
        extras.put("col", "1");
        extras.put("webid", "3");
        extras.put("path", "http://www.jiangxi.gov.cn/");
        extras.put("columnid", "393");
        extras.put("sourceContentType", "1");
        extras.put("unitid", "45663");
        extras.put("webname", "江西省人民政府");
        extras.put("permissiontype", "0");
        request.setExtras(extras);


        RequestConfig requestConfig = new RequestConfig();
        requestConfig.setUrl(request.getUrl());
        requestConfig.setPriority((int) request.getPriority());
        requestConfig.setHeaders(headers);
        requestConfig.setExtras(extras);
        requestConfig.setMethod(request.getMethod());
        String s = JSON.toJSONString(requestConfig);

        System.out.println(s);

        RequestConfig requestConfig1 = JSON.parseObject(s, RequestConfig.class);
        Request request1 = new Request(requestConfig1.getUrl());
        request1.setMethod(requestConfig1.getMethod());
        request1.setHeaders(requestConfig1.getHeaders());
        request1.setExtras(requestConfig1.getExtras());
        request1.setPriority(requestConfig1.getPriority());


        Site site = Site.me();
        site.setSeed("http://www.jiangxi.gov.cn/module/web/jpage/dataproxy.jsp?startrecord=1&endrecord=54&perpage=18");
        site.setSeedRequest(request1);
        site.setScheduler(new PriorityScheduler())
                .setDownloader(new HttpClientDownloader())
                .setTaskName("JX.govTask")
                .processorAddLast("seedProcessor", new JXSeedProcessor())
                .pipelineAddLast("console", new ConsolePipeline());


        Task task = new Task(site, site.getTaskName(), 0);
        DataBee dataBee = new DataBee();
        dataBee.setSync(true);
        dataBee.run(task);
    }

    @Test
    public void test(){
        spiderManager.runDailyTask();
    }
}

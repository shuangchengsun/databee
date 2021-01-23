package com.alan.databee.FanRenXiuXianZhuan;

import com.alan.databee.FanRenXiuXianZhuan.pageProcessor.DefaultProcessor;
import com.alan.databee.FanRenXiuXianZhuan.pipeline.FileWritePipeline;
import com.alan.databee.model.RequestConfig;
import com.alan.databee.service.Task;
import com.alan.databee.spider.DataBee;
import com.alan.databee.spider.Site;

import com.alan.databee.spider.downloader.HttpClientDownloader;
import com.alan.databee.spider.downloader.SeleniumDownloader;
import com.alan.databee.spider.model.Request;
import com.alan.databee.spider.pipeline.ConsolePipeline;
import com.alan.databee.spider.scheduler.PriorityScheduler;
import com.alibaba.fastjson.JSON;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public class main {
    @Test
    public void main() {
        main0(null);
    }
    public void main0(String[] args){
        Request request = new Request("https://www.kunnu.com/fanren/")
                .setPriority(0)
                .setMethod("GET");
        RequestConfig requestConfig = new RequestConfig();
        requestConfig.setMethod(request.getMethod());
        requestConfig.setExtras(request.getExtras());
        requestConfig.setHeaders(request.getHeaders());
        requestConfig.setPriority((int) request.getPriority());
        requestConfig.setUrl(request.getUrl());
        String json = JSON.toJSONString(requestConfig);
        System.out.println(json);

        Site site = Site.me()
                .setSeed("https://www.kunnu.com/fanren/")
                .setSeedRequest(request)
                .setScheduler(new PriorityScheduler())
                .setTaskName("FRXXZ")
                .setDownloader(new SeleniumDownloader().setSleepTime(1000))
                .processorAddFirst("default",new DefaultProcessor())
//                .pipelineAddFirst(new ConsolePipeline())
                .pipelineAddLast(new FileWritePipeline());
        Task task = new Task(site,site.getTaskName(),1);
        DataBee dataBee = new DataBee();
        dataBee.setSync(true);
        dataBee.run(task);
    }
}

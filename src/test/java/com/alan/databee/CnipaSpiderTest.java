package com.alan.databee;

import com.alan.databee.pageProcessor.CnipaPageProcessor;
import com.alan.databee.pipeline.ConsolePipeline;
import com.alan.databee.spider.Site;
import com.alan.databee.spider.TaskRunner;
import com.alan.databee.spider.downloader.HttpClientDownloader;
import com.alan.databee.spider.downloader.SeleniumDownloader;
import com.alan.databee.spider.scheduler.QueueScheduler;
import org.junit.Test;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public class CnipaSpiderTest {

    @Test
    public void test(){
        Site site = Site.me();
        site.addUrl("https://www.cnipa.gov.cn/col/col88/index.html")
                .setDownloader(new SeleniumDownloader())
                .setScheduler(new QueueScheduler())
                .processorAddLast("defaultProcessor",new CnipaPageProcessor())
                .pipelineAddLast("defaultPipeline",new ConsolePipeline());

        TaskRunner runner = new TaskRunner(site).setSync(true);

        runner.run();
    }
}

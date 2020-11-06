package com.alan.databee.spider.service;

import com.alan.databee.spider.exception.SpiderErrorEnum;
import com.alan.databee.spider.exception.SpiderTaskException;
import com.alan.databee.spider.model.SpiderComponentConfig;
import com.alan.databee.spider.model.SpiderTaskConfig;
import us.codecraft.webmagic.Site;
import us.codecraft.webmagic.Spider;
import us.codecraft.webmagic.model.OOSpider;
import us.codecraft.webmagic.pipeline.Pipeline;

/**
 * @ClassName SpiderWorker
 * @Author sunshuangcheng
 * @Date 2020/10/31 5:00 下午
 * @Version -V1.0
 */
public class Task implements Comparable<Task> {
    private SpiderTaskConfig taskConfig;
    private int priority;

    public Task(SpiderTaskConfig taskConfig) {
        this.taskConfig = taskConfig;
        this.priority = taskConfig.getPriority();
    }

    public void run() {
        SpiderComponentConfig componentConfig = taskConfig.getSpiderComponentConfig();
        Spider spider = null;
        if (componentConfig.getPageModel() != null) {
            spider = OOSpider.create(Site.me(), componentConfig.getPageModel().getClass())
                    .addUrl(taskConfig.getUrl())
                    .setDownloader(componentConfig.getDownloader());
            for (Pipeline pipeline : componentConfig.getPipelines()) {
                spider.addPipeline(pipeline);
            }
        } else if (componentConfig.getPageProcessor() != null) {
            spider = OOSpider.create(componentConfig.getPageProcessor())
                    .addUrl(taskConfig.getUrl())
                    .setDownloader(componentConfig.getDownloader());
            for (Pipeline pipeline : componentConfig.getPipelines()) {
                spider.addPipeline(pipeline);
            }
        } else {
            throw new SpiderTaskException(SpiderErrorEnum.Component_Not_Fount);
        }
        spider.run();
    }

    @Override
    public int compareTo(Task o) {
        return (priority - o.priority);
    }
}

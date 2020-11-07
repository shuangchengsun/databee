package com.alan.databee.spider.service;

import com.alan.databee.spider.Site;
import com.alan.databee.spider.TaskRunner;
import com.alan.databee.spider.exception.SpiderErrorEnum;
import com.alan.databee.spider.exception.SpiderTaskException;
import com.alan.databee.spider.model.SpiderComponentConfig;
import com.alan.databee.spider.model.SpiderTaskConfig;
import com.alan.databee.spider.pipeline.Pipeline;
import com.alan.databee.spider.processor.PageProcessor;

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
        configCheck(componentConfig);
        Site site = new Site()
                .addUrl(taskConfig.getUrl())
                .setDownloader(componentConfig.getDownloader());
        for (Pipeline pipeline : componentConfig.getPipelines()) {
            site.pipelineAddLast(pipeline);
        }
        site.processorAddLast(componentConfig.getPageProcessor());
        TaskRunner spider = new TaskRunner(site).setSync(true);
        spider.run();
    }

    private void configCheck(SpiderComponentConfig componentConfig) {
        if (componentConfig.getPageProcessor() == null
                || componentConfig.getDownloader() == null
                || componentConfig.getPipelines() == null
                || componentConfig.getPipelines().isEmpty()) {
            throw new SpiderTaskException(SpiderErrorEnum.Component_Not_Fount);
        }
    }

    @Override
    public int compareTo(Task o) {
        return (priority - o.priority);
    }
}

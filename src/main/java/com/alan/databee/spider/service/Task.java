package com.alan.databee.spider.service;

import com.alan.databee.common.util.log.LoggerUtil;
import com.alan.databee.spider.Site;
import com.alan.databee.spider.exception.SpiderTaskException;
import com.alan.databee.spider.model.SpiderComponentConfig;
import com.alan.databee.spider.model.SpiderTaskConfig;
import com.alan.databee.spider.pipeline.Pipeline;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @ClassName SpiderWorker
 * @Author sunshuangcheng
 * @Date 2020/10/31 5:00 下午
 * @Version -V1.0
 */
public class Task implements Comparable<Task> {
    private static final Logger LOGGER = LoggerFactory.getLogger(Task.class);

    private SpiderTaskConfig taskConfig;
    private int priority;
    public String taskName;
    private Site site;

    public Task(SpiderTaskConfig taskConfig) throws SpiderTaskException{
        this.taskConfig = taskConfig;
        this.taskName = taskConfig.getTaskName();
        this.priority = taskConfig.getPriority();
        SpiderComponentConfig componentConfig = taskConfig.getSpiderComponentConfig();
        configCheck(componentConfig);
        site = new Site()
                .addSeed(taskConfig.getUrl())
                .setDownloader(componentConfig.getDownloader())
                .setScheduler(componentConfig.getScheduler());
        for (Pipeline pipeline : componentConfig.getPipelines()) {
            site.pipelineAddLast(pipeline);
        }
        site.processorAddLast(componentConfig.getPageProcessor());
    }

    private void configCheck(SpiderComponentConfig componentConfig) {
        if (componentConfig.getPageProcessor() == null) {
            LoggerUtil.error(LOGGER,"task组件异常",taskName);
        }
    }

    public SpiderTaskConfig getTaskConfig() {
        return taskConfig;
    }

    public Site getSite() {
        return site;
    }

    @Override
    public int compareTo(Task o) {
        return (priority - o.priority);
    }
}

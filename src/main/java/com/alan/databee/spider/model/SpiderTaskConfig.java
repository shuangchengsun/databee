package com.alan.databee.spider.model;

import java.util.Date;

/**
 * @ClassName SpiderTaskConfig
 * @Author sunshuangcheng
 * @Date 2020/10/31 4:46 下午
 * @Version -V1.0
 */
public class SpiderTaskConfig {

    /**
     * the name of the task
     */
    private String taskName;

    /**
     * 任务的类型
     */
    private String taskType;

    /**
     * 该任务的创造者
     */
    private User creator;

    /**
     * 该任务的创造时间
     */
    private Date gmtCreate;

    /**
     * 该任务的修改时间
     */
    private Date gmtModify;

    /**
     * 该任务的修改者
     */
    private User modifier;

    /**
     * 该任务的组件配置
     */
    private SpiderComponentConfig spiderComponentConfig;

    /**
     * 任务的爬取深度
     */
    private int depth;

    /**
     * 任务的过期时间
     */
    private Date expire;

    /**
     * 任务的目标链接
     */
    private String url;

    /**
     * 任务的优先级
     */
    private int priority;

    /**
     * 开启的线程数
     */
    private int thread;


    public String getTaskName() {
        return taskName;
    }

    public void setTaskName(String taskName) {
        this.taskName = taskName;
    }

    public String getTaskType() {
        return taskType;
    }

    public void setTaskType(String taskType) {
        this.taskType = taskType;
    }

    public User getCreator() {
        return creator;
    }

    public void setCreator(User creator) {
        this.creator = creator;
    }

    public Date getGmtCreate() {
        return gmtCreate;
    }

    public void setGmtCreate(Date gmtCreate) {
        this.gmtCreate = gmtCreate;
    }

    public Date getGmtModify() {
        return gmtModify;
    }

    public void setGmtModify(Date gmtModify) {
        this.gmtModify = gmtModify;
    }

    public User getModifier() {
        return modifier;
    }

    public void setModifier(User modifier) {
        this.modifier = modifier;
    }

    public SpiderComponentConfig getSpiderComponentConfig() {
        return spiderComponentConfig;
    }

    public void setSpiderComponentConfig(SpiderComponentConfig spiderComponentConfig) {
        this.spiderComponentConfig = spiderComponentConfig;
    }

    public int getDepth() {
        return depth;
    }

    public void setDepth(int depth) {
        this.depth = depth;
    }

    public Date getExpire() {
        return expire;
    }

    public void setExpire(Date expire) {
        this.expire = expire;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public int getPriority() {
        return priority;
    }

    public void setPriority(int priority) {
        this.priority = priority;
    }

    public int getThread() {
        return thread;
    }

    public void setThread(int thread) {
        this.thread = thread;
    }
}

package com.alan.databee.spider.scheduler;


import com.alan.databee.spider.Task;
import com.alan.databee.spider.model.Request;

public interface Scheduler {
    /**
     * add a url to fetch
     *
     * @param request request
     * @param task task
     */
    public void push(Request request, Task task);

    /**
     * get an url to crawl
     *
     * @param task the task of spider
     * @return the url to crawl
     */
    public Request poll(Task task);
}

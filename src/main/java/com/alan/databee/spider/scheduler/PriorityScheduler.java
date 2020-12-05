package com.alan.databee.spider.scheduler;

import com.alan.databee.spider.Task;
import com.alan.databee.spider.model.Request;

import java.util.Set;
import java.util.TreeSet;

public class PriorityScheduler implements Scheduler{

    /**
     * @param request request 请求
     * @param task    task 无用
     */
    private final TreeSet<Request> requests = new TreeSet<>();
    @Override
    public void push(Request request, Task task) {
        requests.add(request);
    }

    @Override
    public Request poll(Task task) {
        Request request = requests.pollFirst();
        return request;
    }
}

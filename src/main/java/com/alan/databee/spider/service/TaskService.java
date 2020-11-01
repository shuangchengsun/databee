package com.alan.databee.spider.service;

import com.alan.databee.spider.model.SpiderTaskConfig;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.PriorityQueue;

/**
 * @ClassName TaskServie
 * @Author sunshuangcheng
 * @Date 2020/10/31 8:40 下午
 * @Version -V1.0
 */
@Service
public class TaskService {

    @Autowired
    private TaskConfigService configService;

    public PriorityQueue<Task> AssemblyTask() {
        List<SpiderTaskConfig> configs = configService.getAllTask();
        PriorityQueue<Task> priorityQueue = new PriorityQueue<>();
        for (SpiderTaskConfig config : configs) {
            Task task = new Task(config);
            priorityQueue.add(task);
        }
        return priorityQueue;
    }
}

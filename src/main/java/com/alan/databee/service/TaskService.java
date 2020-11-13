package com.alan.databee.service;

import com.alan.databee.model.BusyReqModel;
import com.alan.databee.model.DebugResult;
import com.alan.databee.spider.model.SpiderTaskConfig;
import com.alan.databee.spider.model.User;
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

    public Task genTask(){
        return null;
    }

    public DebugResult submitTask(BusyReqModel model, User user) {
        // 检查组件是否能够编译通过

        return null;
    }
}

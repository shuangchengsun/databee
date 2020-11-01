package com.alan.databee.spider;

import com.alan.databee.spider.exception.SpiderTaskException;
import com.alan.databee.spider.service.Task;
import com.alan.databee.spider.service.TaskService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.util.PriorityQueue;

/**
 * @ClassName SpiderManager
 * @Author sunshuangcheng
 * @Date 2020/10/31 8:38 下午
 * @Version -V1.0
 */
@Service
public class SpiderManager {

    @Autowired
    TaskService taskService;

//    @Scheduled(cron = "30 * * * * ?")
    public void runTask(){

        PriorityQueue<Task> tasks = taskService.AssemblyTask();
        for (Task task:tasks){
            try {
                task.run();
            }catch (SpiderTaskException e){
                e.printStackTrace();
            }
        }
    }
}

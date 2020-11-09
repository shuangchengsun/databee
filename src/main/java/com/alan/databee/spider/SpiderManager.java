package com.alan.databee.spider;

import com.alan.databee.common.util.log.LoggerUtil;
import com.alan.databee.spider.service.Task;
import com.alan.databee.spider.service.TaskService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.util.Date;
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

    private static final Logger LOGGER = LoggerFactory.getLogger("taskLogger");

    /**
     * 运行日常的任务。采用Spring的定时调度完成。
     */
    @Scheduled(cron = "0 0/2 * * * ?")
    public void runDailyTask() {
        Date start = new Date();
        int sum = 0;
        // 其中包括一些组装失败的任务，或者组件非法的任务。此处得到的任务数和数据库中配置的数量完全一致
        PriorityQueue<Task> tasks = taskService.AssemblyTask();
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("失败的任务：");
        DataBee dataBee = new DataBee().setSync(true);
        for (Task task : tasks) {
            int res = dataBee.run(task);
            sum += res;
            if (res == 0) {
                stringBuilder.append(task.taskName).append(", ");
            }
        }
        Date finish = new Date();

        // 日志格式：开始时间，结束时间，总任务，成功数量，失败数量，失败的任务名称。
        LoggerUtil.info(LOGGER, start, finish, tasks.size(), sum, tasks.size() - sum, stringBuilder.toString());
    }
}

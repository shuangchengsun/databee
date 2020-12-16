package com.alan.databee.service;

import com.alan.databee.common.util.log.LoggerUtil;
import com.alan.databee.model.BusyReqModel;
import com.alan.databee.model.DebugResult;
import com.alan.databee.spider.DataBee;
import com.alan.databee.spider.Site;
import com.alan.databee.spider.pipeline.EchoPipeline;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Queue;

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

    private static final Logger LOGGER = LoggerFactory.getLogger("spiderManagerLogger");

    private DataBee dataBee = new DataBee().setSync(true);

    /**
     * 运行日常的任务。采用Spring的定时调度完成。
     */
    @Scheduled(cron = "0 0 18 1/1 * ?")
    public void runDailyTask() {
        Date start = new Date();
        int sum = 0;
        // 其中包括一些组装失败的任务，或者组件非法的任务。此处得到的任务数和数据库中配置的数量完全一致
        Queue<Task> tasks = taskService.AssemblyTask();
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

    /**
     * 在正式配置任务之前，做一个全流程的调试，
     *
     * @return
     */
    public DebugResult runDebugTask(BusyReqModel model) {
        Task task = taskService.genTask(model);
        int i = dataBee.run(task);
        Site site = task.getSite();
        EchoPipeline echoPipeline = (EchoPipeline) site.getComByName("EchoPipeline");
        List<Map<String, Object>> items = echoPipeline.getItems();
        StringBuilder builder = new StringBuilder();
        for (Map<String, Object> messages : items) {
            Object pageURL = messages.get("pageURL");
            messages.remove("pageURL");
            builder.append(pageURL).append(":").append("\n");
            for (Map.Entry<String, Object> entry : messages.entrySet()) {
                builder.append(entry.getKey()).append(":   ").append(entry.getValue()).append("\n");
            }
        }
        String msg = builder.toString();
        DebugResult result = new DebugResult();
        result.setStat(i);
        result.setSeed(task.getSite().getSeed());
        result.setMsg(msg);
        return result;
    }

    /**
     * 此处是调试一些组件，主要以PageProcessor，和Downloader为主，
     *
     * @return 测试结果。
     */
    public DebugResult componentDebug() {

        return null;
    }

    public void runTaskByName(String taskName){
        Task task = taskService.AssemblyTask(taskName);
        dataBee.run(task);

    }
}

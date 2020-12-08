package com.alan.databee.service;

import com.alan.databee.common.ScriptUtil;
import com.alan.databee.common.util.StringUtil;
import com.alan.databee.dao.model.ComponentConfigDao;
import com.alan.databee.dao.model.ComponentDao;
import com.alan.databee.dao.model.SpiderConfigDao;
import com.alan.databee.model.*;
import com.alan.databee.spider.exception.ScriptException;
import com.alan.databee.spider.exception.SpiderErrorEnum;
import com.alan.databee.spider.model.SpiderTaskConfig;
import com.alan.databee.spider.script.ScriptService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

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

    @Autowired
    private ClassService classService;

    private static final int SUBMIT_NEW = 0x01;
    private static final int SUCCESS_STAT = 0x00;

    public Queue<Task> AssemblyTask() {
        List<SpiderTaskConfig> configs = configService.getAllTask();
        Queue<Task> priorityQueue = new LinkedList<>();
        for (SpiderTaskConfig config : configs) {
            Task task = new Task(config);
            priorityQueue.add(task);
        }
        return priorityQueue;
    }

    /**
     * 依据发送的数据，生成一个Task
     * @param model
     * @return
     */
    public Task genTask(BusyReqModel model) {
        return null;
    }

    /**
     * 提交一个任务， 首先检查任务中的组件是否合法、其次生成各种组件的配置数据、最后提交数据库
     *
     * @param model 数据模型
     * @param user  标志是谁提交的
     * @return
     */
    public DebugResult submitTask(BusyReqModel model, User user) {
        // 1、检查传参是否正确
        DebugResult result = null;
        boolean comCheck = preCheck(model, SUBMIT_NEW);

        // 2、检查组件是否使用了默认的组件名字，并检查组件是否能够通过编译
        String downloader = model.getDownloader();

        comCheck &= classService.checkScript(downloader);

        List<String> pageProcessor = model.getPageProcessor();
        for (String processor : pageProcessor) {
            if (processor == null || processor.length() == 0) {
                comCheck = false;
            }
            comCheck &= classService.checkScript(processor);
        }
        List<String> pipelines = model.getPipeline();
        for (String pipeline : pipelines) {
            comCheck &= classService.checkScript(pipeline);
        }

        String scheduler = model.getScheduler();
        comCheck &= classService.checkScript(scheduler);

        if (!comCheck) {
            result = buildError(ResultEnum.Component_Missing);
            result.setTime(0);
            result.setDownloadStat(ResultEnum.Result_Undefined);
            result.setPipelineStat(ResultEnum.Result_Undefined);
            return result;
        }
        // 组装数据
        SpiderConfigDao configDao = configService.genSpiderConfig(model, user.getUserName());

        try {

            ComponentConfigDao componentConfigDao = configService.genComConfig(model);
            componentConfigDao.setName(configDao.getComponentConfig());
            componentConfigDao.setVersion(1);
            List<ComponentDao> componentDaoList = configService.genCom(model);
            // 写入数据库
            configService.saveSpiderConfig(configDao);
            configService.saveComponentConfig(componentConfigDao);
            for (ComponentDao componentDao : componentDaoList) {
                configService.saveComponent(componentDao);
            }
            result = new DebugResult();
            result.setStat(SUCCESS_STAT);
            result.setMsg("任务提交成功");
        } catch (ScriptException exception) {
            result = new DebugResult();
            result.setStat(ResultEnum.Component_Compiler_Error.getStatCode());
            result.setMsg("在编译时找到了多个class");

        }
        return result;
    }

    private boolean preCheck(BusyReqModel model, int busyType) {
        if (model.getBusyCode() != busyType) {
            return false;
        }
        if (StringUtil.isEmpty(model.getSeed()) || StringUtil.isBlank(model.getSeed())) {
            return false;
        }
        List<String> pageProcessor = model.getPageProcessor();
        if (pageProcessor == null || pageProcessor.size() <= 0) {
            return false;
        }
        for (String st : pageProcessor) {
            if (StringUtil.isEmpty(st) || StringUtil.isBlank(st)) {
                return false;
            }
        }
        return true;
    }

    private DebugResult buildError(ResultEnum resultEnum) {
        DebugResult result = new DebugResult();
        result.setStat(result.getStat());
        result.setMsg(resultEnum.getStatMsg());
        return result;
    }
}

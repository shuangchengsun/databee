package com.alan.databee.service;

import com.alan.databee.common.util.StringUtil;
import com.alan.databee.dao.model.ComponentConfigDao;
import com.alan.databee.dao.model.ComponentDao;
import com.alan.databee.dao.model.SpiderConfigDao;
import com.alan.databee.model.BusyReqModel;
import com.alan.databee.model.CommonConfig;
import com.alan.databee.model.DebugResult;
import com.alan.databee.model.ResultEnum;
import com.alan.databee.spider.exception.ScriptException;
import com.alan.databee.spider.exception.SpiderErrorEnum;
import com.alan.databee.spider.model.SpiderTaskConfig;
import com.alan.databee.spider.model.User;
import com.alan.databee.spider.script.ScriptService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.PriorityQueue;
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
    private ScriptService scriptService;
    private static final int SUBMIT_NEW = 0x01;
    private static final int SUCCESS_STAT = 0x00;

    public PriorityQueue<Task> AssemblyTask() {
        List<SpiderTaskConfig> configs = configService.getAllTask();
        PriorityQueue<Task> priorityQueue = new PriorityQueue<>();
        for (SpiderTaskConfig config : configs) {
            Task task = new Task(config);
            priorityQueue.add(task);
        }
        return priorityQueue;
    }

    public Task genTask(BusyReqModel model) {
        return null;
    }

    public DebugResult submitTask(BusyReqModel model, User user) {
        // 1、检查传参是否正确
        DebugResult result = null;
        if (!preCheck(model, SUBMIT_NEW)) {
            result = buildError(ResultEnum.Component_Missing);
            result.setTime(0);
            result.setDownloadStat(ResultEnum.Result_Undefined.getStatMsg());
            result.setPipelineStat(ResultEnum.Result_Undefined.getStatMsg());
            return result;
        }
        // 2、检查组件是否使用了默认的组件名字，并检查组件是否能够通过编译
        String downloader = model.getDownloader();
        if (downloader != null && downloader.length() <= 32) {
            // 说明使用的是默认的downloader
            if (!CommonConfig.isContainCom(downloader)) {
                result = buildError(ResultEnum.Component_Undefined);
                result.addExtMsg("ErrMsg", "使用通用的downloader时名称错误，组件名：" + downloader);
                return result;
            }
        } else if (downloader != null) {
            // 使用自己编写的组件
            result = checkComCompile(downloader);
            if (result != null && result.getStat() != SUCCESS_STAT)
                return result;
        }
        List<String> pageProcessor = model.getPageProcessor();
        for (String processor : pageProcessor) {
            result = checkComCompile(processor);
            if (result != null && result.getStat() != SUCCESS_STAT)
                return result;
        }
        List<String> pipelines = model.getPipeline();
        for (String pipeline : pipelines) {
            if(pipeline.length()>32) {
                result = checkComCompile(pipeline);
                if (result != null && result.getStat() != SUCCESS_STAT)
                    return result;
            }else {
                if(!CommonConfig.isContainCom(pipeline)){
                    result = buildError(ResultEnum.Component_Undefined);
                    result.addExtMsg("ErrMsg", "使用通用的downloader时名称错误，组件名：" + pipeline);
                    return result;
                }
            }
        }
        String scheduler = model.getScheduler();
        result = checkComCompile(scheduler);
        if (result != null && result.getStat() != SUCCESS_STAT)
            return result;

        // 组装数据
        SpiderConfigDao configDao = AssemblyDao(model, user);
        ComponentConfigDao componentConfigDao = new ComponentConfigDao();
        componentConfigDao.setName(configDao.getComponentConfig());
        componentConfigDao.setVersion(1);
        List<ComponentDao> componentDaoList = new ArrayList<>();
        try {
            if (downloader != null && downloader.length() > 32) {
                ComponentDao downloadComponentDao = new ComponentDao();
                String comName = getComName(downloader);
                comName = user.getUserName() + comName;
                if (comName.length() > 32) {
                    comName = comName.substring(0, 32);
                }
                downloadComponentDao.setName(comName);
                downloadComponentDao.setContent(downloader);
                componentDaoList.add(downloadComponentDao);
                componentConfigDao.setDownloader(comName);
            } else if (downloader != null) {
                componentConfigDao.setDownloader(downloader);
            }

            // 目前，只支持一个
            for (String processor : pageProcessor) {
                ComponentDao processorDao = new ComponentDao();
                String comName = getComName(processor);
                comName = user.getUserName() + comName;
                if (comName.length() > 32) {
                    comName = comName.substring(0, 32);
                }
                processorDao.setName(comName);
                processorDao.setContent(processor);
                componentDaoList.add(processorDao);
                componentConfigDao.setPageProcessor(comName);
            }

            StringBuilder stringBuilder = new StringBuilder();
            for (String pipeline : pipelines) {
                if (pipeline != null && pipeline.length() > 32) {
                    ComponentDao pipelineDao = new ComponentDao();
                    String comName = getComName(pipeline);
                    comName = user.getUserName() + comName;
                    if (comName.length() > 32) {
                        comName = comName.substring(0, 32);
                    }
                    pipelineDao.setName(comName);
                    pipelineDao.setContent(pipeline);
                    componentDaoList.add(pipelineDao);
                    stringBuilder.append(comName).append(",");
                } else if (pipeline != null) {
                    stringBuilder.append(pipeline).append(",");
                }
            }
            String pipelineName = stringBuilder.deleteCharAt(stringBuilder.length()).toString();
            componentConfigDao.setPipelines(pipelineName);

            if (scheduler != null && scheduler.length() > 32) {
                ComponentDao schedulerDao = new ComponentDao();
                String comName = getComName(scheduler);
                comName = user.getUserName() + comName;
                if (comName.length() > 32) {
                    comName = comName.substring(0, 32);
                }
                schedulerDao.setName(comName);
                schedulerDao.setContent(scheduler);
                componentConfigDao.setSchedule(comName);
                componentDaoList.add(schedulerDao);
            } else if (scheduler != null) {
                componentConfigDao.setSchedule(scheduler);
            }
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

    private String getComName(String com) throws ScriptException {
        Pattern pattern = Pattern.compile("^public(?<type>\\w+|\\s+)class (?<name>\\w+)");
        Matcher matcher = pattern.matcher(com);
        int find = 0;
        String name = null;
        while (matcher.find()) {
            if (find >= 1) {
                throw new ScriptException(SpiderErrorEnum.Script_ClassNum_Error);
            }
            find++;
            name = matcher.group("name");
        }
        return name;
    }

    private DebugResult checkComCompile(String com) {
        if(com == null || com.length()==0){
            return null;
        }
        DebugResult result = null;
        try {
            String comName = getComName(com);
            scriptService.genClass(comName, com);
        } catch (ScriptException exception) {
            result = buildError(ResultEnum.Component_Compiler_Error);
            result.addExtMsg("ErrMsg", "组件的代码编译未通过，组件名: " + com);
        }
        return result;
    }

    private SpiderConfigDao AssemblyDao(BusyReqModel model, User user) {
        SpiderConfigDao configDao = new SpiderConfigDao();
        if (model.getBusyCode() == SUBMIT_NEW) {
            configDao.setCreator(user.getUserName());

            long l = System.currentTimeMillis();
            int i = user.getUserName().hashCode();
            String componentConfigName = String.valueOf(i).substring(0, 16) + String.valueOf(l) + "_v1";
            configDao.setComponentConfig(componentConfigName);

            configDao.setDepth(model.getDepth());

            configDao.setGmtModify(new Date());
            configDao.setGmtCreate(new Date());
            configDao.setModifier(user.getUserName());
            configDao.setPriority(1);
            configDao.setTaskName(model.getTaskName());
            configDao.setTaskType("daily");
            configDao.setThread(1);
            configDao.setUrl(model.getSeed());
        }
        return configDao;
    }

}

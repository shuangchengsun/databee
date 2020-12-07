package com.alan.databee.service;

import com.alan.databee.common.util.log.LoggerUtil;
import com.alan.databee.dao.mapper.ComponentConfigMapper;
import com.alan.databee.dao.mapper.ComponentMapper;
import com.alan.databee.dao.mapper.SpiderConfigMapper;
import com.alan.databee.dao.mapper.UserMapper;
import com.alan.databee.dao.model.ComponentConfigDao;
import com.alan.databee.dao.model.ComponentDao;
import com.alan.databee.dao.model.SpiderConfigDao;
import com.alan.databee.dao.model.UserDao;
import com.alan.databee.model.RequestConfig;
import com.alan.databee.spider.downloader.Downloader;
import com.alan.databee.spider.downloader.HttpClientDownloader;
import com.alan.databee.spider.exception.ClassServiceException;
import com.alan.databee.spider.model.Request;
import com.alan.databee.spider.model.SpiderComponentConfig;
import com.alan.databee.spider.model.SpiderTaskConfig;
import com.alan.databee.model.User;
import com.alan.databee.spider.pipeline.ConsolePipeline;
import com.alan.databee.spider.pipeline.Pipeline;
import com.alan.databee.spider.processor.PageProcessor;
import com.alan.databee.spider.scheduler.Scheduler;
import com.alibaba.fastjson.JSON;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.LinkedList;
import java.util.List;

/**
 * @ClassName TaskConfigService
 * @Author sunshuangcheng
 * @Date 2020/10/31 9:03 下午
 * @Version -V1.0
 */
@Service
public class TaskConfigService {
    private static final Logger LOGGER = LoggerFactory.getLogger("taskConfigLogger");

    @Autowired(required = false)
    private SpiderConfigMapper spiderConfigMapper;


    @Autowired(required = false)
    private ComponentConfigMapper componentConfigMapper;

    @Autowired(required = false)
    private UserMapper userMapper;

    @Autowired(required = false)
    private ComponentMapper componentMapper;

    @Autowired
    private ClassService classService;

    public List<SpiderTaskConfig> getAllTask() {
        List<SpiderConfigDao> daily = spiderConfigMapper.getDaily();
        List<SpiderTaskConfig> taskConfigs = new LinkedList<>();

        for (SpiderConfigDao spiderConfigDao : daily) {
            SpiderTaskConfig taskConfig = new SpiderTaskConfig();

            String creator = spiderConfigDao.getCreator();
            UserDao userdao = userMapper.getByName(creator);
            taskConfig.setCreator(userDaoToUser(userdao));

            taskConfig.setDepth(spiderConfigDao.getDepth());

            taskConfig.setExpire(spiderConfigDao.getExpireTime());

            taskConfig.setGmtCreate(spiderConfigDao.getGmtCreate());

            taskConfig.setGmtModify(spiderConfigDao.getGmtModify());

            String modifierName = spiderConfigDao.getModifier();
            UserDao modifierDao = userMapper.getByName(modifierName);
            taskConfig.setModifier(userDaoToUser(modifierDao));

            taskConfig.setPriority(spiderConfigDao.getPriority());


            String componentConfigName = spiderConfigDao.getComponentConfig();
            ComponentConfigDao componentConfigDao = componentConfigMapper.getByName(componentConfigName);

            try {
                RequestConfig requestConfig = JSON.parseObject(spiderConfigDao.getSeedRequestConfig(), RequestConfig.class);
                Request request = new Request(requestConfig.getUrl());
                request.setPriority(requestConfig.getPriority());
                request.setMethod(requestConfig.getMethod());
                request.setHeaders(requestConfig.getHeaders());
                request.setExtras(requestConfig.getExtras());
                taskConfig.setSeedRequest(request);


                taskConfig.setSpiderComponentConfig(componentConfigCreator(componentConfigDao));

                taskConfig.setTaskName(spiderConfigDao.getTaskName());

                taskConfig.setTaskType(spiderConfigDao.getTaskType());

                taskConfig.setUrl(spiderConfigDao.getUrl());

                taskConfig.setThread(spiderConfigDao.getThread());

                // 打印出一个task的基础配置
                LoggerUtil.info(LOGGER, taskConfig.getTaskName(), taskConfig.getCreator(), taskConfig.getModifier(),
                        componentConfigDao.getDownloader(), componentConfigDao.getSchedule(), componentConfigDao.getPageProcessor(),
                        componentConfigDao.getPipelines());
            } catch (ClassServiceException e) {
                // 错误日志
                LoggerUtil.error(LOGGER, taskConfig.getTaskName(), componentConfigDao.getDownloader(), componentConfigDao.getSchedule(), componentConfigDao.getPageProcessor(),
                        componentConfigDao.getPipelines(), e);
            }
            // 此处不管taskConfig是否合法，都加载到总任务中
            taskConfigs.add(taskConfig);
        }

        return taskConfigs;
    }

    private User userDaoToUser(UserDao userDao) {
        User user = new User();
        user.setUserName(userDao.getUserName());
        user.setBizLine(userDao.getBizLine());
        return user;
    }

    private SpiderComponentConfig componentConfigCreator(ComponentConfigDao componentConfigDao) throws ClassServiceException {
        SpiderComponentConfig componentConfig = new SpiderComponentConfig();

        componentConfig.setVersion(componentConfigDao.getVersion());

        // downloader
        String downloaderName = componentConfigDao.getDownloader();
        if (downloaderName != null) {
            Downloader downloader = (Downloader) classService.getComByName(downloaderName);
            componentConfig.setDownloader(downloader);
        } else {
            componentConfig.setDownloader(new HttpClientDownloader());
        }
        // pageModel
        String scheduleName = componentConfigDao.getSchedule();
        if (scheduleName != null) {
            Scheduler scheduler = (Scheduler) classService.getComByName(scheduleName);
            componentConfig.setScheduler(scheduler);
        }
        // pageProcessor
        String parserName = componentConfigDao.getPageProcessor();
        if (parserName != null && parserName.length() > 0) {
            PageProcessor processor = (PageProcessor) classService.getComByName(parserName);
            componentConfig.setPageProcessor(processor);
        }

        // 此处应该是一个List，里面是一个组件的名字
        String persistenceHandler = componentConfigDao.getPipelines();
        List<Pipeline> pipelines = new LinkedList<>();
        if (persistenceHandler != null) {
            String[] handlers = persistenceHandler.split(",");
            for (String name : handlers) {
                Pipeline pipeline = (Pipeline) classService.getComByName(name);
                pipelines.add(pipeline);
            }
        } else {
            pipelines.add(new ConsolePipeline());
        }
        componentConfig.setPipelines(pipelines);
        return componentConfig;
    }

    public void saveSpiderConfig(SpiderConfigDao configDao) {
        spiderConfigMapper.saveConfig(configDao);
    }

    public void saveComponentConfig(ComponentConfigDao componentConfigDao) {
        componentConfigMapper.saveConfig(componentConfigDao);
    }

    public void saveComponent(ComponentDao componentDao) {
        componentMapper.saveComponent(componentDao);
    }

}

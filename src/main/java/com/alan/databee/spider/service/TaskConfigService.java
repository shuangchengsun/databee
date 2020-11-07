package com.alan.databee.spider.service;

import com.alan.databee.dao.mapper.ComponentConfigMapper;
import com.alan.databee.dao.mapper.SpiderConfigMapper;
import com.alan.databee.dao.mapper.UserMapper;
import com.alan.databee.dao.model.ComponentConfigDao;
import com.alan.databee.dao.model.SpiderConfigDao;
import com.alan.databee.dao.model.UserDao;
import com.alan.databee.spider.downloader.Downloader;
import com.alan.databee.spider.downloader.HttpClientDownloader;
import com.alan.databee.spider.model.PageModel;
import com.alan.databee.spider.model.SpiderComponentConfig;
import com.alan.databee.spider.model.SpiderTaskConfig;
import com.alan.databee.spider.model.User;
import com.alan.databee.spider.pipeline.ConsolePipeline;
import com.alan.databee.spider.pipeline.Pipeline;
import com.alan.databee.spider.processor.PageProcessor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.ExecutionException;

/**
 * @ClassName TaskConfigService
 * @Author sunshuangcheng
 * @Date 2020/10/31 9:03 下午
 * @Version -V1.0
 */
@Service
public class TaskConfigService {

    @Autowired(required = false)
    private SpiderConfigMapper spiderConfigMapper;


    @Autowired(required = false)
    private ComponentConfigMapper componentConfigMapper;

    @Autowired(required = false)
    private UserMapper userMapper;

    @Autowired
    private ClassService classService;

//    private GroovyClassLoader loader = new GroovyClassLoader(Thread.currentThread().getContextClassLoader());

    public List<SpiderTaskConfig> getAllTask() {
        List<SpiderConfigDao> daily = spiderConfigMapper.getDaily();
        List<SpiderTaskConfig> taskConfigs = new LinkedList<>();
        try {
            for (SpiderConfigDao spiderConfigDao : daily) {
                SpiderTaskConfig taskConfig = new SpiderTaskConfig();

                int creator = spiderConfigDao.getCreator();
                UserDao userdao = userMapper.getById(creator);
                taskConfig.setCreator(userDaoToUser(userdao));

                taskConfig.setDepth(spiderConfigDao.getDepth());

                taskConfig.setExpire(spiderConfigDao.getExpire());

                taskConfig.setGmtCreate(spiderConfigDao.getGmtCreate());

                taskConfig.setGmtModify(spiderConfigDao.getGmtModify());

                int modifierId = spiderConfigDao.getModifier();
                UserDao modifierDao = userMapper.getById(modifierId);
                taskConfig.setModifier(userDaoToUser(modifierDao));

                taskConfig.setPriority(spiderConfigDao.getPriority());


                int actionConfigId = spiderConfigDao.getActionConfig();
                ComponentConfigDao componentConfigDao = componentConfigMapper.getById(actionConfigId);
                taskConfig.setSpiderComponentConfig(componentConfigCreator(componentConfigDao));

                taskConfig.setTaskName(spiderConfigDao.getTaskName());

                taskConfig.setTaskType(spiderConfigDao.getTaskType());

                taskConfig.setUrl(spiderConfigDao.getUrl());

                taskConfig.setThread(spiderConfigDao.getThread());

                taskConfigs.add(taskConfig);

            }
        } catch (IllegalAccessException | InstantiationException e) {
            e.printStackTrace();
            return null;
        }
        return taskConfigs;
    }

    private User userDaoToUser(UserDao userDao) {
        User user = new User();
        user.setUserName(userDao.getUserName());
        user.setBizLine(userDao.getBizLine());
        return user;
    }

    private SpiderComponentConfig componentConfigCreator(ComponentConfigDao componentConfigDao) throws IllegalAccessException, InstantiationException {
        SpiderComponentConfig componentConfig = new SpiderComponentConfig();

        componentConfig.setVersion(componentConfigDao.getVersion());
        // downloader
        String downloaderName = componentConfigDao.getDownloader();
        try {
            if (downloaderName != null) {
                Downloader downloader = (Downloader) classService.getComByName(downloaderName);
                componentConfig.setDownloader(downloader);
            } else {
                componentConfig.setDownloader(new HttpClientDownloader());
            }
            // pageModel
            String pageModelName = componentConfigDao.getPageModel();
            if (pageModelName != null) {
                PageModel page = (PageModel) classService.getComByName(pageModelName);
                componentConfig.setPageModel(page);
            }
            // pageProcessor
            String parserName = componentConfigDao.getParser();
            if (parserName != null && parserName.length() > 0) {
                PageProcessor processor = (PageProcessor) classService.getComByName(parserName);
                componentConfig.setPageProcessor(processor);
            }

            // 此处应该是一个List，里面是一个组件的名字
            String persistenceHandler = componentConfigDao.getPersistenceHandler();
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
        } catch (ExecutionException | IllegalAccessException | InstantiationException e) {
            e.printStackTrace();
        }
        return componentConfig;
    }

}

package com.alan.databee.spider.service;

import com.alan.databee.dao.mapper.ComponentConfigMapper;
import com.alan.databee.dao.mapper.ComponentMapper;
import com.alan.databee.dao.mapper.SpiderConfigMapper;
import com.alan.databee.dao.mapper.UserMapper;
import com.alan.databee.dao.model.ComponentConfigDao;
import com.alan.databee.dao.model.ComponentDao;
import com.alan.databee.dao.model.SpiderConfigDao;
import com.alan.databee.dao.model.UserDao;
import com.alan.databee.spider.model.AbstractPageModel;
import com.alan.databee.spider.model.SpiderComponentConfig;
import com.alan.databee.spider.model.SpiderTaskConfig;
import com.alan.databee.spider.model.User;
import com.alan.databee.spider.script.ScriptService;
import com.alibaba.fastjson.JSON;
import org.junit.Assert;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import us.codecraft.webmagic.downloader.AbstractDownloader;
import us.codecraft.webmagic.downloader.Downloader;
import us.codecraft.webmagic.downloader.HttpClientDownloader;
import us.codecraft.webmagic.pipeline.ConsolePipeline;
import us.codecraft.webmagic.pipeline.Pipeline;
import us.codecraft.webmagic.processor.PageProcessor;

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

    @Autowired(required = false)
    private SpiderConfigMapper spiderConfigMapper;

    @Autowired(required = false)
    private ComponentMapper componentMapper;

    @Autowired(required = false)
    private ComponentConfigMapper componentConfigMapper;

    @Autowired(required = false)
    private UserMapper userMapper;

    @Autowired
    ScriptService scriptService;
//    private GroovyClassLoader loader = new GroovyClassLoader(Thread.currentThread().getContextClassLoader());

    List<SpiderTaskConfig> getAllTask() {
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

    private SpiderComponentConfig componentConfigCreator(ComponentConfigDao componentConfigDao) throws IllegalAccessException, InstantiationException{
        SpiderComponentConfig componentConfig = new SpiderComponentConfig();

        componentConfig.setVersion(componentConfigDao.getVersion());
        // downloader
        String downloaderName = componentConfigDao.getDownloader();
        if (downloaderName != null) {
            ComponentDao componentDao = componentMapper.getByName(downloaderName);
            Class<?> downloaderClass = scriptService.genClass(downloaderName, componentDao.getContent());
            Downloader downloader = (Downloader) downloaderClass.newInstance();
            componentConfig.setDownloader(downloader);
        }else {
            componentConfig.setDownloader(new HttpClientDownloader());
        }

        // pageModel
        String pageModelName = componentConfigDao.getPageModel();
        if (pageModelName != null) {
//            Class<?> pageModelClass = loader.parseClass(pageModel);
            ComponentDao pageModelDao = componentMapper.getByName(pageModelName);
            Class<?> pageModelClass = scriptService.genClass(pageModelName, pageModelDao.getContent());
            AbstractPageModel page = (AbstractPageModel) pageModelClass.newInstance();
            componentConfig.setPageModel(page);
        }

        // pageProcessor
        String parserName = componentConfigDao.getParser();
        if (parserName != null && parserName.length()>0) {
//            Class<?> parserClass = loader.parseClass(parser);
            ComponentDao parserDao = componentMapper.getByName(parserName);
            Class<?> parserClass = scriptService.genClass(parserName, parserDao.getContent());
            PageProcessor processor = (PageProcessor) parserClass.newInstance();
            componentConfig.setPageProcessor(processor);
        }

        // 此处应该是一个List，里面是一个组件的名字
        String persistenceHandler = componentConfigDao.getPersistenceHandler();
        List<Pipeline> pipelines = new LinkedList<>();
        if(persistenceHandler != null) {
            String[] handlers = persistenceHandler.split(",");
            for (String name : handlers) {
                ComponentDao componentDao = componentMapper.getByName(name);
                Assert.assertNotNull(componentDao);
                String content = componentDao.getContent();
                Class<?> aClass = scriptService.genClass(name,content);
                Pipeline pipeline = (Pipeline) aClass.newInstance();
                pipelines.add(pipeline);
            }
        }else {
            pipelines.add(new ConsolePipeline());
        }
        componentConfig.setPipelines(pipelines);
        return componentConfig;
    }

}

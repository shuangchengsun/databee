package com.alan.databee.service;

import com.alan.databee.common.BeanUtil;
import com.alan.databee.common.ScriptUtil;
import com.alan.databee.common.cache.ComLoader;
import com.alan.databee.common.util.log.LoggerUtil;
import com.alan.databee.dao.mapper.ComponentMapper;
import com.alan.databee.spider.downloader.HttpClientDownloader;
import com.alan.databee.spider.downloader.SeleniumDownloader;
import com.alan.databee.spider.exception.ClassServiceException;
import com.alan.databee.spider.exception.ScriptException;
import com.alan.databee.spider.exception.SpiderErrorEnum;
import com.alan.databee.spider.pipeline.ConsolePipeline;
import com.alan.databee.spider.pipeline.LogPipeline;
import com.alan.databee.spider.scheduler.PriorityScheduler;
import com.alan.databee.spider.scheduler.QueueScheduler;
import com.alan.databee.spider.script.ScriptService;
import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Service;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;

/**
 * 该服务掌管着class服务，即其他的类通过此服务获取class文件，和class的实例
 */
@Service
public class ClassService implements InitializingBean {
    private static final Logger logger = LoggerFactory.getLogger("scriptAppender");

    private LoadingCache<String, Class<?>> classCache;
    private final Map<String, Object> commonComponent = new HashMap();
    private final Map<String, Class<?>> basic = new HashMap<>();

    @Autowired
    ComLoader comLoader;

    @Autowired(required = false)
    private ComponentMapper componentMapper;

    @Autowired
    private ScriptService scriptService;

    @Autowired
    private BeanUtil beanUtil;

    public ClassService() {
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        CacheLoader<String, Class<?>> loader = comLoader;
        classCache = CacheBuilder.newBuilder().maximumSize(64)
                .expireAfterAccess(4, TimeUnit.HOURS)
                .build(loader);
        setComponent();
    }

    public Object genCom(String script) throws ScriptException, IllegalAccessException, InstantiationException {
        if (commonComponent.containsKey(script)) {
            return commonComponent.get(script);
        }
        Class<?> aClass = null;
        if (basic.containsKey(script)) {
            aClass = basic.get(script);
        } else {
            aClass = scriptService.genClass(ScriptUtil.getComName(script), script);
        }
        Object obj = aClass.newInstance();
        try {
            Method method = aClass.getMethod("setClassService", ClassService.class);
            method.invoke(obj, this);
        } catch (InvocationTargetException | NoSuchMethodException exception) {
            return obj;
        }
        return obj;

    }

    public Object getComByName(String name)
            throws ClassServiceException {
        Object obj = null;
        LoggerUtil.info(logger, "gen class: " + name);
        try {
            if (commonComponent.containsKey(name)) {
                return commonComponent.get(name);
            }
            Class<?> aClass = null;
            if (basic.containsKey(name)) {
                aClass = basic.get(name);
            } else {
                aClass = classCache.get(name);
            }
            obj = aClass.newInstance();
            // 获取全部的属性
            Field[] fields = aClass.getFields();
            for(Field field : fields){
                Autowired annotation = field.getAnnotation(Autowired.class);
                if(annotation != null){
                    String fieldName = field.getName();
                    Field declaredField = obj.getClass().getDeclaredField(fieldName);
                    declaredField.setAccessible(true);
                    Class<?> type = declaredField.getType();
                    ApplicationContext applicationContext = beanUtil.getApplicationContext();
                    Object bean = applicationContext.getBean(type);
                    declaredField.set(obj,bean);
                }
            }
            Method method = aClass.getMethod("setClassService", ClassService.class);
            method.invoke(obj, this);
        } catch (RuntimeException | ExecutionException exception) {
            // 此处的异常标志着获取class时发生的错误，多半情况是类编译错误，包括类文件获取失败，和类文件编译异常
            LoggerUtil.error(logger, "在获取class时发生错误（编译错误，或者多线程操作错误）" + name, exception);

            throw new ClassServiceException(SpiderErrorEnum.Script_Compiler_Error, exception);
        } catch (InstantiationException | IllegalAccessException exception) {
            // 此处的异常是类实例化过程中发生的错误，一般是实例化失败
            LoggerUtil.error(logger, "实例化错误, name: " + name, exception);
            throw new ClassServiceException(SpiderErrorEnum.Class_Instance_Error, exception);
        } catch (NoSuchMethodException exception) {
            // 此处说明该类不需要额外的初始化操作，可以直接返回
            return obj;
        } catch (InvocationTargetException exception) {
            // 此处表明类的初始化方法执行错误。
            LoggerUtil.error(logger, "类初始化错误，name: " + name, exception);
            throw new ClassServiceException(SpiderErrorEnum.Class_Init_Error, exception);
        } catch (NoSuchFieldException e) {
            e.printStackTrace();
        }
        // 经过分析此处不可能是null。
        return obj;
    }

    public boolean containCom(String com) {
        return commonComponent.containsKey(com);
    }

    public boolean checkScript(String script) {
        if (script == null || script.length() == 0) {
            return true;
        }
        if (!containCom(script)) {
            try {
                String name = ScriptUtil.getComName(script);
                scriptService.genClass(name, script);
                return true;
            } catch (ScriptException exception) {
                return false;
            }
        } else {
            return true;
        }
    }

    private void setComponent() {
        basic.put(QueueScheduler.class.getName(), QueueScheduler.class);
        basic.put(PriorityScheduler.class.getName(), PriorityScheduler.class);
        commonComponent.put(HttpClientDownloader.class.getName(), new HttpClientDownloader());
        commonComponent.put(SeleniumDownloader.class.getName(), new SeleniumDownloader());
        commonComponent.put(LogPipeline.class.getName(), new LogPipeline());
        commonComponent.put(ConsolePipeline.class.getName(), new ConsolePipeline());

    }
}

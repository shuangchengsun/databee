package com.alan.databee.spider.service;

import com.alan.databee.common.cache.ComLoader;
import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;
import org.springframework.stereotype.Service;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;

/**
 *
 */
@Service
public class ClassService {

    private LoadingCache<String, Class<?>> classCache;

    public ClassService(){
        CacheLoader<String,Class<?>> loader = new ComLoader();
        classCache = CacheBuilder.newBuilder().maximumSize(64)
                .expireAfterAccess(4,TimeUnit.HOURS)
                .build(loader);
    }

    public Object getComByName(String name)
            throws ExecutionException, IllegalAccessException, InstantiationException {
        Class<?> aClass = classCache.get(name);

        // 完成一些基本参数的初始化（此处主要是在组件中注入自己）
        Object obj = aClass.newInstance();
        try {
            Method method = aClass.getMethod("setClassService", ClassService.class);
            method.invoke(obj,this);
        }catch (NoSuchMethodException | InvocationTargetException exception){
            return obj;
        }
        return obj;
    }
}

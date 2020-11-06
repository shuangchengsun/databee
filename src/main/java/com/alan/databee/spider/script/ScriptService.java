package com.alan.databee.spider.script;

import com.alan.databee.spider.exception.ScriptException;
import com.alan.databee.spider.exception.SpiderErrorEnum;
import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;
import org.springframework.stereotype.Service;

import javax.tools.*;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

/**
 * @ClassName MemCompiler
 * @Author sunshuangcheng
 * @Date 2020/11/1 11:22 上午
 * @Version -V1.0
 */
@Service
public class ScriptService {

    // 缓存编译的class。
    private Cache<String,Class<?>> classCache;

    public ScriptService() {
        classCache = CacheBuilder.newBuilder()
                .expireAfterAccess(4, TimeUnit.HOURS)
                .build();
    }

    public Class<?> genClass(String name, String script) {
        Class<?> aClass = classCache.getIfPresent(name);
        if(aClass != null){
            return aClass;
        }
        String fileName = name+".java";
        Map<String, byte[]> classBytes = new HashMap<>();
        JavaCompiler compiler = ToolProvider.getSystemJavaCompiler();
        StandardJavaFileManager stdManager = compiler.getStandardFileManager(null, null, null);
        MemoryJavaFileManager manager = new MemoryJavaFileManager(stdManager);
        JavaFileObject javaFileObject = manager.makeStringSource(fileName, script);
        JavaCompiler.CompilationTask task = compiler.getTask(null, manager, null, null, null, Collections.singletonList(javaFileObject));
        if (task.call()) {
            classBytes = manager.getClassBytes();
        }
        try {
            MemClassLoader classLoader = new MemClassLoader(classBytes);
            classCache.putAll(classLoader.getAllClass());
            aClass = classCache.getIfPresent(name);
            if(aClass == null){
                throw new ClassNotFoundException(name + " class not found");
            }
            return aClass;
        }catch (ClassNotFoundException e) {
           throw new ScriptException(SpiderErrorEnum.Script_Compiler_Error,e);
        }
    }
}


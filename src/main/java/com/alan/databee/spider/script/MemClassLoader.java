package com.alan.databee.spider.script;

import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import java.net.URL;
import java.net.URLClassLoader;
import java.util.HashMap;
import java.util.Map;

/**
 * @ClassName MemClassLoader
 * @Author sunshuangcheng
 * @Date 2020/11/1 11:18 上午
 * @Version -V1.0
 */
public class MemClassLoader extends URLClassLoader {

    private Map<String,byte[]> classBytes = new HashMap<>();


    public MemClassLoader(Map<String, byte[]> classBytes) {
        super(new URL[0], MemClassLoader.class.getClassLoader());
        this.classBytes = classBytes;
    }

    @Override
    protected Class<?> findClass(String name) throws ClassNotFoundException {
        byte[] buf = classBytes.get(name);
        if (buf == null) {
            return super.findClass(name);
        }
        classBytes.remove(name);
        return defineClass(name, buf, 0, buf.length);
    }

    protected  Map<String, Class<?>> getAllClass() throws ClassNotFoundException {
        Map<String, Class<?>> classMap = new HashMap<>();
        for(String name:classBytes.keySet()){
            Class<?> aClass = findClass(name);
            classMap.put(name,aClass);
        }
        return classMap;
    }
}

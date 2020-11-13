package com.alan.databee.spider.script;

import com.alan.databee.spider.exception.ScriptException;
import com.alan.databee.spider.exception.SpiderErrorEnum;
import org.springframework.stereotype.Service;

import javax.tools.*;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

/**
 * @ClassName MemCompiler
 * @Author sunshuangcheng
 * @Date 2020/11/1 11:22 上午
 * @Version -V1.0
 */
@Service
public class ScriptService {

    public ScriptService() {
    }

    public Class<?> genClass(String name, String script) throws ScriptException {
        String fileName = name + ".java";
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
            return classLoader.findClass(name);
        } catch (ClassNotFoundException e) {
            throw new ScriptException(SpiderErrorEnum.Script_Compiler_Error, e);
        }
    }

    public Map<String, Class<?>> genAllClass(String name, String script) throws ClassNotFoundException {
        String fileName = name + ".java";
        Map<String, byte[]> classBytes = new HashMap<>();
        JavaCompiler compiler = ToolProvider.getSystemJavaCompiler();
        StandardJavaFileManager stdManager = compiler.getStandardFileManager(null, null, null);
        MemoryJavaFileManager manager = new MemoryJavaFileManager(stdManager);
        JavaFileObject javaFileObject = manager.makeStringSource(fileName, script);
        JavaCompiler.CompilationTask task = compiler.getTask(null, manager, null, null, null, Collections.singletonList(javaFileObject));
        if (task.call()) {
            classBytes = manager.getClassBytes();
        }
        MemClassLoader classLoader = new MemClassLoader(classBytes);
        return classLoader.getAllClass();
    }
}


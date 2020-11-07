package com.alan.databee.common.cache;

import com.alan.databee.dao.mapper.ComponentMapper;
import com.alan.databee.dao.model.ComponentDao;
import com.alan.databee.spider.exception.SpiderErrorEnum;
import com.alan.databee.spider.exception.SpiderTaskException;
import com.alan.databee.spider.script.ScriptService;
import com.google.common.cache.CacheLoader;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Map;

public class ComLoader extends CacheLoader<String,Class<?>> {

    @Autowired(required = false)
    private ComponentMapper componentMapper;

    @Autowired
    private ScriptService scriptService;

    @Override
    public Class<?> load(String name) throws Exception {
        ComponentDao componentDao = componentMapper.getByName(name);
        paramCheck(componentDao);
        String content = componentDao.getContent();
        Map<String, Class<?>> classMap = scriptService.genClass(name, content);
        return classMap.get(name);
    }

    private void paramCheck(ComponentDao componentDao){
        if(componentDao == null
            || componentDao.getContent()==null
            || componentDao.getContent().isEmpty()
            || componentDao.getContent().length()==0){
            throw new SpiderTaskException(SpiderErrorEnum.Component_Not_Fount);
        }
    }
}

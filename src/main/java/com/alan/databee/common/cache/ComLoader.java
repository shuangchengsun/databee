package com.alan.databee.common.cache;

import com.alan.databee.common.util.log.LoggerUtil;
import com.alan.databee.dao.mapper.ComponentMapper;
import com.alan.databee.dao.model.ComponentDao;
import com.alan.databee.spider.exception.DaoException;
import com.alan.databee.spider.exception.ScriptException;
import com.alan.databee.spider.exception.SpiderErrorEnum;
import com.alan.databee.spider.script.ScriptService;
import com.google.common.cache.CacheLoader;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Map;

public class ComLoader extends CacheLoader<String, Class<?>> {

    private static final Logger LOGGER = LoggerFactory.getLogger("scriptLogger");

    @Autowired(required = false)
    private ComponentMapper componentMapper;

    @Autowired
    private ScriptService scriptService;

    @Override
    public Class<?> load(String name) {
        ComponentDao componentDao = componentMapper.getByName(name);
        try {
            paramCheck(componentDao);
        } catch (DaoException e) {
            LoggerUtil.error(LOGGER, "从数据库获取组件文件错误，未找到或文件不全", name, e);
            return null;
        }
        String content = componentDao.getContent();
        Map<String, Class<?>> classMap = null;
        try {
            return scriptService.genClass(name,content);
        } catch (ScriptException e) {
            LoggerUtil.error(LOGGER, "组件编译错误", name, e.getStackTrace());
            return null;
        }
    }

    private void paramCheck(ComponentDao componentDao) throws DaoException {
        if (componentDao == null) {
            throw new DaoException(SpiderErrorEnum.Component_Not_Fount);
        }
        if (componentDao.getContent() == null
                || componentDao.getContent().isEmpty()
                || componentDao.getContent().length() == 0) {
            throw new DaoException(SpiderErrorEnum.Component_Is_Empty);
        }
    }
}

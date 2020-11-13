package com.alan.databee.dao.mapper;

import com.alan.databee.dao.model.SpiderConfigDao;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;

/**
 * @ClassName SpiderConfigMapper
 * @Author sunshuangcheng
 * @Date 2020/10/31 9:04 下午
 * @Version -V1.0
 */
@Mapper
public interface SpiderConfigMapper {

    @Select(value = "SELECT * FROM spider_config WHERE task_type='daily'")
    List<SpiderConfigDao> getDaily();

    @Insert("INSERT INTO spider_config (task_type,task_name,creator,gmt_create,gmt_modify,modifier,component_config,depth" +
            "expire_time,url,priority,thread) VALUES (#{dao.taskType}, #{dao.taskName},#{dao.creator},#{dao.gmtCreate},#{dao.gmtModify}," +
            "#{dao.modifier}#{dao.componentConfig},#{dao.depth},#{dao.expireTime},#{dao.url},#{dao.priority},#{dao.thread})")
    void saveConfig(SpiderConfigDao dao);
}

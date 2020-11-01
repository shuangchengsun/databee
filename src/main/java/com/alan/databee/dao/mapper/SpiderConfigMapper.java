package com.alan.databee.dao.mapper;

import com.alan.databee.dao.model.SpiderConfigDao;
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
}

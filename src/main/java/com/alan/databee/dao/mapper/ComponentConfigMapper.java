package com.alan.databee.dao.mapper;

import com.alan.databee.dao.model.ComponentConfigDao;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

/**
 * @ClassName ComponentConfigMapper
 * @Author sunshuangcheng
 * @Date 2020/10/31 9:11 下午
 * @Version -V1.0
 */
@Mapper
public interface ComponentConfigMapper {

    @Select("SELECT * FROM component_config WHERE id=#{id}")
    ComponentConfigDao getById(int id);
}

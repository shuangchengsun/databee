package com.alan.databee.dao.mapper;

import com.alan.databee.dao.model.ComponentDao;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

/**
 * @ClassName ComponentMapper
 * @Author sunshuangcheng
 * @Date 2020/10/31 9:13 下午
 * @Version -V1.0
 */
@Mapper
public interface ComponentMapper {

    @Select("SELECT * FROM component WHERE name=#{name}")
    ComponentDao getByName(String name);

    @Insert("INSERT INTO component (name,content) VALUES (#{componentDao.name}, #{componentDao.content})")
    void saveComponent(ComponentDao componentDao);
}

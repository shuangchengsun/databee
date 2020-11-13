package com.alan.databee.dao.mapper;

import com.alan.databee.dao.model.ComponentConfigDao;
import org.apache.ibatis.annotations.Insert;
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

    @Select("SELECT * FROM component_config WHERE name=#{name}")
    ComponentConfigDao getByName(String name);

    @Insert("INSERT INTO component_config (version, processor,downloader,pipeline, `name`,scheduler) " +
            "VALUES (#{dao.version}, #{dao.pageProcessor}, #{dao.downloader}, #{dao.pipelines}, #{dao.name}, #{dao.schedule})")
    void saveConfig(ComponentConfigDao dao);
}

package com.alan.databee.dao.mapper;

import com.alan.databee.dao.model.UserDao;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

/**
 * @ClassName UserMapper
 * @Author sunshuangcheng
 * @Date 2020/10/31 9:23 下午
 * @Version -V1.0
 */
@Mapper
public interface UserMapper {

    @Select("SELECT id,user_name,biz_line FROM user WHERE id=#{id}")
    UserDao getById(int id);
}

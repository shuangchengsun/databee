package com.alan.databee.dao.mapper;

import com.alan.databee.dao.model.UserDao;
import com.alan.databee.model.User;
import org.apache.ibatis.annotations.Insert;
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

    @Select("SELECT id,user_name,biz_line FROM user WHERE user_name=#{name}")
    UserDao getByName(String name);

    @Select(value = "SELECT id,user_name,biz_line FROM user WHERE user_name=#{name} AND password=#{password}")
    UserDao getByNameAndPassword(String name, String password);

    @Insert(value = "INSERT INTO user (user_name, biz_line, password) VALUES (#{userName}, #{bizLine}, #{password})")
    void insertUser(UserDao user);
}

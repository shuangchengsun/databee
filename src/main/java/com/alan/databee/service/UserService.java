package com.alan.databee.service;

import com.alan.databee.dao.mapper.UserMapper;
import com.alan.databee.dao.model.UserDao;
import com.alan.databee.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserService {
    @Autowired(required = false)
    UserMapper userMapper;
    public String checkUser(String userName, String password){
        UserDao userDao = userMapper.getByNameAndPassword(userName, password);
        if(userDao == null){
            return null;
        }
        return userName;
    }

    public String signIn(User user){
        userMapper.insertUser(user);
        return user.getUserName();
    }
}

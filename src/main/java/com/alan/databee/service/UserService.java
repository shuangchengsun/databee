package com.alan.databee.service;

import com.alan.databee.common.token.TokenUtil;
import com.alan.databee.dao.mapper.UserMapper;
import com.alan.databee.dao.model.UserDao;
import com.alan.databee.model.User;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserService {
    @Autowired(required = false)
    UserMapper userMapper;

    @Autowired
    TokenUtil tokenUtil;


    public String checkUser(String userName, String password){
        UserDao userDao = userMapper.getByNameAndPassword(userName, password);
        if(userDao == null){
            return null;
        }
        return tokenUtil.genToken(userName);
    }

    public String signIn(UserDao user){
        String ans = user.getUserName();
        try {
            userMapper.insertUser(user);
        }catch (Exception exception){
            ans = null;
        }
        return tokenUtil.genToken(ans);
    }

    public User searchUserByName( String userName){
        UserDao userDao = userMapper.getByName(userName);
        User user = new User();
        BeanUtils.copyProperties(userDao,user);
        return user;
    }
}

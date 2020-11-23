package com.alan.databee.controller;

import com.alan.databee.dao.model.UserDao;
import com.alan.databee.model.RequestData;
import com.alan.databee.model.ResponseData;
import com.alan.databee.model.User;
import com.alan.databee.service.UserService;
import com.alibaba.fastjson.JSON;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;

@Controller
public class UserController {
    @Autowired
    UserService userService;

    @PostMapping("/user/")
    public ResponseData login(@RequestBody RequestData requestData, HttpServletResponse response){
        ResponseData responseData = new ResponseData();
        int code = requestData.getBusCode();
        responseData.setBusCode(code);
        responseData.setSuccess(false);
        responseData.setMsg("数据处理失败");
        switch (code){
            case 0x01:{
                // 登陆
                String msg = requestData.getMsg();
                User parse = JSON.parseObject(msg, User.class);
                String token = userService.checkUser(parse.getUserName(), parse.getPassword());
                if(token !=null){
                    responseData.setBusCode(0x01);
                    responseData.setSuccess(true);
                    responseData.setMsg("登陆成功");
                    Cookie cookie = new Cookie("token", token);
                    response.addCookie(cookie);
                }
                break;
            }
            case 0x02:{
                // 注册
                String msg = requestData.getMsg();
                User user = JSON.parseObject(msg,User.class);
                String token = userService.signIn(user);
                if(token !=null){
                    responseData.setBusCode(0x02);
                    responseData.setSuccess(true);
                    responseData.setMsg("注册成功");
                    Cookie cookie = new Cookie("token", token);
                    response.addCookie(cookie);
                }
                break;
            }
            default:{
                responseData.setSuccess(false);
                responseData.setMsg("不支持的业务");
                break;
            }
        }
        return responseData;
    }
}

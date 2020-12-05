package com.alan.databee.controller;

import com.alan.databee.common.util.log.LoggerUtil;
import com.alan.databee.dao.model.UserDao;
import com.alan.databee.model.RequestData;
import com.alan.databee.model.ResponseData;
import com.alan.databee.service.UserService;
import com.alibaba.fastjson.JSON;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;

@Controller
@ResponseBody
public class UserController {

    private static final Logger LOGGER = LoggerFactory.getLogger("debugLogger");

    @Autowired
    UserService userService;

    @PostMapping("/user")
    public ResponseData login(@RequestBody final RequestData requestData, HttpServletResponse response) {
        ResponseData responseData = new ResponseData();
        int code = requestData.getBusCode();
        responseData.setBusCode(code);
        responseData.setSuccess(false);
        responseData.setMsg("数据处理失败");
        switch (code) {
            case 0x01: {
                // 登陆
                LoggerUtil.debug(LOGGER, "收到登陆请求");
                String msg = requestData.getMsg();
                UserDao parse = JSON.parseObject(msg, UserDao.class);
                String token = userService.checkUser(parse.getUserName(), parse.getPassword());
                if (token != null) {
                    responseData.setBusCode(0x01);
                    responseData.setSuccess(true);
                    responseData.setMsg("登陆成功");
                    Cookie cookie = new Cookie("token", token);
                    response.addCookie(cookie);
                } else {
                    responseData.setMsg("账号或密码错误");
                }
                LoggerUtil.debug(LOGGER, responseData);
                break;
            }
            case 0x02: {
                // 注册
                String msg = requestData.getMsg();
                UserDao user = JSON.parseObject(msg, UserDao.class);
                String token = userService.signIn(user);
                if (token != null) {
                    responseData.setBusCode(0x02);
                    responseData.setSuccess(true);
                    responseData.setMsg("注册成功");
                    Cookie cookie = new Cookie("token", token);
                    response.addCookie(cookie);
                    LoggerUtil.debug(LOGGER, "注册成功");
                } else {
                    responseData.setMsg("注册失败，用户名重复");
                    LoggerUtil.debug(LOGGER, "注册失败，用户名重复");
                }
                break;
            }
            default: {
                responseData.setSuccess(false);
                responseData.setMsg("不支持的业务");
                LoggerUtil.error(LOGGER,"不支持的业务");
                break;
            }
        }
        return responseData;
    }

    @GetMapping("/user")
    public ResponseData getProcess(){
        ResponseData responseData = new ResponseData();
        responseData.setSuccess(false);
        responseData.setMsg("不支持GET操作");
        LoggerUtil.info(LOGGER,"收到了不支持的Get请求");
        return responseData;
    }
}

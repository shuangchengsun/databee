package com.alan.databee.controller;

import com.alan.databee.builder.DebugResultBuilder;
import com.alan.databee.common.token.TokenUtil;
import com.alan.databee.common.util.log.LoggerUtil;
import com.alan.databee.model.BusyReqModel;
import com.alan.databee.model.DebugResult;
import com.alan.databee.model.ResultEnum;
import com.alan.databee.service.SpiderManager;
import com.alan.databee.service.TaskService;
import com.alan.databee.model.User;
import com.alan.databee.service.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;

@Controller
public class TaskController {
    private static final Logger LOGGER = LoggerFactory.getLogger("controllerLogger");

    @Autowired
    private SpiderManager spiderManager;

    @Autowired
    private TaskService taskService;

    @Autowired
    private UserService userService;

    @Autowired
    private TokenUtil tokenUtil;

    @PostMapping({"/task"})
    @ResponseBody
    public DebugResult taskTest(@RequestBody final BusyReqModel reqModel, HttpServletRequest request) {
        DebugResult result = null;
        String userName = null;
        int busyCode = reqModel.getBusyCode();
        Cookie[] cookies = request.getCookies();
        for (Cookie cookie : cookies) {
            if (cookie.getName().equals("token")) {
                String value = cookie.getValue();
                userName = tokenUtil.parseToken(value);
            }
        }
//        if (userName == null) {
//            LoggerUtil.error(LOGGER, "user is null,用户未登录，或者cookie过期");
//            result = DebugResultBuilder.buildError(ResultEnum.User_Need_Login);
//            return result;
//        }
//        User user = userService.searchUserByName(userName);
        User user = null;

        switch (busyCode) {
            // 默认提交任务
            case 0: {
                result = taskService.submitTask(reqModel, user);
                break;
            }
            case 1: {
                result = spiderManager.runDebugTask(reqModel);
                break;
            }
            default: {
                result = DebugResultBuilder.buildError(ResultEnum.Undefined_Option);
                break;
            }
        }
        return result;
    }

}

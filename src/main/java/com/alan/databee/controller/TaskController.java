package com.alan.databee.controller;

import com.alan.databee.model.BusyReqModel;
import com.alan.databee.model.DebugResult;
import com.alan.databee.service.SpiderManager;
import com.alan.databee.service.TaskService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
public class TaskController {

    @Autowired
    private SpiderManager spiderManager;

    @Autowired
    private TaskService taskService;

    @PostMapping({"/task"})
    @ResponseBody
    public DebugResult taskTest(@RequestBody BusyReqModel reqModel){
        DebugResult result = new DebugResult();
        int busyCode = reqModel.getBusyCode();
        switch (busyCode){
            // 默认提交任务
            case 0:
        }
        return result;
    }

}

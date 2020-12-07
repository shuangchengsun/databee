package com.alan.databee.spider.pipeline;

import com.alan.databee.common.util.log.LoggerUtil;
import com.alan.databee.spider.Task;
import com.alan.databee.spider.model.ResultItems;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Map;

public class LogPipeline implements Pipeline {
    private static final Logger LOGGER = LoggerFactory.getLogger("LogPipelineLogger");

    @Override
    public void process(ResultItems resultItems, Task task) {
        StringBuilder builder = new StringBuilder();
        for (Map.Entry<String, Object> entry : resultItems.getAll().entrySet()) {
            String value = entry.getValue().toString();
            if(value.length()<=0){
                LoggerUtil.error(LOGGER,"抓取数据长度为0",resultItems.getRequest().getUrl());
                return;
            }
            builder.append(entry.getKey()).append(":\t").append(entry.getValue()).append(",   ");
        }
        String msg = builder.toString();
        if (msg.length() > 0) {
            LoggerUtil.info(LOGGER, msg);
        }
    }
}

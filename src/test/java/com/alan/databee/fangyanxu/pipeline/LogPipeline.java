package com.alan.databee.fangyanxu.pipeline;

import com.alan.databee.common.util.log.LoggerUtil;
import com.alan.databee.spider.Task;
import com.alan.databee.spider.model.ResultItems;
import com.alan.databee.spider.pipeline.Pipeline;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Map;

public class LogPipeline implements Pipeline {
    private static final Logger LOGGER = LoggerFactory.getLogger("LogePipelineLogger");
    @Override
    public void process(ResultItems resultItems, Task task) {
        StringBuilder builder = new StringBuilder();
        for (Map.Entry<String, Object> entry : resultItems.getAll().entrySet()) {
            builder.append(entry.getKey()).append(":\t").append(entry.getValue());
        }
        LoggerUtil.info(LOGGER,builder.toString());
    }
}

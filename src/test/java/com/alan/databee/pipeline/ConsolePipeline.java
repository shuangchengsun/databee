package com.alan.databee.pipeline;

import com.alan.databee.spider.Task;
import com.alan.databee.spider.model.ResultItems;
import com.alan.databee.spider.pipeline.Pipeline;

public class ConsolePipeline implements Pipeline {
    @Override
    public void process(ResultItems resultItems, Task task) {
        System.out.println(resultItems.getAll().toString());
    }
}

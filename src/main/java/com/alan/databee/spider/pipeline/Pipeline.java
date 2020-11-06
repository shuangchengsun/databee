package com.alan.databee.spider.pipeline;


import com.alan.databee.spider.model.ResultItems;
import com.alan.databee.spider.Task;

public interface Pipeline {
    /**
     * Process extracted results.
     *
     * @param resultItems resultItems
     * @param task task
     */
    public void process(ResultItems resultItems, Task task);
}

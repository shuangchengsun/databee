package com.alan.databee.spider.pipeline;

import com.alan.databee.spider.Task;
import com.alan.databee.spider.model.ResultItems;

import java.util.LinkedList;
import java.util.List;
import java.util.Map;

public class EchoPipeline implements Pipeline{
    List<Map<String, Object>> items = new LinkedList<>();

    @Override
    public void process(ResultItems resultItems, Task task) {
        Map<String, Object> all = resultItems.getAll();
        items.add(all);
    }

    public List<Map<String, Object>> getItems() {
        return items;
    }
}

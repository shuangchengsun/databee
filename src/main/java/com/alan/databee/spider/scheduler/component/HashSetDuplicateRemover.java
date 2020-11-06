package com.alan.databee.spider.scheduler.component;



import com.alan.databee.spider.Task;
import com.alan.databee.spider.model.Request;

import java.util.Collections;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

public class HashSetDuplicateRemover  implements DuplicateRemover{
    private Set<String> urls = Collections.newSetFromMap(new ConcurrentHashMap<String, Boolean>());

    @Override
    public boolean isDuplicate(Request request, Task task) {
        return !urls.add(getUrl(request));
    }

    protected String getUrl(Request request) {
        return request.getUrl();
    }

    @Override
    public void resetDuplicateCheck(Task task) {
        urls.clear();
    }

    @Override
    public int getTotalRequestsCount(Task task) {
        return urls.size();
    }
}

package com.alan.databee.fangyanxu.pipeline;

import com.alan.databee.common.util.log.LoggerUtil;
import com.alan.databee.spider.Task;
import com.alan.databee.spider.model.ResultItems;
import com.alan.databee.spider.pipeline.Pipeline;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class FxyPipeline implements Pipeline {
    private static final Logger LOGGER = LoggerFactory.getLogger("FxyLogger");

    @Override
    public void process(ResultItems resultItems, Task task) {

        Object content = resultItems.get("content");
        if (content != null) {
            String url = resultItems.get("url").toString();
            String title = resultItems.get("title").toString();
            LoggerUtil.info(LOGGER, "url:   " + url, "title:   " + title);
            System.out.println("url:   " + url+ ",   title:   " + title);
        } else {
            StringBuilder builder = new StringBuilder();
            for (Map.Entry<String, Object> entry : resultItems.getAll().entrySet()) {
                builder.append(entry.getKey()).append(":\t").append(entry.getValue());
            }
            String s = builder.toString();
            Matcher urlMatcher = Pattern.compile("[a-zA-z]+://[^\\s]*").matcher(s);
            Matcher titleMatcher = Pattern.compile("title:(.*?)content").matcher(s);
            while(urlMatcher.find() && titleMatcher.find()){
                String url = urlMatcher.group();
                String title = titleMatcher.group(1);
                LoggerUtil.info(LOGGER, "url:   " + url, "title:   " + title);
                System.out.println("url:   " + url+ ",   title:   " + title);
            }
        }

    }
}

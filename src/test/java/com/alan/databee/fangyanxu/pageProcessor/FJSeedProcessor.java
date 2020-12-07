package com.alan.databee.fangyanxu.pageProcessor;

import com.alan.databee.common.util.log.LoggerUtil;
import com.alan.databee.spider.Site;
import com.alan.databee.spider.model.Request;
import com.alan.databee.spider.page.Page;
import com.alan.databee.spider.processor.PageProcessor;
import com.alan.databee.spider.selector.Json;
import com.jayway.jsonpath.PathNotFoundException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

public class FJSeedProcessor implements PageProcessor {
    private static final Logger LOGGER = LoggerFactory.getLogger("PageProcessorLogger");

    @Override
    public void process(Page page, Site site) {
        Json json = page.getJson();
        List<String> all = json.jsonPath("$.docs[*]").all();
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
        boolean flag = true;
        int index = 0;
        try {
            Date current = simpleDateFormat.parse(simpleDateFormat.format(new Date()));
            for (String a : all) {
                try {
                    Json node = new Json(a);
                    String title = node.jsonPath("$.title").get();
                    String timeString = node.jsonPath("$.time").get();
                    String href = node.jsonPath("$.url").get();
                    String content = node.jsonPath("$.content").get();
                    Date time = simpleDateFormat.parse(timeString);
                    if (time.before(current)) {
                        flag = false;
                    } else {
                        String m = "url"+":\t"+href
                                +",   title"+":\t"+title
                                +",   content"+":\t"+content.replaceAll("<br>","");
                        page.putField(String.valueOf(index),m);
                        index++;
//                        System.out.println("time: " + timeString + ", href: " + href + ", title: " + title);
                    }
                } catch (PathNotFoundException e) {
                    LoggerUtil.error(LOGGER,"内容解析错误",a,page.getUrl());
                }

            }
            if (flag) {
                Request request = page.getRequest();
                String url = request.getUrl();
                int length = url.length();
                int point = length - 1;
                for (; point >= 0; point--) {
                    if (url.charAt(point) == '=') {
                        break;
                    }
                }
                int param = Integer.parseInt(url.substring(point + 1, length));
                String newUrl = url.substring(0, point + 1) + (param + 1);
                request.setUrl(newUrl);
                page.addTargetRequest(request);
            }
            LoggerUtil.info(LOGGER,"页面解析完成",page.getUrl());
        } catch (ParseException e) {
            LoggerUtil.warn(LOGGER,"时间解析错误",page.getUrl());
        }
    }

    @Override
    public Site getSite() {
        return null;
    }

    @Override
    public void setSite() {

    }
}

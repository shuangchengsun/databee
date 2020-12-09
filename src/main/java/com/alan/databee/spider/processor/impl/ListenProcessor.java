package com.alan.databee.spider.processor.impl;

import com.alan.databee.spider.Site;
import com.alan.databee.spider.model.Request;
import com.alan.databee.spider.page.Page;
import com.alan.databee.spider.processor.PageProcessor;

public class ListenProcessor implements PageProcessor {

    @Override
    public void process(Page page, Site site) {
        Request request = page.getRequest();
        String url = request.getUrl();
        page.putField("pageURL", url);
    }

    @Override
    public Site getSite() {
        return null;
    }

    @Override
    public void setSite() {

    }
}

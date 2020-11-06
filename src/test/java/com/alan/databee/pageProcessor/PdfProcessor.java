package com.alan.databee.pageProcessor;

import com.alan.databee.spider.Site;
import com.alan.databee.spider.page.Page;
import com.alan.databee.spider.processor.PageProcessor;

public class PdfProcessor implements PageProcessor {

    @Override
    public void process(Page page, Site site) {
        String rawText = page.getRawText();
    }

    @Override
    public Site getSite() {
        return null;
    }

    @Override
    public void setSite() {

    }
}

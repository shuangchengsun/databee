package com.alan.databee.fangyanxu.pageProcessor;

import com.alan.databee.spider.Site;
import com.alan.databee.spider.page.Page;
import com.alan.databee.spider.processor.PageProcessor;
import com.alan.databee.spider.selector.Html;
import com.alan.databee.spider.selector.Selectable;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class SXContentProcessor implements PageProcessor {

    @Override
    public void process(Page page, Site site) {
        Html html = page.getHtml();
        Selectable basic = html.xpath("//div[@class='common-detail-page-printer']");
        String regex = "[0-9]*[\\u4e00-\\u9fa5]";
        Pattern compile = Pattern.compile(regex);
        String s = basic.xpath("//div[@class='detail-article-title oflow-hd']").get();
        Matcher titleMatcher = compile.matcher(s);

        StringBuilder builder = new StringBuilder();
        while(titleMatcher.find()){
            builder.append(titleMatcher.group());
        }
        String title = builder.toString();
        builder.delete(0,title.length());
        String s1 = basic.xpath("//div[@class='TRS_Editor']").get();
        Matcher matcher = compile.matcher(s1);
        while (matcher.find()) {
            builder.append(matcher.group());
        }
        String content = builder.toString();
        page.putField("url", page.getUrl());
        page.putField("title", title);
        page.putField("content", content);
    }

    @Override
    public Site getSite() {
        return null;
    }

    @Override
    public void setSite() {

    }
}

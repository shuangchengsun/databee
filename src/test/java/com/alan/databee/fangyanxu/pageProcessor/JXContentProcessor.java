package com.alan.databee.fangyanxu.pageProcessor;

import com.alan.databee.spider.Site;
import com.alan.databee.spider.page.Page;
import com.alan.databee.spider.processor.PageProcessor;
import com.alan.databee.spider.selector.Html;

import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class JXContentProcessor implements PageProcessor {

    @Override
    public void process(Page page, Site site) {
        Html html = page.getHtml();
        String basicXpath = "//div[@class='artile_zw']/div[@class='bt-article-02']";
        String titleXpath = basicXpath+"/p[@class='sp_title con-title']";
        String contentXpath = basicXpath + "/div[@id='zoom']/p";
        String titleString = html.xpath(titleXpath).get();
        String titleRegex = "begin-->(.*)";
        Pattern pattern = Pattern.compile(titleRegex);
        Matcher titleMatcher = pattern.matcher(titleString);
        page.putField("url",page.getRequest().getUrl());
        if(titleMatcher.find()){
            page.putField("title",titleMatcher.group(1));
        }
        List<String> allContent = html.xpath(contentXpath).all();
        String contentRegex = "<p style=\"(.*)text-indent: 2em;\">(.*)</p>";
        Pattern contentPattern = Pattern.compile(contentRegex);
        StringBuilder builder = new StringBuilder();
        for(String content:allContent){
            Matcher matcher = contentPattern.matcher(content);
            if(matcher.find()){
                builder.append(matcher.group(2));
            }
        }
        String content = builder.toString();
        if(content.length() == 0){
            content = page.getRequest().getUrl();
        }
        page.putField("content",content);
    }

    @Override
    public Site getSite() {
        return null;
    }

    @Override
    public void setSite() {

    }
}

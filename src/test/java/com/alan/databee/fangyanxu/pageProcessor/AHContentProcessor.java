package com.alan.databee.fangyanxu.pageProcessor;

import com.alan.databee.common.util.log.LoggerUtil;
import com.alan.databee.spider.Site;
import com.alan.databee.spider.page.Page;
import com.alan.databee.spider.processor.PageProcessor;
import com.alan.databee.spider.selector.Html;
import com.alan.databee.spider.selector.Selectable;
import org.dom4j.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class AHContentProcessor implements PageProcessor {
    private static final Logger LOGGER = LoggerFactory.getLogger("PageProcessorLogger");

    @Override
    public void process(Page page, Site site) {
        Html html = page.getHtml();
        Selectable xpath = html.xpath("//div[@class='secnr']/div[@class='wenzhang']");
        Selectable titleXpath = xpath.xpath("//h1");
        String titleString = titleXpath.get();
        String contentString = xpath.xpath("//div[@class='wzcon j-fontContent']").get();
        page.putField("url", page.getRequest().getUrl());
        String titleRegex = "<h1 class=\"wztit\" (.*?)>(.*?)</h1>";
        Pattern pattern = Pattern.compile(titleRegex);
        Matcher matcher = pattern.matcher(titleString);
        if (matcher.find()) {
            String title = matcher.group(2);
            page.putField("title", title.replaceAll("<br>", ""));
        } else {
            LoggerUtil.error(LOGGER, "title匹配出错", page.getUrl(),titleRegex);
        }
        try {
            Document document1 = DocumentHelper.parseText(contentString);
            Element rootElement1 = document1.getRootElement();

            List<Element> elements = rootElement1.selectNodes("//p");
            StringBuilder builder = new StringBuilder();
            for (Element element : elements) {
                builder.append(element.getText());
            }
            String content = builder.toString();
            page.putField("content", content);
        } catch (DocumentException e) {
            List<String> all = xpath.xpath("//div[@class='wzcon j-fontContent']/p").all();
            StringBuilder builder = new StringBuilder();
            for (String s : all) {
                String regex = "<p(.*?)\">(<img.*?\".*?\">)*(<span.*?>)*(.*?)(</span>)*</p>";
                Matcher matcher1 = Pattern.compile(regex).matcher(s);
                if (matcher1.find()) {
                    String content = matcher1.group(4);
                    builder.append(content);
                } else {
                    LoggerUtil.error(LOGGER,"content匹配出错",page.getUrl(),regex);
                }
            }
            page.putField("content", builder.toString());

        }
        LoggerUtil.info(LOGGER, "页面解析完成", page.getUrl());
    }

    @Override
    public Site getSite() {
        return null;
    }

    @Override
    public void setSite() {

    }
}

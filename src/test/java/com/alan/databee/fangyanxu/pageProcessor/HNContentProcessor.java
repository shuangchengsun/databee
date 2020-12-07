package com.alan.databee.fangyanxu.pageProcessor;

import com.alan.databee.common.util.log.LoggerUtil;
import com.alan.databee.spider.Site;
import com.alan.databee.spider.page.Page;
import com.alan.databee.spider.processor.PageProcessor;
import com.alan.databee.spider.selector.Html;
import com.alan.databee.spider.selector.Selectable;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class HNContentProcessor implements PageProcessor {
    private static final Logger LOGGER = LoggerFactory.getLogger("PageProcessorLogger");

    @Override
    public void process(Page page, Site site) {
        Html html = page.getHtml();
        Selectable basicXpath = html.xpath("//div[@class='xly_bg']/div[@class='xly_Box']");
        String titleRegex = "<h3(.*?)>(.*?)</h3>";
        String titleString = basicXpath.xpath("//h3[@class='sp_title']").get();
        Matcher matcher = Pattern.compile(titleRegex).matcher(titleString);
        String title = null;
        page.putField("url", page.getRequest().getUrl());
        if (matcher.find()) {
            title = matcher.group(2);
        } else {
//            System.out.println("title匹配出错 " + titleRegex + " " + titleString);
            LoggerUtil.error(LOGGER, "title匹配出错 ", titleRegex, page.getUrl());
        }
        List<String> all = basicXpath.xpath("//div[@id='zoom']/p").all();
        String contentRegex = "<p(.*)>(.*?)</p>";
        Pattern pattern = Pattern.compile(contentRegex);
        StringBuilder builder = new StringBuilder();
        for (String s : all) {
            Matcher matcher1 = pattern.matcher(s);
            if (matcher1.find()) {
                String group = matcher1.group(2);
                builder.append(group);
            } else {
//                System.out.println("内容匹配失败 " + contentRegex + " " + s);
                LoggerUtil.error(LOGGER, "内容匹配失败 ", contentRegex, page.getUrl());
            }
        }
        page.putField("title", title);
        page.putField("content", builder.toString());
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

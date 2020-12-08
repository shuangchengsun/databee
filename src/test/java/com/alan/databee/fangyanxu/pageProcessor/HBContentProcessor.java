package com.alan.databee.fangyanxu.pageProcessor;

import com.alan.databee.common.util.log.LoggerUtil;
import com.alan.databee.spider.Site;
import com.alan.databee.spider.page.Page;
import com.alan.databee.spider.processor.PageProcessor;
import com.alan.databee.spider.selector.Html;
import com.alan.databee.spider.selector.Selectable;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class HBContentProcessor implements PageProcessor {
    private static final Logger LOGGER = LoggerFactory.getLogger("PageProcessorLogger");
    @Override
    public void process(Page page, Site site) {
        Html html = page.getHtml();
        Selectable titleXpath = html.xpath("//h2[@class='text-center']");
        String titleString = titleXpath.get();

        page.putField("url",page.getUrl());
        Selectable contentXpath = html.xpath("//div[@class='row content_block']/div[@class='col-xs-12 xs_nopad_md_pad']/div[@class='view TRS_UEDITOR trs_paper_default trs_web']/p");
        List<String> all = contentXpath.all();
        StringBuilder builder = new StringBuilder();
        try {
            Document document = DocumentHelper.parseText(titleString);
            String title = document.getRootElement().getText();
            page.putField("title", title);
            for (String s : all) {
                Pattern pattern = Pattern.compile("<p style=(.*?)\">(.*?)</p>");
                Matcher matcher = pattern.matcher(s);
                if (matcher.find()) {
                    builder.append(matcher.group(2));
                } else {
                    LoggerUtil.warn(LOGGER, "匹配出错", page.getUrl());
                }
            }
        } catch (DocumentException e) {
            LoggerUtil.warn(LOGGER, "匹配出错", page.getUrl());
        }
        String content = builder.toString();
        page.putField("content",content);
        LoggerUtil.info(LOGGER,"页面解析完成",page.getUrl());
    }

    @Override
    public Site getSite() {
        return null;
    }

    @Override
    public void setSite() {

    }
}

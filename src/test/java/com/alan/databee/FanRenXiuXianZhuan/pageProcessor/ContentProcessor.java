package com.alan.databee.FanRenXiuXianZhuan.pageProcessor;

import com.alan.databee.spider.Site;
import com.alan.databee.spider.page.Page;
import com.alan.databee.spider.processor.PageProcessor;
import com.alan.databee.spider.selector.Selectable;
import jdk.nashorn.internal.runtime.regexp.joni.MatcherFactory;
import jdk.nashorn.internal.runtime.regexp.joni.Regex;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;

import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ContentProcessor implements PageProcessor {
    @Override
    public void process(Page page, Site site) {
        Selectable xpath = page.getHtml().xpath("//article[@class='post clearfix']");
        String title = xpath.xpath("//h1[@id='nr_title']").get();
        MatcherFactory compile = new Regex("[\\u4e00-\\u9fa5]").compile();
        Pattern pattern = Pattern.compile(">(.+)</h1>");
        Matcher matcher = pattern.matcher(title);
        if(matcher.find()){
            title = matcher.group(1);
        }
        String s = xpath.xpath("//div[@id='nr1']").get();
        StringBuilder builder = new StringBuilder();
        try {
            Document document = DocumentHelper.parseText(s);
            Element rootElement = document.getRootElement();
            List<Element> nodes = rootElement.selectNodes("//p");
            for(Element node : nodes ){
                String text = node.getText();
                builder.append(text).append("\r\n\r\n\r\n");
            }
            page.putField("title",title);
            page.putField("content",builder.toString());
        } catch (DocumentException e) {
            e.printStackTrace();
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

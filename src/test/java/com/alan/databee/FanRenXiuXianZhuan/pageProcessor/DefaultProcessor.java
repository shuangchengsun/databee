package com.alan.databee.FanRenXiuXianZhuan.pageProcessor;

import com.alan.databee.spider.Site;
import com.alan.databee.spider.model.Request;
import com.alan.databee.spider.page.Page;
import com.alan.databee.spider.processor.PageProcessor;
import com.alan.databee.spider.selector.Html;
import com.alan.databee.spider.selector.Selectable;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;

import java.util.List;

public class DefaultProcessor implements PageProcessor {
    @Override
    public void process(Page page, Site site) {
        String rawText = page.getRawText();
//        System.out.println(rawText);
        Html html = page.getHtml();
        Selectable xpath = html.xpath("//div[@class='book-list clearfix']/ul");
        String s = xpath.get();
        try {
            Document document = DocumentHelper.parseText(s);
            Element rootElement = document.getRootElement();
            List<Element> nodes = rootElement.selectNodes("//li//a");
            for(Element element : nodes){
                String href = element.attributeValue("href");
                Request request = new Request(href)
                        .setPriority(1)
                        .setMethod("GET");
                request.setRetryTimes(5);
                page.addTargetRequest(request);
            }
            site.removeProcessor("default");
            site.processorAddLast("content",new ContentProcessor());
        }catch (DocumentException e) {
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

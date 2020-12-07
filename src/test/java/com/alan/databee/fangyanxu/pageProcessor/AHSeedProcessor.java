package com.alan.databee.fangyanxu.pageProcessor;

import com.alan.databee.common.util.log.LoggerUtil;
import com.alan.databee.service.ClassService;
import com.alan.databee.spider.Site;
import com.alan.databee.spider.exception.ClassServiceException;
import com.alan.databee.spider.model.Request;
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

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

public class AHSeedProcessor implements PageProcessor {
    private static final Logger LOGGER = LoggerFactory.getLogger("PageProcessorLogger");

    private ClassService classService;

    @Override
    public void process(Page page, Site site) {
        Request seedRequest = page.getRequest();
        String seed = seedRequest.getUrl();
        int seedNum = seed.charAt(seed.length() - 1) - '0';

        Html html = page.getHtml();
        Selectable xpath = html.xpath("//div[@class='navjz clearfix']/ul[@class='doc_list list-6782061']/li");
        List<String> all = xpath.all();
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
        boolean flag = true;
        try {
            Date current = simpleDateFormat.parse(simpleDateFormat.format(new Date()));
//            Date current = simpleDateFormat.parse("2020-11-30");
            for (String s : all) {
                Document document = DocumentHelper.parseText(s);
                Element rootElement = document.getRootElement();
                Element span = rootElement.element("span");
                Element a = rootElement.element("a");
                if (span == null || a == null) {
                    continue;
                }
                String timeString = span.getText();
                Date time = simpleDateFormat.parse(timeString);
                String href = a.attribute("href").getValue();
                String title = a.attributeValue("title");
                if (time.before(current)) {
                    // 出现了不合规的时间
                    flag = false;
                } else {
                    Request request = new Request(href);
                    request.setPriority(1);
                    page.addTargetRequest(request);
                }
                System.out.println("time: " + timeString + ", href: " + href + ", title: " + title);
            }

            if (flag) {
                // 说明还需要继续爬取新闻
                String substring = seed.substring(0, seed.length() - 1);
                seedRequest.setUrl(substring + (seedNum + 1));
                page.addTargetRequest(seedRequest);
            } else {
                site.removeProcessor("AHSeedProcessor");
                Object AHContentProcessor = classService.getComByName("AHContentProcessor");
                site.processorAddLast("AHContentProcessor", (PageProcessor) AHContentProcessor);
//                site.processorAddLast("AHContentProcessor", new AHContentProcessor());
            }
            LoggerUtil.info(LOGGER, "页面解析完成", page.getUrl());
        } catch (DocumentException | ParseException e) {
            e.printStackTrace();
        } catch (ClassServiceException e) {
            e.printStackTrace();
            LoggerUtil.info(LOGGER, "替换解析器出错", page.getUrl());
        }


    }

    @Override
    public Site getSite() {
        return null;
    }

    @Override
    public void setSite() {

    }

    public void setClassService(ClassService classService) {
        this.classService = classService;
    }
}

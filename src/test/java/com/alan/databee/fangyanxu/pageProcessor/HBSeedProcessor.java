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
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class HBSeedProcessor implements PageProcessor {
    private static final Logger LOGGER = LoggerFactory.getLogger("PageProcessorLogger");

    private ClassService classService;

    @Override
    public void process(Page page, Site site) {
        String basic = site.getSeed();
        Html html = page.getHtml();
        Selectable xpath = html.xpath("//div[@class='row list_block']/div[@class='col-xs-12 xs_nopad_md_pad']/ul[@class='list-unstyled news_list']/li");
        List<String> all = xpath.all();
        Request seedRequest = page.getRequest();
        String seedUrl = seedRequest.getUrl();
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
        boolean flag = true;
        try {
            Date current = simpleDateFormat.parse(simpleDateFormat.format(new Date()));
            for (String s : all) {
                Document document = DocumentHelper.parseText(s);
                Element rootElement = document.getRootElement();
                Element a = rootElement.element("a");
                List<Element> spans = (List<Element>) rootElement.elements("span");
                String href = null;
                String title = null;
                href = a.attributeValue("href");
                title = a.getText();
                for (Element span : spans) {
                    if (span.attribute("class").getValue().equalsIgnoreCase("pull-right hidden-xs")) {
                        String timeString = span.getText();
                        Date time = simpleDateFormat.parse(timeString);
                        if (time.before(current)) {
                            flag = false;
                        } else {
                            Request request = new Request(basic + href);
                            request.setPriority(1);
                            page.addTargetRequest(request);
                        }
                    }
                }
            }
            if (flag) {
                // 说明还需要继续查找新闻列表
                String[] split = seedUrl.split("/");
                String s = split[split.length - 1];
                if (s.contains("_")) {
                    Pattern pattern = Pattern.compile("[0-9]+[0-9]*");
                    Matcher matcher = pattern.matcher(s);
                    if (matcher.find()) {
                        int index = Integer.parseInt(matcher.group());
                        StringBuilder builder = new StringBuilder();
                        for (int i = 0; i < split.length - 1; i++) {
                            builder.append(split[i]).append("/");
                        }
                        builder.append("index_").append(index + 1).append(".shtml");
                        String newSeed = builder.toString();
                        seedRequest.setUrl(newSeed);
                    } else {
                        System.out.println("在更新seed时发生错误");
                    }
                } else {
                    StringBuilder builder = new StringBuilder();
                    for (int i = 0; i < split.length - 1; i++) {
                        builder.append(split[i]).append("/");
                    }
                    builder.append("index_1.shtml");
                    String newSeed = builder.toString();
                    seedRequest.setUrl(newSeed);
                    page.addTargetRequest(seedRequest);
                }
            } else {
                site.removeProcessor("HBSeedProcessor");
                try {
                    Object HBContentProcessor = classService.getComByName("HBContentProcessor");
                    site.processorAddLast("HBContentProcessor", (PageProcessor) HBContentProcessor);
//                    site.processorAddLast("HBContentProcessor",new HBContentProcessor());
                } catch (Exception | ClassServiceException e) {
                    LoggerUtil.error(LOGGER, "替换processor出错", e);
                }
            }
        } catch (DocumentException | ParseException e) {
            LoggerUtil.error(LOGGER, "页面解析错误", page.getUrl(), e);
        }
        LoggerUtil.info(LOGGER, "页面处理完毕", page.getUrl());
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

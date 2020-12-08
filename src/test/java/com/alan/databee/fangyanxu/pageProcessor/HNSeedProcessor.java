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
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class HNSeedProcessor implements PageProcessor {
    private static final Logger LOGGER = LoggerFactory.getLogger("PageProcessorLogger");

    private ClassService classService;

    @Override
    public void process(Page page, Site site) {
        Html html = page.getHtml();
        Selectable xpath = html.xpath("//div[@class='yl-list2']/div[@class='yl-listbox']/ul/li");
        List<String> all = xpath.all();
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
        String basic = site.getSeed();
        boolean flag = true;
        try {
            Date today = simpleDateFormat.parse(simpleDateFormat.format(new Date()));
            Calendar calendar = Calendar.getInstance();
            calendar.setTime(today);
            int circle = site.getTaskCircle();
            calendar.add(Calendar.DATE, -circle);
            for (String ulString : all) {
                Document document = DocumentHelper.parseText(ulString);
                Element rootElement = document.getRootElement();
                Element a = rootElement.element("a");
                Element span = rootElement.element("span");
                String timeString = span.getText();
                String href = a.attributeValue("href");
                String title = a.attributeValue("title");
                Date time = simpleDateFormat.parse(timeString);
                Calendar current = Calendar.getInstance();
                current.setTime(time);
                if (current.after(calendar)) {
                    Request request = new Request(basic + href);
                    request.setPriority(1);
                    page.addTargetRequest(request);
                } else {
                    // 出现了不合规的时间
                    flag = false;
                }
            }

            if (flag) {
                String url = page.getRequest().getUrl();
                String[] split = url.split("/");
                String param = split[split.length - 1];
                Matcher matcher = Pattern.compile("[0-9]+").matcher(param);
                String newParam = null;
                if (matcher.find()) {
                    int i = Integer.parseInt(matcher.group());
                    newParam = "gl_fgsjpx_" + (i + 1) + ".html";
                } else {
                    newParam = "gl_fgsjpx_2.html";
                }
                Request request = page.getRequest();
                StringBuilder builder = new StringBuilder();
                for (int k = 0; k < split.length - 1; k++) {
                    builder.append(split[k]).append("/");
                }
                builder.append(newParam);
                request.setUrl(builder.toString());
                page.addTargetRequest(request);
            } else {
                site.removeProcessor("HNSeedProcessor");
                Object HNContentProcessor = classService.getComByName("HNContentProcessor");
                site.processorAddLast("HNContentProcessor", (PageProcessor) HNContentProcessor);
            }
            LoggerUtil.info(LOGGER, "页面解析完成", page.getUrl());
        } catch (DocumentException | ParseException | ClassServiceException e) {
            LoggerUtil.error(LOGGER, "页面解析错误", page.getUrl(), e);
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

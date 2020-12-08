package com.alan.databee.fangyanxu.pageProcessor;

import com.alan.databee.common.util.log.LoggerUtil;
import com.alan.databee.service.ClassService;
import com.alan.databee.spider.Site;
import com.alan.databee.spider.exception.ClassServiceException;
import com.alan.databee.spider.model.Request;
import com.alan.databee.spider.page.Page;
import com.alan.databee.spider.processor.PageProcessor;
import com.alan.databee.spider.selector.Html;
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

public class SXSeedProcessor implements PageProcessor {
    private static final Logger LOGGER = LoggerFactory.getLogger("PageProcessorLogger");

    private ClassService classService;

    @Override
    public void process(Page page, Site site) {
        Html html = page.getHtml();
        List<String> all = html.xpath("//div[@class='tab-flag-construck']/ul/li").all();

        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
        String basic = site.getSeed();

        try {
            boolean flag = true;
            Date today = simpleDateFormat.parse(simpleDateFormat.format(new Date()));
            Calendar calendar = Calendar.getInstance();
            calendar.setTime(today);
            int circle = site.getTaskCircle();
            calendar.add(Calendar.DATE, -circle);
            for (String s : all) {
                Document document = DocumentHelper.parseText(s);
                Element rootElement = document.getRootElement();
                Element a = rootElement.element("a");
                Element span = rootElement.element("span");

                String timeString = span.getText();
                String title = a.attributeValue("title");
                String href = a.attributeValue("href");

                Date time = simpleDateFormat.parse(timeString.substring(1, timeString.length() - 1));
                Calendar current = Calendar.getInstance();
                current.setTime(time);
                if (current.after(calendar)) {
                    Request request = new Request(basic + href);
                    request.setMethod("GET")
                            .setPriority(1);
                    page.addTargetRequest(request);
                } else {
                    // 出现了不合规的时间
                    flag = false;
                }
                System.out.println("time: " + timeString + ", href: " + href + ", title" + title);
            }

            if (flag) {
                // 需要继续爬取
                Request request = site.getSeedRequest();
                String url = request.getUrl();
                Matcher matcher = Pattern.compile("[0-9]+").matcher(url);
                String newUrl = null;
                if (matcher.find()) {
                    int i = Integer.parseInt(matcher.group());
                    newUrl = matcher.replaceFirst(String.valueOf(i + 1));
                } else {
                    newUrl = url.replaceAll("index.shtml", "index_1.shtml");
                }
                request.setUrl(newUrl);
                page.addTargetRequest(request);
            } else {
                site.removeProcessor("SXSeedProcessor");
                Object sxContentProcessor = classService.getComByName("SXContentProcessor");
                site.processorAddLast("SXContentProcessor", (PageProcessor) sxContentProcessor);
//                site.processorAddFirst("SXContentProcessor", new SXContentProcessor());
            }
            LoggerUtil.info(LOGGER, "页面解析完成", page.getUrl());
        } catch (DocumentException | ParseException | ClassServiceException e) {
            LoggerUtil.error(LOGGER, "页面解析出现错误", page.getUrl());
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

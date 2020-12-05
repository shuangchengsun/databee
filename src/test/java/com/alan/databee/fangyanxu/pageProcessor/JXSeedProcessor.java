package com.alan.databee.fangyanxu.pageProcessor;

import com.alan.databee.common.util.log.LoggerUtil;
import com.alan.databee.service.ClassService;
import com.alan.databee.spider.Site;
import com.alan.databee.spider.exception.ClassServiceException;
import com.alan.databee.spider.model.Request;
import com.alan.databee.spider.page.Page;
import com.alan.databee.spider.processor.PageProcessor;

import com.alan.databee.spider.utils.UrlUtils;
import org.dom4j.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import sun.rmi.runtime.Log;


import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class JXSeedProcessor implements PageProcessor {
    private static final Logger LOGGER = LoggerFactory.getLogger("PageProcessorLogger");
    private ClassService classService;

    @Override
    public void process(Page page, Site site) {


        String rawText = page.getRawText();
        Pattern pattern = Pattern.compile("<record><!\\[CDATA\\[<li>(.*?)</li>(.*?)\\]\\]></record>");
        Matcher matcher = pattern.matcher(rawText);
        boolean flag = true;
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy/MM/dd");
        try {
            Date current = simpleDateFormat.parse(simpleDateFormat.format(new Date()));
            while (matcher.find()) {
                String group = "<li>" + matcher.group(1) + "</li>";
                Document document = DocumentHelper.parseText(group);
                Element rootElement = document.getRootElement();
                Element span = rootElement.element("span");
                Element a = rootElement.element("a");
                String timeString = span.getText();
                Date time = simpleDateFormat.parse(timeString);
                if (time.before(current)) {
                    flag = false;
                } else {
                    Attribute href = a.attribute("href");
                    String newsName = a.getText();
                    Request request = new Request(href.getValue());
                    request.setPriority(1);
                    page.addTargetRequest(request);
                }
            }
        } catch (DocumentException | ParseException e) {
            e.printStackTrace();
            LoggerUtil.error(LOGGER,"解析文本时发生错误",e);
        }

        Request seedRequest = page.getRequest();
        String seed = seedRequest.getUrl();
        if (flag) {
            // 说明全部的时间都符合预期
            Map<String, String> map = UrlUtils.parseParams(seed);
            int startrecord = Integer.parseInt(map.get("startrecord"));
            int endrecord = Integer.parseInt(map.get("endrecord"));
            int perpage = Integer.parseInt(map.get("perpage"));
            map.put("startrecord", String.valueOf(endrecord + 1));
            map.put("endrecord", String.valueOf(endrecord + 3 * perpage));
            String base = map.get("main");
            map.remove("main");
            String newSeed = UrlUtils.buildUrl(base, map);
            Map<String, String> headers = seedRequest.getHeaders();
            Map<String, String> extras = seedRequest.getExtras();
            Request newRequest = new Request(newSeed);
            newRequest.setHeaders(headers);
            newRequest.setExtras(extras);
            newRequest.setPriority(0);
            newRequest.setMethod("POST");
            page.addTargetRequest(newRequest);
        } else {
            // 说明出现了不符合要求的时间数据，后续的不需要在访问
            site.removeProcessor("seedProcessor");
            try {
                Object jxContentProcessor = classService.getComByName("JXContentProcessor");
                site.processorAddLast("contentProcessor", (PageProcessor) jxContentProcessor);
            } catch (ClassServiceException e) {
                LoggerUtil.error(LOGGER,"processor替换失败",page.getUrl());
                e.printStackTrace();

            }
        }
        LoggerUtil.info(LOGGER,"页面解析完成",page.getUrl());

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

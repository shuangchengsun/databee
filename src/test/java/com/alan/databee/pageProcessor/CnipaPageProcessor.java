package com.alan.databee.pageProcessor;


import com.alan.databee.spider.Site;
import com.alan.databee.spider.page.Page;
import com.alan.databee.spider.processor.PageProcessor;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class CnipaPageProcessor implements PageProcessor {
    @Override
    public void process(Page page, Site site) {
        String xpath = page.getHtml().xpath("//ul[@class='list clearfix']").toString();
        String regex = "http.+\" ";
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(xpath);
        while(matcher.find()) {
            String res = matcher.group();
            String url = res.substring(0, res.length() - 2).replace(';','&');
            site.addUrl(url);
        }
        site.removeProcessor(this);
        site.processorAddLast("pdfProcessor",new PdfProcessor());
    }


    @Override
    public Site getSite() {
        return null;
    }

    @Override
    public void setSite() {

    }
}

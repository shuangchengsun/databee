package com.alan.databee.fangyanxu;

import com.alan.databee.spider.Site;
import com.alan.databee.spider.downloader.Downloader;
import com.alan.databee.spider.downloader.HttpClientDownloader;
import com.alan.databee.spider.model.Request;
import com.alan.databee.spider.page.Page;
import com.alan.databee.spider.selector.Html;

import org.dom4j.*;
import org.junit.Test;
import org.springframework.boot.test.context.SpringBootTest;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@SpringBootTest
public class DownloaderTest {

    @Test
    public void testHttpDownloader() throws IOException, DocumentException, ParseException {
        Downloader downloader = new HttpClientDownloader();
        Request request = new Request("http://www.shanxi.gov.cn/yw/sxyw/index_1.shtml");
        Site site = Site.me();
        Page page = downloader.download(request, site);
        Html html = page.getHtml();
        List<String> all = html.xpath("//div[@class='tab-flag-construck']/ul/li").all();
        for (String s : all) {
            Document document = DocumentHelper.parseText(s);
            Element rootElement = document.getRootElement();
            Element a = rootElement.element("a");
            Element span = rootElement.element("span");
            String timeString = span.getText();
            String title = a.attributeValue("title");
            String href = a.attributeValue("href");
            System.out.println("time: " + timeString + ", href" + href + ", title" + title);
        }


    }

    @Test
    public void regexTest() throws ParseException {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
        Date parse = simpleDateFormat.parse("2020-12-06");

    }
}

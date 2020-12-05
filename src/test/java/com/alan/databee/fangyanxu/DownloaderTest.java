package com.alan.databee.fangyanxu;

import com.alan.databee.spider.Site;
import com.alan.databee.spider.downloader.Downloader;
import com.alan.databee.spider.downloader.HttpClientDownloader;
import com.alan.databee.spider.downloader.SeleniumDownloader;
import com.alan.databee.spider.model.Request;
import com.alan.databee.spider.page.Page;
import com.alan.databee.spider.selector.Html;
import com.alan.databee.spider.selector.Selectable;
import com.sun.deploy.net.HttpDownload;
import com.sun.org.apache.xerces.internal.jaxp.SAXParserImpl;
import org.apache.http.NameValuePair;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import org.dom4j.*;
import org.dom4j.io.SAXReader;
import org.junit.Test;
import org.springframework.boot.test.context.SpringBootTest;

import javax.xml.parsers.SAXParser;
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
        Downloader downloader = new SeleniumDownloader();
        Request request = new Request("http://www.hubei.gov.cn/zwgk/hbyw/hbywqb/index.shtml");
        Site site = Site.me();
        Page page = downloader.download(request, site);
        Html html = page.getHtml();
        Selectable xpath = html.xpath("//div[@class='row list_block']/div[@class='col-xs-12 xs_nopad_md_pad']/ul[@class='list-unstyled news_list']/li");
        List<String> all = xpath.all();

        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
        Date current = simpleDateFormat.parse(simpleDateFormat.format(new Date()));
        for (String s : all) {
            Document document = DocumentHelper.parseText(s);
            Element rootElement = document.getRootElement();
            Element a = rootElement.element("a");
            List<Element> spans = (List<Element>) rootElement.elements("span");
            String href = null;
            String title = null;
            String timeString = null;
            href = a.attributeValue("href");
            title = a.attributeValue("title");
            for (Element span : spans) {
                if (span.attribute("class").getValue().equalsIgnoreCase("pull-right hidden-xs")) {
                    timeString = span.getText();
                    Date time = simpleDateFormat.parse(timeString);

                    System.out.println("time: " + timeString + ", " + "href: " + href + ", " + "title: " + title);

                }
            }
//            Iterator iterator = rootElement.elementIterator();
//            String href = null;
//            String title=null;
//            while (iterator.hasNext()){
//                Element next = (Element) iterator.next();
//                Attribute attribute = next.attribute("class");
//                if(next.getName().equalsIgnoreCase("a")){
//                    href = next.attribute("href").getValue();
//                    title = next.attribute("title").getValue();
//                }
//                if(attribute != null && attribute.getValue().equalsIgnoreCase("pull-right hidden-xs")){
//                    String time = attribute.getValue();
//                    System.out.println("time: "+time+", "+"href: "+href+", "+"title: "+title);
//                }
//
//            }
        }


        String rawText = page.getRawText();
        System.out.println(rawText);


    }
}

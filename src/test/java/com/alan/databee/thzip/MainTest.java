package com.alan.databee.thzip;

import com.alan.databee.model.RequestConfig;
import com.alan.databee.spider.Site;
import com.alan.databee.spider.downloader.Downloader;
import com.alan.databee.spider.downloader.HttpClientDownloader;
import com.alan.databee.spider.downloader.SeleniumDownloader;
import com.alan.databee.spider.model.Request;
import com.alan.databee.spider.page.Page;
import com.alan.databee.spider.utils.HttpConstant;
import com.alibaba.fastjson.JSON;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.Capabilities;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.HashMap;
import java.util.Map;

@SpringBootTest
public class MainTest {

    @Test
    public void CDST() {
        Request request = new Request("http://cdst.chengdu.gov.cn/cdkxjsj/c108728/part_list_more.shtml")
                .setMethod("GET").setPriority(0);
        RequestConfig requestConfig = new RequestConfig();
        requestConfig.setMethod(request.getMethod());
        requestConfig.setExtras(request.getExtras());
        requestConfig.setHeaders(request.getHeaders());
        requestConfig.setPriority((int) request.getPriority());
        requestConfig.setUrl(request.getUrl());
        String json = JSON.toJSONString(requestConfig);
        System.out.println(json);

        Site site = Site.me()
                .setSeed("http://cdst.chengdu.gov.cn/cdkxjsj/c108728/part_list_more.shtml");

    }


    public static void main(String[] args) throws InterruptedException {
        Request request = new Request("http://cdst.chengdu.gov.cn/es-search/search/63dbfe2e6c324e7e89395ecb6ca69d35?" +
                "template=zhaofa/yjgl_list&_isAgg=0&_pageSize=15&page=1")
                .setMethod("GET").setPriority(0);
        Map<String, String> headers = new HashMap<>();
        headers.put("Host", "cdst.chengdu.gov.cn");
        headers.put("User-Agent", "Mozilla/5.0 (Macintosh; Intel Mac OS X 10.16; rv:84.0) Gecko/20100101 Firefox/84.0");
        headers.put("Accept", "text/html, */*; q=0.01");
        headers.put("Accept-Language", "zh-CN,zh;q=0.8,zh-TW;q=0.7,zh-HK;q=0.5,en-US;q=0.3,en;q=0.2");
        headers.put("Accept-Encoding", "gzip, deflate");
        headers.put("X-Requested-With", "XMLHttpRequest");
        headers.put("DNT", "1");
        headers.put("Connection", "keep-alive");
        headers.put("Referer", "http://cdst.chengdu.gov.cn/cdkxjsj/c108728/part_list_more.shtml");
        headers.put("Cookie", "azSsQE5NvspcS=52ylqBgOo8.Wizyl8KkTVgpySBFtIlatGDvBkyHa55.ghyu3ZDDxoJqQSgBzSvOu7j7eDlC9uE5PRuridootu5a; azSsQE5NvspcT=53qBakDqhZr9qqqm6h.SYAqXjsVgCidUB.ZTiVimX3MERrBt_KHOnF_OAGW71k3W5fL95yDv6is_qNTvDeq3DHl.L2nag94BBFzQxI9Y4dJ7YuSO8y4ZgaZKqVwa1ONxpcHyPyfam_Dh85bXTUIDG3JOzPXgK88iGv9VfwHWP1oM.0fj5xR5cScXSxfIYqJafJ5J6BI.BDhbnrbbsQVuylA.AzMvFeVmyImetUpqFfHIkL.iSB8EFsqN5rhxvy7q820CssuOZ0kfO027ZbCxLB4whx9nvkDJiroIlrxcR.245NbCxGidNSDDOMtkiHZkKW; yfx_c_g_u_id_10000063=_ck21012019485115100155013771299; yfx_f_l_v_t_10000063=f_t_1611143331512__r_t_1611387442267__v_t_1611394100698__r_c_1; toolbarStatus=open");

        request.setHeaders(headers);
        Downloader downloader = new HttpClientDownloader();
        Page page = downloader.download(request, Site.me());
        int statusCode = page.getStatusCode();
        System.out.println("status code is " + statusCode);
        System.out.println(page.getRawText());
        downloader.shutdown();
    }

}

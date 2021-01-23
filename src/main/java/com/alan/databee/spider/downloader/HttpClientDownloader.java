package com.alan.databee.spider.downloader;

import com.alan.databee.spider.Site;
import com.alan.databee.spider.model.Request;
import com.alan.databee.spider.page.Page;
import com.alan.databee.spider.proxy.Proxy;
import com.alan.databee.spider.proxy.ProxyProvider;
import com.alan.databee.spider.selector.PlainText;
import com.alan.databee.spider.utils.CharsetUtils;
import com.alan.databee.spider.utils.HttpClientUtils;
import com.alan.databee.spider.utils.HttpConstant;
import org.apache.commons.io.IOUtils;
import org.apache.http.HttpRequest;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.nio.charset.Charset;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;


/**
 * The http downloader based on HttpClient.
 *
 * @author code4crafter@gmail.com <br>
 * @since 0.1.0
 */
public class HttpClientDownloader extends AbstractDownloader {

    private Logger logger = LoggerFactory.getLogger(getClass());

    private final Map<String, CloseableHttpClient> httpClients = new HashMap<String, CloseableHttpClient>();

    private HttpClientGenerator httpClientGenerator = new HttpClientGenerator();

    private HttpUriRequestConverter httpUriRequestConverter = new HttpUriRequestConverter();
    
    private ProxyProvider proxyProvider;

    private boolean responseHeader = true;

    public void setHttpUriRequestConverter(HttpUriRequestConverter httpUriRequestConverter) {
        this.httpUriRequestConverter = httpUriRequestConverter;
    }

    public void setProxyProvider(ProxyProvider proxyProvider) {
        this.proxyProvider = proxyProvider;
    }

    private CloseableHttpClient getHttpClient(Site site) {
        if (site == null) {
            return httpClientGenerator.getClient(null);
        }
        String domain = site.getSeed();
        CloseableHttpClient httpClient = httpClients.get(domain);
        if (httpClient == null) {
            synchronized (this) {
                httpClient = httpClients.get(domain);
                if (httpClient == null) {
                    httpClient = httpClientGenerator.getClient(site);
                    httpClients.put(domain, httpClient);
                }
            }
        }
        return httpClient;
    }

    @Override
    public Page download(Request request,  Site site) {
        if (site == null) {
            throw new NullPointerException("site can not be null");
        }
        CloseableHttpResponse httpResponse = null;
        CloseableHttpClient httpClient = getHttpClient(site);
        Proxy proxy = proxyProvider != null ? proxyProvider.getProxy(site) : null;

        HttpClientRequestContext requestContext = httpUriRequestConverter.convert(request, site, proxy);

        Page page = Page.fail();
        page.setRequest(request);
        try {
            HttpUriRequest httpRequest = requestGen(request, site);
//            httpResponse = httpClient.execute(requestContext.getHttpUriRequest(), requestContext.getHttpClientContext());
            httpResponse = httpClient.execute(httpRequest);
            page = handleResponse(request, request.getCharset() != null ? request.getCharset() : site.getCharset(), httpResponse, site);
            onSuccess(request);
            logger.info("downloading page success {}", request.getUrl());
            return page;
        } catch (IOException e) {
            logger.warn("download page {} error", request.getUrl(), e);
            onError(request);
            page.setDownloadSuccess(false);
            return page;
        } finally {
            if (httpResponse != null) {
                //ensure the connection is released back to pool
                EntityUtils.consumeQuietly(httpResponse.getEntity());
            }
            if (proxyProvider != null && proxy != null) {
                proxyProvider.returnProxy(proxy, page, site);
            }
        }
    }
    protected HttpUriRequest requestGen(Request request,Site site) throws UnsupportedEncodingException {
        String method = request.getMethod();
        HttpUriRequest httpRequest = null;
        if(method == null||method.equalsIgnoreCase(HttpConstant.Method.GET)){
            httpRequest = new HttpGet(request.getUrl());
            Map<String, String> headers = request.getHeaders();
            for(Map.Entry<String,String> entry : headers.entrySet()){
                httpRequest.setHeader(entry.getKey(), entry.getValue());
            }
        }else if(method.equalsIgnoreCase(HttpConstant.Method.POST)){
            httpRequest = new HttpPost(request.getUrl());
            HttpPost post = (HttpPost) httpRequest;
            Map<String, String> headers = request.getHeaders();
            for(Map.Entry<String,String> entry : headers.entrySet()){
                httpRequest.setHeader(entry.getKey(), entry.getValue());
            }
            Map<String, String> extras = request.getExtras();
            List<NameValuePair> params = new LinkedList<>();
            for(Map.Entry<String,String> entry:extras.entrySet()){
                params.add(new BasicNameValuePair(entry.getKey(), entry.getValue()));
            }
            post.setEntity(new UrlEncodedFormEntity(params));
        }
        return httpRequest;
    }

    @Override
    public void setThread(int thread) {
        httpClientGenerator.setPoolSize(thread);
    }

    protected Page handleResponse(Request request, String charset, HttpResponse httpResponse, Site site) throws IOException {
        byte[] bytes = IOUtils.toByteArray(httpResponse.getEntity().getContent());
        String contentType = httpResponse.getEntity().getContentType() == null ? "" : httpResponse.getEntity().getContentType().getValue();
        Page page = new Page();
        page.setBytes(bytes);
        if (!request.isBinaryContent()){
            if (charset == null) {
                charset = getHtmlCharset(contentType, bytes);
            }
            page.setCharset(charset);
            page.setRawText(new String(bytes, charset));
        }
        page.setUrl(new PlainText(request.getUrl()));
        page.setRequest(request);
        page.setStatusCode(httpResponse.getStatusLine().getStatusCode());
        page.setDownloadSuccess(true);
        if (responseHeader) {
            page.setHeaders(HttpClientUtils.convertHeaders(httpResponse.getAllHeaders()));
        }
        return page;
    }

    private String getHtmlCharset(String contentType, byte[] contentBytes) throws IOException {

        String charset = CharsetUtils.detectCharset(contentType, contentBytes);
        if (charset == null) {
            charset = Charset.defaultCharset().name();
            logger.warn("Charset autodetect failed, use {} as charset. Please specify charset in Site.setCharset()", Charset.defaultCharset());
        }
        return charset;
    }

    @Override
    public void shutdown() {

    }
}

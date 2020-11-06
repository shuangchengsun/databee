package com.alan.databee.spider.proxy;


import com.alan.databee.spider.Site;
import com.alan.databee.spider.Task;
import com.alan.databee.spider.page.Page;

/**
 * Proxy provider. <br>
 *     
 * @since 0.7.0
 */
public interface ProxyProvider {

    /**
     *
     * Return proxy to Provider when complete a download.
     * @param proxy the proxy config contains host,port and identify info
     * @param page the download result
     * @param task the download task
     */
    void returnProxy(Proxy proxy, Page page, Site site);

    /**
     * Get a proxy for task by some strategy.
     * @param task the download task
     * @return proxy 
     */
    Proxy getProxy(Site site);

}

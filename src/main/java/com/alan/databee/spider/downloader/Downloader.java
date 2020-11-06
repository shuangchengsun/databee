package com.alan.databee.spider.downloader;


import com.alan.databee.spider.Site;
import com.alan.databee.spider.model.Request;
import com.alan.databee.spider.page.Page;

public interface Downloader {
    /**
     * Downloads web pages and store in Page object.
     *
     * @param request request
     * @param task task
     * @return page
     */
    public Page download(Request request, Site site);

    /**
     * Tell the downloader how many threads the spider used.
     * @param threadNum number of threads
     */
    public void setThread(int threadNum);
}

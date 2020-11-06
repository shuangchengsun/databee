package com.alan.databee.spider.processor;


import com.alan.databee.spider.Site;
import com.alan.databee.spider.page.Page;

public interface PageProcessor {
    /**
     * process the page, extract urls to fetch, extract the data and store
     *
     * @param page page
     */
    public void process(Page page, Site site);

    /**
     * get the site settings
     *
     * @return site
     * @see Site
     */
    public Site getSite();

    public void setSite();
}

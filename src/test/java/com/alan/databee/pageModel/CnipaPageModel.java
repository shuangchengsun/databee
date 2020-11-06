package com.alan.databee.pageModel;

import us.codecraft.webmagic.MultiPageModel;

import java.util.Collection;
import java.util.List;

public class CnipaPageModel implements MultiPageModel {

    private String pageKey;

    private String page;

    private List<String> otherPage;

    private String title;

    private String content;

    @Override
    public String getPageKey() {
        return null;
    }

    @Override
    public String getPage() {
        return null;
    }

    @Override
    public Collection<String> getOtherPages() {
        return null;
    }

    @Override
    public MultiPageModel combine(MultiPageModel multiPageModel) {
        return null;
    }
}

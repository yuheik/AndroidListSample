package com.sample.myapplication.Flickr;

import com.sample.myapplication.Utils.LogUtil;

import java.util.ArrayList;

class FlickrPhotos {
    private int                    page;
    private String                 pages;
    private int                    perpage;
    private String                 total;
    private ArrayList<FlickrPhoto> photo;

    public ArrayList getPhotos() {
        return photo;
    }

    public void dump() {
        LogUtil.debug("page    : " + this.page);
        LogUtil.debug("pages   : " + this.pages);
        LogUtil.debug("perpage : " + this.perpage);
        LogUtil.debug("total   : " + this.total);
        for (FlickrPhoto flickrPhoto : photo) {
            flickrPhoto.dump();
        }
    }
}

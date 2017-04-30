package com.sample.myapplication.Flickr;

import android.support.annotation.NonNull;

import com.google.gson.annotations.SerializedName;
import com.sample.myapplication.Utils.LogUtil;

import java.io.Serializable;

public class FlickrPhoto implements Serializable {
    private String id;
    private String owner;
    private String secret;
    private String server;
    private int    farm;
    private String title;
    @SerializedName("ispublic")
    private int    isPublic;
    @SerializedName("isfriend")
    private int    isFriend;
    @SerializedName("isfamily")
    private int    isFamily;
    private String url_o;
    private String url_m;

    public void dump() {
        LogUtil.dumpObject("Photo Entry", this);
    }

    public String getId()     { return id;     }
    public String getOwner()  { return owner;  }
    public String getTitle()  { return title;  }
    public String getSecret() { return secret; }

    public String getUrl() {
        return "https://farm" + farm + ".staticflickr.com/" + server + "/" + id + "_" + secret + ".jpg";
    }

    public boolean isEqual(@NonNull FlickrPhoto other) {
        return this.getId().equals(other.getId());
    }
}

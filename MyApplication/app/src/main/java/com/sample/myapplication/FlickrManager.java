package com.sample.myapplication;

import android.support.annotation.Nullable;

import com.sample.myapplication.Utils.LogUtil;
import com.sample.myapplication.Utils.NetworkUtil;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class FlickrManager {
    public enum Type {
        SEARCH,
        RECENT,
    }

    static final String ApiKey = "56550df01e50dba4228b82e187629d23";
    static final String ApiSecret = "b8e7d24b424bd775";

    static final String EndPoint = "https://api.flickr.com/services/rest/";

    public interface PhotosListener {
        void get(@Nullable Photos photos);
    }

    public static void search(final String keyword, final PhotosListener photosListener) {
        requestJSON(searchAPI(keyword), photosListener);
    }

    public static void recent(final PhotosListener photosListener) {
        requestJSON(recentAPI(), photosListener);
    }

    private static String flickrAPI(String apiName, String... params) {
        String url = EndPoint +
                     "?method=" + apiName +
                     "&api_key=" + ApiKey +
                     "&per_page=" + 20 +
                     "&format=json" +
                     "&nojsoncallback=1";
        for (String param : params) {
            url += "&" + param;
        }

        return url;
    }

    private static String searchAPI(String keyword) {
        return flickrAPI("flickr.photos.search", "text=" + keyword);
    }

    private static String recentAPI() {
        return flickrAPI("flickr.photos.getRecent");
    }

    private static void requestJSON(String urlString, final PhotosListener photosListener) {
        NetworkUtil.request(urlString, new NetworkUtil.RequestListener() {
            @Override
            public void onResult(String result) {
                LogUtil.debug(result);
                photosListener.get(parseFlickrApiResult(result));
            }
        });
    }

    private static Photos parseFlickrApiResult(String result) {
        LogUtil.traceFunc(result);

        if (result == null) {
            return null;
        }

        JSONObject root;
        try {
            root = new JSONObject(result);
            root = root.getJSONObject("photos");
        } catch (JSONException e) {
            LogUtil.error("convert JSON error", e);
            return null;
        }

        return new Photos(root);
    }

    public static class Photos {
        int page;
        int pages;
        int perpage;
        int total;
        ArrayList<Photo> photos = new ArrayList<>();

        public Photos(JSONObject data) {
            if (data == null) {
                LogUtil.error("data is null");
                return;
            }

            try {
                this.page    = data.getInt("page");
                this.pages   = data.getInt("pages");
                this.perpage = data.getInt("perpage");
                this.total   = data.getInt("total");

                JSONArray photoValues = data.getJSONArray("photo");
                for (int i = 0; i < photoValues.length(); i++) {
                    photos.add(new Photo(photoValues.getJSONObject(i)));
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

        public ArrayList getPhotos() { return photos; }

        public void dump() {
            LogUtil.debug("page    : " + this.page);
            LogUtil.debug("pages   : " + this.pages);
            LogUtil.debug("perpage : " + this.perpage);
            LogUtil.debug("total   : " + this.total);
            for (Photo photo : photos) {
                photo.dump();
            }
        }
    }

    public static class Photo {
        String  id;
        String  owner;
        String  secret;
        String  server;
        String  farm;
        String  title;
        boolean isPublic;
        boolean isFriend;
        boolean isFamily;

        String  url_o;
        String  url_m;

        public Photo(JSONObject data) {
            try {
                this.id       = data.getString("id");
                this.owner    = data.getString("owner");
                this.secret   = data.getString("secret");
                this.server   = data.getString("server");
                this.farm     = data.getString("farm");
                this.title    = data.getString("title");
                this.isPublic = !(data.getInt("ispublic") == 0);
                this.isFriend = !(data.getInt("isfriend") == 0);
                this.isFamily = !(data.getInt("isfamily") == 0);

                this.url_o = data.getString("url_o");
                this.url_m = data.getString("url_m");
            } catch (JSONException e) {
//                e.printStackTrace();
            }
        }

        public String getId()     { return id; }
        public String getTitle()  { return title; }
        public String getOwner()  { return owner; }
        public String getSecret() { return secret; }
        public String getServer() { return server; }
        public String getUrl()    {
            return  "https://farm" + farm + ".staticflickr.com/" + server + "/" + id + "_" + secret + ".jpg";
        }

        public void dump() {
            LogUtil.dumpObject("Photo Entry", this);
        }
    }
}

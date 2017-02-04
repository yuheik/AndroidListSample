package com.sample.myapplication;

import android.support.annotation.Nullable;

import com.sample.myapplication.Utils.LogUtil;
import com.sample.myapplication.Utils.NetworkUtil;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;
import java.util.ArrayList;

public class FlickrManager {
    public enum Type {
        SEARCH,
        RECENT,
    }

    static final String ApiKey = "56550df01e50dba4228b82e187629d23";
    static final String ApiSecret = "b8e7d24b424bd775";
    static final String EndPoint = "https://api.flickr.com/services/rest/";

    private static ArrayList<Photo> resultSearch = new ArrayList<>();
    private static ArrayList<Photo> resultRecent = new ArrayList<>();

    public interface PhotosListener {
        void get(@Nullable ArrayList<Photo> photos);
    }

    public static void search(final String keyword, int page, final PhotosListener photosListener) {
        if (page == 1) {
            resultSearch.clear();
        }
        requestJSON(searchAPI(keyword, page), resultSearch, photosListener);
    }

    public static void recent(int page, final PhotosListener photosListener) {
        if (page == 1) {
            resultRecent.clear();
        }
        requestJSON(recentAPI(page), resultRecent, photosListener);
    }

    public static ArrayList<Photo> getResult(FlickrManager.Type type) {
        switch (type) {
            case SEARCH: return resultSearch;
            case RECENT: return resultRecent;
        }
        return null;
    }

    private static String flickrAPI(String apiName, int page, String... params) {
        String url = EndPoint +
                     "?method=" + apiName +
                     "&api_key=" + ApiKey +
                     "&per_page=" + 20 +
                     "&page=" + page +
                     "&format=json" +
                     "&nojsoncallback=1";
        for (String param : params) {
            url += "&" + param;
        }

        return url;
    }

    private static String replaceWhiteSpaceWithPlus(String text) {
        String[] texts = text.split("\\s+");
        text = "";
        for (int i = 0; i < texts.length; i++) {
            if (i != 0) { text += "+"; }
            text += texts[i];
        }
        return text;
    }

    private static String searchAPI(String keyword, int page) {
        keyword = replaceWhiteSpaceWithPlus(keyword);
        return flickrAPI("flickr.photos.search", page, "text=" + keyword);
    }

    private static String recentAPI(int page) {
        return flickrAPI("flickr.photos.getRecent", page);
    }

    private static void requestJSON(String urlString, final ArrayList<Photo> resultData, final PhotosListener photosListener) {
        NetworkUtil.request(urlString, new NetworkUtil.RequestListener() {
            @Override
            public void onResult(String result) {
                LogUtil.debug(result);
                Photos photos = parseFlickrApiResult(result);
                resultData.addAll(photos.getPhotos());
                photosListener.get((ArrayList<Photo>) resultData.clone());
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

    public static class Photo implements Serializable {
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

package com.sample.myapplication;

import com.sample.myapplication.Utils.LogUtil;

import java.util.ArrayList;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.GET;
import retrofit2.http.Query;

interface IFlickrService {
    String EndPoint = "https://api.flickr.com/services/rest/";

    @GET("?method=flickr.photos.getRecent&format=json&nojsoncallback=1")
    Call<FlickrResponse> getRecent(@Query("api_key") String apiKey,
                                   @Query("per_page") int perPage,
                                   @Query("page") int page);
}

class FlickrResponse {
    FlickrPhotos photos;
    String stat;
}

class FlickrPhotos {
    public int                    page;
    public String                 pages;
    public int                    perpage;
    public String                 total;
    public ArrayList<FlickrPhoto> photo;

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

class FlickrPhoto {
    String id;
    String owner;
    String secret;
    String server;
    int    farm;
    String title;
    int    isPublic;
    int    isFriend;
    int    isFamily;
    String url_o;
    String url_m;

    public void dump() {
        LogUtil.dumpObject("Photo Entry", this);
    }
}

public class FlickrAPI {
    static final String ApiKey = "56550df01e50dba4228b82e187629d23";
    static final int PerPage = 20; // Max item num per request;

    public static FlickrAPI self;
    private static IFlickrService service;

    public FlickrAPI() {
        // for logging
        HttpLoggingInterceptor logging = new HttpLoggingInterceptor();
        logging.setLevel(HttpLoggingInterceptor.Level.BODY);

        OkHttpClient.Builder httpClient = new OkHttpClient.Builder();
        httpClient.addInterceptor(logging);

        Retrofit retrofit =
                new Retrofit.Builder()
                        .baseUrl(IFlickrService.EndPoint)
                        .addConverterFactory(GsonConverterFactory.create())
                        .client(httpClient.build()) // for logging
                        .build();

        service = retrofit.create(IFlickrService.class);
    }

    public static FlickrAPI getInstance() {
        if (self == null) { self = new FlickrAPI(); }
        return self;
    }

    public void getRecent(int page) {
        service.getRecent(ApiKey, PerPage, page)
               .enqueue(new Callback<FlickrResponse>() {
                   @Override
                   public void onResponse(Call<FlickrResponse> call, Response<FlickrResponse> response) {
                       LogUtil.traceFunc(response.body().stat);

                       FlickrPhotos photos = response.body().photos;
                       photos.dump();
                   }

                   @Override
                   public void onFailure(Call<FlickrResponse> call, Throwable t) {
                       LogUtil.traceFunc(call.toString());
                       LogUtil.error((Exception) t);
                   }
               });
    }
}

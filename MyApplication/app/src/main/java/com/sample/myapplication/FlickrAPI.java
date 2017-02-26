package com.sample.myapplication;

import com.google.gson.annotations.SerializedName;
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
    String CommonQueryParam = "format=json&nojsoncallback=1";

    @GET("?method=flickr.photos.getRecent" + "&" + CommonQueryParam)
    Call<FlickrResponse> getRecent(@Query("api_key") String apiKey,
                                   @Query("per_page") int perPage,
                                   @Query("page") int page);

    @GET("?method=flickr.photos.search" + "&" + CommonQueryParam)
    Call<FlickrResponse> search(@Query("api_key") String apiKey,
                                @Query("per_page") int perPage,
                                @Query("page") int page,
                                @Query("text") String text);
}

class FlickrResponse {
    FlickrPhotos photos;
    String       stat;
}

class FlickrPhotos {
    private int                    page;
    private String                 pages;
    private int                    perpage;
    private String                 total;
    private ArrayList<FlickrPhoto> photo;

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
}

class RetrofitUtil {
    private static OkHttpClient getHttpClient() {
        // for logging
        HttpLoggingInterceptor logging = new HttpLoggingInterceptor();
        logging.setLevel(HttpLoggingInterceptor.Level.BODY);

        OkHttpClient.Builder httpClient = new OkHttpClient.Builder();
        httpClient.addInterceptor(logging);

        return httpClient.build();
    }

    public static <T> T createJsonService(Class<T> target, String EndPoint) {

        Retrofit retrofit =
                new Retrofit.Builder()
                        .baseUrl(EndPoint)
                        .addConverterFactory(GsonConverterFactory.create())
                        .client(getHttpClient())
                        .build();

        return retrofit.create(target);
    }
}

public class FlickrAPI {
    static final String ApiKey    = "56550df01e50dba4228b82e187629d23";
    static final String ApiSecret = "b8e7d24b424bd775";
    static final String EndPoint  = "https://api.flickr.com/services/rest/";
    static final int    PerPage   = 3; // Max item num per request;

    public static  FlickrAPI      self;
    private static IFlickrService service;

    public FlickrAPI() {
        service = RetrofitUtil.createJsonService(IFlickrService.class, EndPoint);
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

    public void search(int page, String keyword) {
        service.search(ApiKey, PerPage, page, keyword)
               .enqueue(new Callback<FlickrResponse>() {
                   @Override
                   public void onResponse(Call<FlickrResponse> call, Response<FlickrResponse> response) {
                       LogUtil.traceFunc();
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

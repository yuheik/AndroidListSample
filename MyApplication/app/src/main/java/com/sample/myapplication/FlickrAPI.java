package com.sample.myapplication;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.google.gson.annotations.SerializedName;
import com.sample.myapplication.Utils.LogUtil;

import java.io.Serializable;
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

class FlickrPhoto implements Serializable {
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
}

class RetrofitUtil {
    /** wrap HttpLoggingInterceptor.Level enum */
    public enum LogLevel {
        NONE(HttpLoggingInterceptor.Level.NONE),
        BASIC(HttpLoggingInterceptor.Level.BASIC),
        HEADERS(HttpLoggingInterceptor.Level.HEADERS),
        BODY(HttpLoggingInterceptor.Level.BODY);

        private HttpLoggingInterceptor.Level level;

        LogLevel(HttpLoggingInterceptor.Level level) {
            this.level = level;
        }
    }

    private static OkHttpClient getHttpClient(LogLevel logLevel) {
        HttpLoggingInterceptor loggingInterceptor =
                new HttpLoggingInterceptor().setLevel(logLevel.level);

        return (new OkHttpClient.Builder())
                .addInterceptor(loggingInterceptor)
                .build();
    }

    public static <T> T createJsonService(Class<T> target, String endPoint) {
        return createJsonService(target, endPoint, LogLevel.NONE);
    }

    public static <T> T createJsonService(Class<T> target, String endPoint, LogLevel logLevel) {
        Retrofit retrofit =
                new Retrofit.Builder().baseUrl(endPoint)
                                      .addConverterFactory(GsonConverterFactory.create())
                                      .client(getHttpClient(logLevel))
                                      .build();

        return retrofit.create(target);
    }
}

public class FlickrAPI {
    static final String ApiKey    = "56550df01e50dba4228b82e187629d23";
    static final String ApiSecret = "b8e7d24b424bd775";
    static final String EndPoint  = "https://api.flickr.com/services/rest/";
    static final int    PerPage   = 20; // Max item num per request;

    public static  FlickrAPI      self;
    private static IFlickrService service;

    public FlickrAPI() {
        service = RetrofitUtil.createJsonService(IFlickrService.class, EndPoint);
    }

    public static FlickrAPI getInstance() {
        if (self == null) { self = new FlickrAPI(); }
        return self;
    }

    public interface Listener {
        void onResult(@Nullable FlickrPhotos flickrPhotos);
    }

    public void getRecent(int page, @NonNull final Listener listener) {
        service.getRecent(ApiKey, PerPage, page)
               .enqueue(new Callback<FlickrResponse>() {
                   @Override
                   public void onResponse(Call<FlickrResponse> call, Response<FlickrResponse> response) {
                       FlickrPhotos photos = response.body().photos;
                       listener.onResult(photos);
                   }

                   @Override
                   public void onFailure(Call<FlickrResponse> call, Throwable t) {
                       LogUtil.error((Exception) t);
                       listener.onResult(null);
                   }
               });
    }

    public void search(String keyword, int page, @NonNull final Listener listener) {
        service.search(ApiKey, PerPage, page, replaceWhiteSpaceWithPlus(keyword))
               .enqueue(new Callback<FlickrResponse>() {
                   @Override
                   public void onResponse(Call<FlickrResponse> call, Response<FlickrResponse> response) {
                       FlickrPhotos photos = response.body().photos;
                       listener.onResult(photos);
                   }

                   @Override
                   public void onFailure(Call<FlickrResponse> call, Throwable t) {
                       LogUtil.error((Exception) t);
                       listener.onResult(null);
                   }
               });
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
}

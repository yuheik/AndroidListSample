package com.sample.myapplication.Flickr;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.sample.myapplication.Utils.LogUtil;
import com.sample.myapplication.Utils.RetrofitUtil;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
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

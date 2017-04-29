package com.sample.myapplication.Utils;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class RetrofitUtil {
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

    private static OkHttpClient getHttpClient(RetrofitUtil.LogLevel logLevel) {
        HttpLoggingInterceptor loggingInterceptor =
                new HttpLoggingInterceptor().setLevel(logLevel.level);

        return (new OkHttpClient.Builder())
                .addInterceptor(loggingInterceptor)
                .build();
    }

    public static <T> T createJsonService(Class<T> target, String endPoint) {
        return createJsonService(target, endPoint, RetrofitUtil.LogLevel.NONE);
    }

    public static <T> T createJsonService(Class<T> target, String endPoint, RetrofitUtil.LogLevel logLevel) {
        Retrofit retrofit =
                new Retrofit.Builder().baseUrl(endPoint)
                                      .addConverterFactory(GsonConverterFactory.create())
                                      .client(getHttpClient(logLevel))
                                      .build();

        return retrofit.create(target);
    }
}

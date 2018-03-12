package com.kmo.drom.kmoappdrom.HTTP;

import android.util.Base64;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Response;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by kmoaz on 07.03.2018.
 */

public class Backend {
    private static volatile Backend instance;
    private final Retrofit retrofit;

    public static Retrofit getInstance() {
        Backend localInstance = instance;
        if (localInstance == null) {
            synchronized (Backend.class) {
                localInstance = instance;
                if (localInstance == null) {
                    instance = localInstance = new Backend("");
                }
            }
        }

        return localInstance.retrofit;
    }

    public static Retrofit getInstanceCredentials(String username, String password) {
        instance = new Backend(username + ":" + password);
        return instance.retrofit;
    }

    public static void setInstance() {
        instance = new Backend("");
    }

    private Backend(final String token) {
        HttpLoggingInterceptor interceptor = new HttpLoggingInterceptor();
        interceptor.setLevel(HttpLoggingInterceptor.Level.BODY);

        final String url = "https://api.github.com/";

        OkHttpClient.Builder okHttpClient = new OkHttpClient.Builder();
        okHttpClient.readTimeout(30, TimeUnit.SECONDS);
        okHttpClient.connectTimeout(15, TimeUnit.SECONDS);
        okHttpClient.retryOnConnectionFailure(true);
        okHttpClient.addInterceptor(interceptor);

        if (!token.isEmpty()) {
            okHttpClient.interceptors().add(new Interceptor() {
                @Override
                public Response intercept(Chain chain) throws IOException {
                    return chain.proceed(
                            chain.request().newBuilder()
                                    .addHeader("Authorization", "Basic " + Base64.encodeToString(token.getBytes("UTF-8"), Base64.NO_WRAP))
                                    .method(chain.request().method(), chain.request().body())
                                    .build()
                    );
                }
            });
        }

        retrofit = new Retrofit.Builder()
                .baseUrl(url)
                .client(okHttpClient.build())
                .addConverterFactory(GsonConverterFactory.create())
                .build();
    }
}

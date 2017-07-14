package com.example.ideo7.weather.API;

/**
 * Created by ideo7 on 14.07.2017.
 */

import com.example.ideo7.weather.API.AuthenticationInterceptor;
import com.facebook.stetho.okhttp3.StethoInterceptor;

import java.io.IOException;

import cz.msebera.android.httpclient.util.TextUtils;
import okhttp3.Credentials;
import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;


public class ServiceGenerator {

    public static final String API_BASE_URL = "http://api.openweathermap.org/data/2.5/";

    private static OkHttpClient.Builder httpClient = new OkHttpClient.Builder();

    private static Retrofit.Builder builder =
            new Retrofit.Builder()
                    .baseUrl(API_BASE_URL)
                    .client(httpClient.build())
                    .addConverterFactory(GsonConverterFactory.create());

    private static Retrofit retrofit = builder.build();

    public static <S> S createService(Class<S> serviceClass) {

        httpClient.addNetworkInterceptor(new StethoInterceptor());
        builder.client(httpClient.build());
        retrofit = builder.build();
        return retrofit.create(serviceClass);
    }






}
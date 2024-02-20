package com.suncode.horoscope.helper;

import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

import static com.suncode.horoscope.base.Constant.BASE_URL_HOROSCOPE;
import static com.suncode.horoscope.base.Constant.BASE_URL_IMAGE;

import com.suncode.horoscope.service.LoggingInterceptor;

public class ApiClient {
    public static Retrofit horoscopeBuild() {
        OkHttpClient client = new OkHttpClient.Builder()
                .addInterceptor(new LoggingInterceptor()) // Add logging interceptor here
                .build();

        Retrofit.Builder builder = new Retrofit.Builder()
                .baseUrl(BASE_URL_HOROSCOPE)
                .addConverterFactory(GsonConverterFactory.create())
                .client(client);

        return builder.build();
    }

    public static Retrofit imageBuild() {
        Retrofit.Builder builder = new Retrofit.Builder()
                .baseUrl(BASE_URL_IMAGE)
                .addConverterFactory(GsonConverterFactory.create());

        return builder.build();
    }
}

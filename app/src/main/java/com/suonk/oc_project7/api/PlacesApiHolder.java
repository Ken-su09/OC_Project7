package com.suonk.oc_project7.api;

import androidx.annotation.VisibleForTesting;

import okhttp3.HttpUrl;
import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class PlacesApiHolder {

    private static final String BASE_URL = "https://maps.googleapis.com/";

    public static PlacesApiService getInstance() {
        return getInstance(HttpUrl.get(BASE_URL));
    }

    @VisibleForTesting
    public static PlacesApiService getInstance(HttpUrl baseUrl) {
        HttpLoggingInterceptor interceptor = new HttpLoggingInterceptor();
        interceptor.setLevel(HttpLoggingInterceptor.Level.BODY);

        OkHttpClient client = new OkHttpClient.Builder()
                .addInterceptor(interceptor)
                .build();

        return new Retrofit.Builder()
                .baseUrl(baseUrl)
                .client(client)
                .addConverterFactory(GsonConverterFactory.create())
                .build()
                .create(PlacesApiService.class);
    }
}

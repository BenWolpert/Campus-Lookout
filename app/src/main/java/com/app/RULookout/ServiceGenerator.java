package com.app.RULookout;

import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by TRoc9 on 8/10/2017.
 */

public class ServiceGenerator {
    public static final String API_BASE_URL = "http://10.0.2.2:3000";




    private static Retrofit retrofit;

    private static Retrofit.Builder builder =
            new Retrofit.Builder()
            .addConverterFactory(GsonConverterFactory.create())
            .baseUrl(API_BASE_URL);

    private static OkHttpClient.Builder httpClient =
            new OkHttpClient.Builder();


    private ServiceGenerator() {
    }

}



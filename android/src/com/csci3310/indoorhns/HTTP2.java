package com.csci3310.indoorhns;

import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import retrofit2.Call;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.Body;
import retrofit2.http.POST;

/**
 * Created by ray on 18/5/2017.
 */

public interface HTTP2 {

    String theURL ="https://ml.internalpositioning.com";

    @POST("/track")
    Call<FindApiTrackResponse> track(
        @Body FindApiTrackRequest findApiTrackRequest
    );


    OkHttpClient okHttpClient = new OkHttpClient.Builder()
            .readTimeout(10, TimeUnit.SECONDS)
            .connectTimeout(10, TimeUnit.SECONDS)
            .build();

    Retrofit retrofit = new Retrofit.Builder()
            .baseUrl(theURL)
            .client(okHttpClient)
            .addConverterFactory(GsonConverterFactory.create())
            .build();

}

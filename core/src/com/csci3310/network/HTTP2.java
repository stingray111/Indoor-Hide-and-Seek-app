package com.csci3310.network;

import com.csci3310.network.model.FindApiTrackRequest;
import com.csci3310.network.model.FindApiTrackResponse;

import okhttp3.OkHttpClient;
import java.util.concurrent.TimeUnit;

import retrofit2.Call;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;

/**
 * Created by Igho on 17/5/2017.
 */

public interface HTTP2 {
    @POST("/track")
    Call<FindApiTrackResponse> track(
            @Body FindApiTrackRequest findApiTrackRequest
    );

    String theURL ="https://ml.internalpositioning.com";

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

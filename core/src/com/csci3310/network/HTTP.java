package com.csci3310.network;

import com.csci3310.network.model.CreateRoomRequest;
import com.csci3310.network.model.JoinRoomRequest;
import com.csci3310.network.model.LocationLabel;
import com.csci3310.network.model.PushLocationLabelRequest;
import com.csci3310.network.model.RoomId;
import com.csci3310.network.model.StartGameResponse;
import com.csci3310.network.model.Success;
import com.oracle.webservices.internal.api.message.PropertySet;

import java.util.List;
import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import retrofit2.Call;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.Body;
import retrofit2.http.POST;

public interface HTTP {

    String theURL ="https://stingray.space:3001";

    @POST("/createRoom")
    Call<RoomId> createRoom(
        @Body CreateRoomRequest createRoomRequest
    );

    @POST("/joinRoom")
    Call<Success> joinRoom(
        @Body JoinRoomRequest joinRoomRequest
    );

    @POST("/getLocationLabelList")
    Call<List<LocationLabel>> getLocationLabelList(
            @Body RoomId roomId
    );

    @POST("/startGame")
    Call<StartGameResponse> startGame(
            @Body RoomId roomId
    );

    @POST("/pushLocationLabel")
    Call<PushLocationLabelRequest> pushLocationLabel(
            @Body Success success;
    );

    final OkHttpClient okHttpClient = new OkHttpClient.Builder()
            .readTimeout(10, TimeUnit.SECONDS)
            .connectTimeout(10, TimeUnit.SECONDS)
            //.sslSocketFactory(SSL.getNewSSL(),SSL.getTm())
            .build();

    Retrofit retrofit = new Retrofit.Builder()
            .baseUrl(theURL)
            .client(okHttpClient)
            .addConverterFactory(GsonConverterFactory.create())
            .build();
}

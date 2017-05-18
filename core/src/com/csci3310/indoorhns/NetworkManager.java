package com.csci3310.indoorhns;

import com.badlogic.gdx.Gdx;
import com.csci3310.network.HTTP;
import com.csci3310.network.model.CreateRoomRequest;
import com.csci3310.network.model.JoinRoomRequest;
import com.csci3310.network.model.RoomId;
import com.csci3310.network.model.Success;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

import okhttp3.Request;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


/**
 * Created by Edmund on 5/14/2017.
 */

public class NetworkManager {

    public static String serverError = "Server Error";
    public static String networkUnreachable = "Network Unreachable";

    final static public String server = "http://localhost/";

    public void joinRoom(final int roomId, final String playerName, final NetworkTaskFinishListener listener){
        //TO-DO join room network IO
        new Thread(new Runnable() {
            @Override
            public void run() {
                final HTTP httpService = HTTP.retrofit.create(HTTP.class);
                Callback<Success> joinRoomCallBack = new Callback<Success>() {
                    @Override
                    public void onResponse(Call<Success> call, Response<Success> response) {
                        if(!response.isSuccessful()){
                            listener.onJoinRoomFail(serverError);
                        }
                        else if(!response.body().isSuccess()){
                            listener.onJoinRoomFail("Room not exist");
                        }else {
                            listener.onJoinRoomSuccess();
                        }
                    }

                    @Override
                    public void onFailure(Call<Success> call, Throwable t) {
                        listener.onJoinRoomFail(networkUnreachable);
                    }
                };
                Call<Success> joinRoomRequestCall = httpService.joinRoom(new JoinRoomRequest(roomId,playerName);
                joinRoomRequestCall.enqueue(joinRoomCallBack);
            }
        }).start();
    }

    public void createRoom(final String playerName,final NetworkTaskFinishListener listener){
        //TO-DO create room network IO
        new Thread(new Runnable() {
            @Override
            public void run() {

                final HTTP httpService = HTTP.retrofit.create(HTTP.class);
                Callback<RoomId> createRoomCallBack = new Callback<RoomId>(){
                    @Override
                    public void onResponse(Call<RoomId> call, Response<RoomId> response) {
                        if(!response.isSuccessful()){
                            listener.onJoinRoomFail(serverError);
                        }else {
                            listener.onCreateRoomSuccess(response.body().getRoomid());
                        }
                    }

                    @Override
                    public void onFailure(Call<RoomId> call, Throwable t) {
                        listener.onJoinRoomFail(networkUnreachable);
                    }
                };
                Call<RoomId> createRoomCall = httpService.createRoom(new CreateRoomRequest(playerName));
                createRoomCall.enqueue(createRoomCallBack);
            }
        }).start();
    }

    public void leaveRoom(int roomId){
        //TO-DO leave room network IO

        //finally
        // None, UI will leave waiting room without waiting for server response
    }

    public void startPlayerListPolling(final WaitingRoomScreen waitingRoom, final NetworkTaskFinishListener listener){
        new Thread(new Runnable(){
            @Override
            public void run() {
                HashMap<String, Player> playerMap = waitingRoom.getPlayerMap();
                Player me = waitingRoom.getMe();

                int i=0;
                while(waitingRoom.getPlayerListUpdatePollingTrigger()){
//                    if(i == 0){
//                        playerMap.put("testing0", new Player(Player.Type.Hunter, "Hunter 0", "testing0"));
//                        i++;
//                    }else if(i == 1){
//                        playerMap.remove("testing0");
//                        i++;
//                    }else if(i == 2){
//                        playerMap.put("testing0", new Player(Player.Type.Hunter, "Hunter 0", "testing0"));
//                        i++;
//                    }else if(i == 3){
//                        playerMap.put("testing1", new Player(Player.Type.Hunter, "Hunter 1", "testing1"));
//                        i++;
//                    }else{
//                        playerMap.remove("testing0");
//                        playerMap.remove("testing1");
//                        i = 0;
//                    }
                    if(i == 0) {
                        playerMap.put("testing0", new Player(Player.Type.Hunter, "Hunter 0", "testing0"));
                        playerMap.put("testing1", new Player(Player.Type.Hunter, "Hunter 1", "testing1"));
                        playerMap.put("testing2", new Player(Player.Type.Hunter, "Hunter 2", "testing2"));
                    }else if(i == 5){
                        playerMap.remove("testing0");
                    }else if(i == 7){
                        i = -1;
                    }
                    listener.onPlayerListUpdate();
                    i++;
                    try {
                        Thread.sleep(2000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
                System.out.println("PlayerList Polling Thread Ended");
            }
        }).start();
    }

    static class NetworkTaskFinishListener implements JoinRoomSuccessListener, JoinRoomFailListener, CreateRoomSuccessListener, CreateRoomFailListener, PlayerListUpdateListener {
        @Override
        public void onJoinRoomSuccess() {}
        @Override
        public void onCreateRoomSuccess(int roomId) {}
        @Override
        public void onJoinRoomFail(String response) {}
        @Override
        public void onCreateRoomFail(String response) {}
        @Override
        public void onPlayerListUpdate() {}
    }

    interface JoinRoomSuccessListener {
        void onJoinRoomSuccess();
    }
    interface JoinRoomFailListener {
        void onJoinRoomFail(String response);
    }
    interface CreateRoomSuccessListener {
        void onCreateRoomSuccess(int roomId);
    }
    interface CreateRoomFailListener {
        void onCreateRoomFail(String response);
    }
    interface PlayerListUpdateListener {
        void onPlayerListUpdate();
    }

}

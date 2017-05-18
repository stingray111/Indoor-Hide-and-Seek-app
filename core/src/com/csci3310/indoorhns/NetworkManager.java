package com.csci3310.indoorhns;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.utils.StringBuilder;
import com.csci3310.network.HTTP;
import com.csci3310.network.model.CreateRoomRequest;
import com.csci3310.network.model.GameStartCheckResponse;
import com.csci3310.network.model.JoinRoomRequest;
import com.csci3310.network.model.RoomId;
import com.csci3310.network.model.Success;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

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
                Call<Success> joinRoomRequestCall = httpService.joinRoom(new JoinRoomRequest(roomId,playerName));
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

    public void endGame(int roomId){
        // victim being caught, end game

    }


    public void startPlayerLocationPolling(final GameScreen gameScreen, NetworkTaskFinishListener listener){
        new Thread(new Runnable() {
            @Override
            public void run() {
                gameScreen.getMainGame().getAndroidConnector().getCoordinator().getWifiScanReceiverLocation();
            }
        });
    }


    public void startPlayerListPolling(final WaitingRoomScreen waitingRoom, final NetworkTaskFinishListener listener){
        new Thread(new Runnable(){
            @Override
            public void run() {

                final HashMap<String, Player> playerMap = waitingRoom.getPlayerMap();
                final Player me = waitingRoom.getMe();

                //use
                //waitingRoom.getRoomId();
                //get
                //playerMap.put("testing0", new Player(Player.Type.Hunter, "Hunter 0", "testing0"));
                //listener.onPlayerListUpdate();

                final HTTP httpService = HTTP.retrofit.create(HTTP.class);

                final Callback<GameStartCheckResponse> gameStartCheckResponseCallback = new Callback<GameStartCheckResponse>() {
                    @Override
                    public void onResponse(Call<GameStartCheckResponse> call, Response<GameStartCheckResponse> response) {
                        if(!response.isSuccessful()){
                            //server error
                            call.enqueue(this);
                        }
                        else {
                            //Success
                            List<GameStartCheckResponse.Player> playerList = response.body().getPlayerList();
                            String victim = response.body().getVictim();
                            playerMap.clear();
                            playerMap.put(me.getAndroidID(),me);

                            for (GameStartCheckResponse.Player player:playerList ) {
                                if(player.uuid != me.getAndroidID()) {
                                    if (victim.equals(player.uuid)){
                                        playerMap.put(player.uuid,new Player(Player.Type.Huntee,player.playerName,player.uuid));
                                    }
                                    else{
                                        playerMap.put(player.uuid,new Player(Player.Type.Hunter,player.playerName,player.uuid));
                                    }
                                }
                            }
                            listener.onPlayerListUpdate();
                            if(waitingRoom.getPlayerListUpdatePollingTrigger()) {
                                call.enqueue(this);
                            }
                        }
                    }

                    @Override
                    public void onFailure(Call<GameStartCheckResponse> call, Throwable t) {
                        //call again
                        call.enqueue(this);
                    }
                };
                final Call<GameStartCheckResponse>  gameStartCheckResponseCall= httpService.startGameCheck(new RoomId(waitingRoom.getRoomId()));
                gameStartCheckResponseCall.enqueue(gameStartCheckResponseCallback);
            }
        }).start();
    }

    static class NetworkTaskFinishListener implements JoinRoomSuccessListener, JoinRoomFailListener, CreateRoomSuccessListener, CreateRoomFailListener, PlayerListUpdateListener ,PlayerLocationUpdateListener{
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
        @Override
        public void onPlayerLocationUpdate(HashMap<String,String> playerPointMap) {}
        @Override
        public void onEndGame(){}
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
    interface PlayerLocationUpdateListener{
        void onPlayerLocationUpdate(HashMap<String,String> playerPointMap);
        void onEndGame();
    }

}

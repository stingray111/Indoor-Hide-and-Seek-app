package com.csci3310.indoorhns;

import com.badlogic.gdx.Gdx;

import java.util.ArrayList;
import java.util.HashMap;


/**
 * Created by Edmund on 5/14/2017.
 */

public class NetworkManager {

    final static public String server = "http://localhost/";

    public void joinRoom(String roomId, final NetworkTaskFinishListener listener){
        //TO-DO join room network IO
        new Thread(new Runnable() {
            @Override
            public void run() {
                boolean success = false;


                // pretend waiting for network response
                try {
                    Thread.sleep(2000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

                // finally call codes below to inform ui
                if(success) {
                    listener.onJoinRoomSuccess();
                }else{
                    listener.onJoinRoomFail("I don't know why");
                }
            }
        }).start();
    }

    public void createRoom(final NetworkTaskFinishListener listener){
        //TO-DO create room network IO
        new Thread(new Runnable() {
            @Override
            public void run() {
                boolean success = true;


                // pretend waiting for network response
                try {
                    Thread.sleep(2000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

                //finally call codes below to inform ui
                if(success) {
                    listener.onCreateRoomSuccess("ROOM ID");
                }else{
                    listener.onJoinRoomFail("I don't know why");
                }
            }
        }).start();
    }

    public void leaveRoom(String roomId){
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
        public void onCreateRoomSuccess(String roomId) {}
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
        void onCreateRoomSuccess(String roomId);
    }
    interface CreateRoomFailListener {
        void onCreateRoomFail(String response);
    }
    interface PlayerListUpdateListener {
        void onPlayerListUpdate();
    }

}

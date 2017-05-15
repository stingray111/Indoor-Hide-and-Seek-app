package com.csci3310.indoorhns;

import com.badlogic.gdx.Gdx;

/**
 * Created by Edmund on 5/14/2017.
 */

public class NetworkManager {

    final static public String server = "http://localhost/";

    public void joinRoom(String roomId, final NetworkTaskFinishListener listener){
        //TO-DO join room network IO



        // finally call codes below to inform ui
        Gdx.app.postRunnable(new Runnable() {
            @Override
            public void run() {
                listener.onJoinRoomFinish();
            }
        });
    }

    public void createRoom(final NetworkTaskFinishListener listener){
        //TO-DO create room network IO


        //finally call codes below to inform ui
        Gdx.app.postRunnable(new Runnable() {
            @Override
            public void run() {
                listener.onCreateRoomFinish("ROOM ID");
            }
        });
    }

    public void leaveRoom(){
        //TO-DO leave room network IO

        //finally
        // None, UI will leave waiting room without waiting for server response
    }

    static public class NetworkTaskFinishListener implements JoinRoomFinishListener, CreateRoomFinishListener{
        @Override
        public void onJoinRoomFinish() {}
        @Override
        public void onCreateRoomFinish(String roomId) {}
    }

    public interface JoinRoomFinishListener {
        void onJoinRoomFinish();
    }

    public interface CreateRoomFinishListener {
        void onCreateRoomFinish(String roomId);
    }
}

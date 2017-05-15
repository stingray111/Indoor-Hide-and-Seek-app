package com.csci3310.indoorhns;

import com.badlogic.gdx.Gdx;

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


                // pretend waiting for network response
                try {
                    Thread.sleep(2000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

                // finally call codes below to inform ui
                Gdx.app.postRunnable(new Runnable() {
                    @Override
                    public void run() {
                        listener.onJoinRoomFinish();
                    }
                });
            }
        }).start();
    }

    public void createRoom(final NetworkTaskFinishListener listener){
        //TO-DO create room network IO
        new Thread(new Runnable() {
            @Override
            public void run() {


                // pretend waiting for network response
                try {
                    Thread.sleep(2000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

                //finally call codes below to inform ui
                Gdx.app.postRunnable(new Runnable() {
                    @Override
                    public void run() {
                        listener.onCreateRoomFinish("ROOM ID");
                    }
                });
            }
        }).start();
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

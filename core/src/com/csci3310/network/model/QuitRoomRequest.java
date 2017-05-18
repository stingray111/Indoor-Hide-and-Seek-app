package com.csci3310.network.model;

/**
 * Created by ray on 18/5/2017.
 */

public class QuitRoomRequest {
    public int roomId;
    public String uuid;
    public QuitRoomRequest(int roomId, String uuid){
        this.roomId = roomId;
        this.uuid = uuid;
    }
}

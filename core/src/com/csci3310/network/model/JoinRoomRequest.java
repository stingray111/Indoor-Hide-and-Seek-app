package com.csci3310.network.model;

import java.util.UUID;

/**
 * Created by ray on 17/5/2017.
 */

public class JoinRoomRequest {
    public int roomId;
    public String uuid;
    public String playerName;

    public JoinRoomRequest(int roomId, String playerName){
        this.playerName = playerName;
        this.uuid = UUID.randomUUID().toString();
        this.roomId = roomId;
    }
}

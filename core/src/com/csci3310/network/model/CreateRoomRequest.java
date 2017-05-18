package com.csci3310.network.model;

import java.util.UUID;

/**
 * Created by ray on 17/5/2017.
 */

public class CreateRoomRequest {
    public String uuid;
    public String playerName;

    public CreateRoomRequest(String playerName,String uuid){
        this.uuid = uuid;
        this.playerName = playerName;
    }

    public String getUuid() {
        return uuid;
    }

    public void setUuid(String uuid) {
        this.uuid = uuid;
    }

    public String getPlayerName() {
        return playerName;
    }
}

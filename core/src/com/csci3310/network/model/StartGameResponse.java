package com.csci3310.network.model;

import java.util.List;

/**
 * Created by ray on 17/5/2017.
 */

public class StartGameResponse {
    private boolean success;
    private List<Player> playerList;

    public List<Player> getPlayerList() {
        return playerList;
    }

    public boolean isSuccess() {
        return success;
    }

    private class Player{
        public String uuid;
        public String locationLabel;
        public Player(String uuid, String locationLabel){
            this.locationLabel = locationLabel;
            this.uuid = uuid;
        }
    }
}


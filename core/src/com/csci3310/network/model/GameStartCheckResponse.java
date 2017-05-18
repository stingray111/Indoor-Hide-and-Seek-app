package com.csci3310.network.model;

import java.util.List;

/**
 * Created by ray on 18/5/2017.
 */

public class GameStartCheckResponse {
    private String victim;
    private List<Player> playerList;

    public String getVictim() {
        return victim;
    }

    public List<Player> getPlayerList() {
        return playerList;
    }

    public class Player{
        public String uuid;
        public String playerName;
    }
}

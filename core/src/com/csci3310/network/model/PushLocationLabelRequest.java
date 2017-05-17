package com.csci3310.network.model;

/**
 * Created by ray on 17/5/2017.
 */

public class PushLocationLabelRequest {
    private int roomId;
    private String locationLabel;
    private String uuid;
    public PushLocationLabelRequest(int roomId,String locationLabel,String uuid){
        this.roomId = roomId;
        this.locationLabel = locationLabel;
        this.uuid = uuid;
    }

    public String getLocationLabel() {
        return locationLabel;
    }

    public String getUuid() {
        return uuid;
    }

    public int getRoomId() {
        return roomId;
    }
}

package com.csci3310.network;

import com.csci3310.network.model.LocationLabel;

/**
 * Created by ray on 18/5/2017.
 */

public class LocationLabelExchangeRequest {
    public int roomId;
    public String locationLabel;
    public String uuid;

    public LocationLabelExchangeRequest(int roomId,String uuid,String locationLabel){
        this.roomId = roomId;
        this.uuid = uuid;
        this.locationLabel = locationLabel;
    }

}

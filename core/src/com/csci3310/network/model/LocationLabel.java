package com.csci3310.network.model;

/**
 * Created by ray on 17/5/2017.
 */

public class LocationLabel {
    public String uuid;
    public String locationLabel;
    public LocationLabel(String uuid,String locationLabel){
        this.uuid = uuid;
        this.locationLabel = locationLabel;
    }

    public String getUuid() {
        return uuid;
    }

    public String getLocationLabel() {
        return locationLabel;
    }
}

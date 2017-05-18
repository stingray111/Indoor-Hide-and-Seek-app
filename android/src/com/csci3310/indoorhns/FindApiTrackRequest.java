package com.csci3310.indoorhns;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by ray on 18/5/2017.
 */

public class FindApiTrackRequest {
    public String group;
    public String username;
    public String location;
    public long time;

    @SerializedName("wifi-fingerprint")
    public List<WifiSignal> wifi_fingerprint;

    public FindApiTrackRequest(String username, List<WifiSignal> wifiSignalList){
        this.group = "hideandseekinshb";
        this.location = "tracking";
        this.time = System.currentTimeMillis();
        this.username = username;
        this. wifi_fingerprint = wifiSignalList;
    }
}



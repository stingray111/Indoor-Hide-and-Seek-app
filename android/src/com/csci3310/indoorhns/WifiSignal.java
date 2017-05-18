package com.csci3310.indoorhns;

/**
 * Created by ray on 18/5/2017.
 */

public class WifiSignal {

    public String mac;
    public int rssi;
    public WifiSignal(String mac,int rssi){
        this.mac = mac;
        this.rssi = rssi;
    }
}

package com.csci3310.indoorhns;

/**
 * Created by Igho on 17/5/2017.
 */



public class WifiFingerprint {
    public String macAddress;
    public int rssi;

    public WifiFingerprint(String macAddress, int rssi){
        this.macAddress = macAddress;
        this.rssi = rssi;
    }
}
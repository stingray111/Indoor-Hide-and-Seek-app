package com.csci3310.indoorhns;

import android.annotation.SuppressLint;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.wifi.ScanResult;
import android.net.wifi.WifiManager;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Igho on 17/5/2017.
 */

public class WifiScanReceiver extends BroadcastReceiver {

    private WifiManager wifiManager;
    private ArrayList<WifiFingerprint> resultList;
    private boolean keepScanning;
    private boolean scanning;

    public WifiScanReceiver(Context context) {
        wifiManager = (WifiManager) context.getSystemService (Context.WIFI_SERVICE);
        this.resultList = new ArrayList<WifiFingerprint>();
        this.keepScanning = false;
        this.scanning = false;
    }

    public boolean isScanning(){return scanning;}

    public void startScan(){
        this.scanning = true;
        wifiManager.startScan();
    }

    public void setKeepScanning(boolean bool){
        this.keepScanning = bool;
    }

    public ArrayList<WifiFingerprint> getResultList(){
        return resultList;
    }

    @SuppressLint("UseValueOf")
    public void onReceive(Context c, Intent intent) {
        List<ScanResult> scanReusltList = wifiManager.getScanResults();
        System.out.println("OnReceive: "+scanReusltList.size());
        this.resultList.clear(); //add this
        for (int i = 0; i < scanReusltList.size(); i++) {
            ScanResult result = scanReusltList.get(i);
            //use add here:
            System.out.println(result.toString());
            resultList.add(new WifiFingerprint(result.BSSID, result.level)); //append to the other data
        }
        if(keepScanning) {
            wifiManager.startScan(); //start a new scan to update values faster
        }else{
            scanning = false;
        }
    }
}

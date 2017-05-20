package com.csci3310.indoorhns;

import android.annotation.SuppressLint;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.wifi.ScanResult;
import android.net.wifi.WifiManager;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by Igho on 17/5/2017.
 */

public class WifiScanReceiver extends BroadcastReceiver {

    private WifiManager wifiManager;
    private String uuid;
    private ArrayList<WifiFingerprint> resultList;
    private boolean keepScanning;
    private boolean scanning;
    public volatile static String location = "09r3";

    public WifiScanReceiver(Context context) {
        wifiManager = (WifiManager) context.getSystemService (Context.WIFI_SERVICE);
        this.resultList = new ArrayList<WifiFingerprint>();
        this.keepScanning = false;
        this.scanning = false;
        this.uuid = ((IndoorHnSLauncher)context).getAndroidId();
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
        final List<ScanResult> scanReusltList = wifiManager.getScanResults();

        new Thread(new Runnable() {
            @Override
            public void run() {
                final HTTP2 httpService = HTTP2.retrofit.create(HTTP2.class);
                Callback<FindApiTrackResponse> findApiTrackResponseCallback = new Callback<FindApiTrackResponse>() {
                    @Override
                    public void onResponse(Call<FindApiTrackResponse> call, Response<FindApiTrackResponse> response) {
                        if(response.isSuccessful()) {
                            System.out.println("Wifi: " + response.body().location);
                            WifiScanReceiver.location = response.body().location;
                        }else{
                            System.out.println("Wifi: response 500");
                        }
                    }

                    @Override
                    public void onFailure(Call<FindApiTrackResponse> call, Throwable t) {
                        System.out.println("Wifi: on failure");
                    }
                };
                List<WifiSignal> wifiSignals = new ArrayList<>();
                for (ScanResult r:scanReusltList ) {
                    wifiSignals.add(new WifiSignal(r.BSSID,r.level));
                }
                FindApiTrackRequest request = new FindApiTrackRequest(uuid,wifiSignals);
                Call<FindApiTrackResponse> findApiTrackResponseCall = httpService.track(request);
                findApiTrackResponseCall.enqueue(findApiTrackResponseCallback);
            }
        }).start();

        if(keepScanning) {
            wifiManager.startScan(); //start a new scan to update values faster
        }else{
            scanning = false;
        }
    }
}

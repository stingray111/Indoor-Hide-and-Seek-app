package com.csci3310.indoorhns;

import java.util.ArrayList;

/**
 * Created by Edmund on 5/8/2017.
 */

public class AndroidConnector{

    private AndroidConnectorCoordinator coordinator;

    public AndroidConnector(AndroidConnectorCoordinator coordinator){
        this.coordinator = coordinator;
    }

    public AndroidConnectorCoordinator getCoordinator(){
        return coordinator;
    }

    public interface AndroidConnectorCoordinator {
        void test();
        String getAndroidId();
        void showToast(String message);
        void startWifiScan();
        void stopWifiScan();
        ArrayList<WifiFingerprint> getWifiScanResult();
    }
}

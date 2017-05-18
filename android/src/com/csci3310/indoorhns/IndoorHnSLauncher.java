package com.csci3310.indoorhns;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.location.LocationManager;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.widget.Toast;

import com.badlogic.gdx.backends.android.AndroidApplication;
import com.badlogic.gdx.backends.android.AndroidApplicationConfiguration;

import java.util.ArrayList;

public class IndoorHnSLauncher extends AndroidApplication implements AndroidConnector.AndroidConnectorCoordinator {
	final Context context = this;
    private static final int PERMISSION_REQUEST_CODE =23143;
	IndoorHideAndSeek game;
	WifiScanReceiver wifiScanReceiver;
	private LocationManager mLocationManager;

	@Override
	protected void onCreate (Bundle savedInstanceState) {
        /*
		mLocationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
		if(!validPermission()){
			getPermission();
		}else{
        }
        */
        //init();


        super.onCreate(savedInstanceState);

        if(!validPermission()){
            getPermission();
        }else {
            wifiInit();
        }
        AndroidConnector connector = new AndroidConnector(this);
        AndroidApplicationConfiguration config = new AndroidApplicationConfiguration();
        game = new IndoorHideAndSeek(connector);
        initialize(game, config);
	}

	private void wifiInit(){
        wifiScanReceiver = new WifiScanReceiver(this);
        registerReceiver(wifiScanReceiver, new IntentFilter(WifiManager.SCAN_RESULTS_AVAILABLE_ACTION));
    }


	@Override
	public void test() {
		System.out.println("Test");
	}

	@Override
	public String getAndroidId() {
		return Settings.Secure.getString(getContentResolver(), Settings.Secure.ANDROID_ID);
	}

    @Override
    public void showToast(final String message) {
		runOnUiThread(new Runnable() {
			@Override
			public void run() {
				Toast.makeText(context, message, Toast.LENGTH_LONG).show();

			}
		});
    }

    @Override
    public void startWifiScan(){
		if(!wifiScanReceiver.isScanning()) {
			wifiScanReceiver.setKeepScanning(true);
			wifiScanReceiver.startScan();
		}
	}

	@Override
	public void stopWifiScan(){
		wifiScanReceiver.setKeepScanning(false);
	}

	@Override
	public ArrayList<WifiFingerprint> getWifiScanResult(){
		return wifiScanReceiver.getResultList();
	}

	@Override
	public String getWifiScanReceiverLocation() {
		return wifiScanReceiver.location;
	}

	@Override
    protected void onDestroy() {
        //unregisterReceiver(wifiScanReceiver);
        super.onDestroy();
    }

	public void getPermission(){
		String[] permissions = new String[]{Manifest.permission.ACCESS_FINE_LOCATION,Manifest.permission.ACCESS_COARSE_LOCATION};
		requestPermissions(permissions,PERMISSION_REQUEST_CODE);
	}

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
         if(requestCode == PERMISSION_REQUEST_CODE){
            if(!validPermission()){
                finish();
            }else{
                wifiInit();
            }
        }
    }

    public boolean validPermission(){
		if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED &&
                ActivityCompat.checkSelfPermission(this,Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
			return true;
		} else{
			return false;
		}

	}


}

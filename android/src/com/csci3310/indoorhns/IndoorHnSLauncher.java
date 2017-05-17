package com.csci3310.indoorhns;

import android.content.Context;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.provider.Settings;
import android.widget.Toast;

import com.badlogic.gdx.backends.android.AndroidApplication;
import com.badlogic.gdx.backends.android.AndroidApplicationConfiguration;

import java.util.ArrayList;

public class IndoorHnSLauncher extends AndroidApplication implements AndroidConnector.AndroidConnectorCoordinator {
	final Context context = this;
	IndoorHideAndSeek game;
	WifiScanReceiver wifiScanReceiver;

	@Override
	protected void onCreate (Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		AndroidConnector connector = new AndroidConnector(this);
		wifiScanReceiver = new WifiScanReceiver(this);
		AndroidApplicationConfiguration config = new AndroidApplicationConfiguration();
		game = new IndoorHideAndSeek(connector);
		initialize(game, config);
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
		System.out.println("Wifi Start Scanning");
	}

	@Override
	public void stopWifiScan(){
		wifiScanReceiver.setKeepScanning(false);
	}

	@Override
	public ArrayList<WifiFingerprint> getWifiScanResult(){
		return wifiScanReceiver.getResultList();
	}


}

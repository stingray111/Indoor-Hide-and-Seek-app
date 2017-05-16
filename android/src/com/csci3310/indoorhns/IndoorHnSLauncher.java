package com.csci3310.indoorhns;

import android.content.Context;
import android.os.Bundle;
import android.provider.Settings;
import android.widget.Toast;

import com.badlogic.gdx.backends.android.AndroidApplication;
import com.badlogic.gdx.backends.android.AndroidApplicationConfiguration;

public class IndoorHnSLauncher extends AndroidApplication implements AndroidConnector.AndroidConnectorCoordinator {
	final Context context = this;
	IndoorHideAndSeek game;

	@Override
	protected void onCreate (Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		AndroidConnector connector = new AndroidConnector(this);
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


}

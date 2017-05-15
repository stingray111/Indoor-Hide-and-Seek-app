package com.csci3310.indoorhns;

import android.os.Bundle;

import com.badlogic.gdx.backends.android.AndroidApplication;
import com.badlogic.gdx.backends.android.AndroidApplicationConfiguration;

public class IndoorHnSLauncher extends AndroidApplication implements AndroidConnector.AndroidConnectorRequestListener {

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
	public void onTestRequest() {
		System.out.println("Test");
	}
}

package com.csci3310.indoorhns;

import android.app.Service;
import android.content.Context;
import android.os.Bundle;
import android.view.inputmethod.InputMethod;
import android.view.inputmethod.InputMethodInfo;
import android.view.inputmethod.InputMethodManager;

import com.badlogic.gdx.backends.android.AndroidApplication;
import com.badlogic.gdx.backends.android.AndroidApplicationConfiguration;
import com.csci3310.indoorhns.IndoorHideAndSeek;

import java.util.List;

public class IndoorHnSLauncher extends AndroidApplication implements AndroidConnectorRequestListener {

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

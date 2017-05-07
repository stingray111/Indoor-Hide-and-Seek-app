package com.csci3310.indoorhns;

import android.os.Bundle;

import com.badlogic.gdx.backends.android.AndroidApplication;
import com.badlogic.gdx.backends.android.AndroidApplicationConfiguration;
import com.csci3310.indoorhns.IndoorHideAndSeek;

public class AndroidLauncher extends AndroidApplication {

	IndoorHideAndSeek game;

	@Override
	protected void onCreate (Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		AndroidApplicationConfiguration config = new AndroidApplicationConfiguration();
		game = new IndoorHideAndSeek();
		initialize(game, config);
	}
}

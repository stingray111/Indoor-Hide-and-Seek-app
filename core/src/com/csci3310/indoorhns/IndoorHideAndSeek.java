package com.csci3310.indoorhns;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.ScreenAdapter;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;


public class IndoorHideAndSeek extends Game {

	public AndroidConnector connector;
	public NetworkManager networkManager;
	public ScreenManager screenManager;

	public IndoorHideAndSeek(AndroidConnector connector){
		this.connector = connector;
		this.networkManager = new NetworkManager();
		this.screenManager = new ScreenManager(this);
	}

	public NetworkManager getNetworkManager(){return this.networkManager;}
	public ScreenManager getScreenManager(){return this.screenManager;}

	@Override
	public void create () {
		// more setting and init here
		Gdx.input.setCatchBackKey(true);
		Gdx.input.setCatchMenuKey(true);
		this.setScreen(new ScreenManager.BlackScreen());
		screenManager.transitToWelcomeScreen();
	}
}

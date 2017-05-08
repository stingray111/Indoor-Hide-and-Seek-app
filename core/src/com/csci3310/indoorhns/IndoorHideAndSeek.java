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

	static public class BlackScreen implements Screen {

		public BlackScreen(){

		}

		@Override
		public void show() {

		}

		@Override
		public void render(float delta) {
			Gdx.gl.glClearColor(0, 0, 0, 1);
			Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
		}

		@Override
		public void resize(int width, int height) {

		}

		@Override
		public void pause() {

		}

		@Override
		public void resume() {

		}

		@Override
		public void hide() {

		}

		@Override
		public void dispose() {

		}
	}

	public IndoorHideAndSeek(AndroidConnector connector){
		this.connector = connector;
	}

	@Override
	public void create () {
		// more setting and init here
		Gdx.input.setCatchBackKey(true);
		Gdx.input.setCatchMenuKey(true);
		this.setScreen(new TransitionScreen(this, new BlackScreen(), WelcomeScreen.class));
	}
}

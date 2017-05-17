package com.csci3310.indoorhns;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

import java.util.HashMap;

/**
 * Created by Edmund on 5/14/2017.
 */

public class ScreenManager {
    private IndoorHideAndSeek mainGame;

    public ScreenManager(IndoorHideAndSeek mainGame){
        this.mainGame = mainGame;
    }

    public void transitToWelcomeScreen(){
        TransitionScreen transition = new TransitionScreen(mainGame, new WelcomeScreen(mainGame));
        mainGame.setScreen(transition);
    }

    public void transitToWaitingRoomScreen(int roomId, Player me){
        TransitionScreen transition = new TransitionScreen(mainGame, new WaitingRoomScreen(mainGame, roomId, me));
        this.mainGame.setScreen(transition);
    }

    public void transitToGameScreen(int roomId, Player me, HashMap<String, Player> playerMap){
        TransitionScreen transition = new TransitionScreen(mainGame, new GameScreen(mainGame, roomId, me, playerMap));
        this.mainGame.setScreen(transition);
    }



    static public class TransitionScreen implements Screen {
        private final float TRANSITION_TIME = 500; //1000 ms

        private IndoorHideAndSeek mainGame;
        private Screen currentScreen, nextScreen;
        private long startTime;
        private Sprite fade;
        private SpriteBatch batch;
        private int state;
        private float fadeAlpha;

        public TransitionScreen(IndoorHideAndSeek mainGame, Screen nextScreen){
            this.mainGame = mainGame;
            this.currentScreen = mainGame.getScreen();
            this.nextScreen = nextScreen;
            this.state = -1;
            this.fadeAlpha = 0;
            this.startTime = System.currentTimeMillis();
            this.batch = new SpriteBatch();
            setBackground();
        }

        private void setBackground(){
            Pixmap pixmap = new Pixmap(Gdx.graphics.getWidth(), Gdx.graphics.getHeight(), Pixmap.Format.RGB888);
            pixmap.setColor(Color.BLACK);
            pixmap.fill();
            fade = new Sprite(new Texture(pixmap));
            pixmap.dispose();
            fade.setPosition(0, 0);
            fade.setSize(Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        }

        @Override
        public void show() {

        }

        private void halfTransition(){
            this.state = 1;
            this.currentScreen.dispose();
            this.currentScreen = null;
            this.startTime = System.currentTimeMillis();
        }

        private void endTransition(){
            Gdx.app.postRunnable(new Runnable() {
                @Override
                public void run() {
                    mainGame.setScreen(nextScreen);
                    dispose();
                }
            });
        }

        @Override
        public void render(float delta) {
            Gdx.gl.glClearColor(0, 0, 0, 1);
            Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
            long currentTime = System.currentTimeMillis();
            if(state == -1) {
                fadeAlpha = Math.abs((currentTime - startTime) / TRANSITION_TIME);
                this.currentScreen.render(delta);
            }else if(state == 1){
                fadeAlpha = Math.abs(1f - (currentTime - startTime) / TRANSITION_TIME);
                this.nextScreen.render(delta);
            }
            batch.begin();
            fade.draw(batch, fadeAlpha);
            batch.end();
            if (currentTime - this.startTime >= TRANSITION_TIME) {
                if (state == -1) {
                    this.halfTransition();
                } else if (state == 1){
                    this.endTransition();
                }
            }
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
            batch.dispose();
        }


    }

    static public class BlackScreen implements Screen {
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
}

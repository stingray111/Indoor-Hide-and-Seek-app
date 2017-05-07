package com.csci3310.indoorhns;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.ScreenAdapter;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

/**
 * Created by Edmund on 11/3/2017.
 */

public class TransitionScreen implements Screen {
    private final float TRANSITION_TIME = 1000; //1000 ms

    private Game mainGame;
    private Screen currentScreen, nextScreen;
    private Class nextScreenClass;
    private long startTime;
    private Sprite fade;
    private SpriteBatch batch;
    private boolean fadeIn;

    public TransitionScreen(Game mainGame, Screen currentScreen, Class nextScreenClass){
        this.mainGame = mainGame;
        this.currentScreen = currentScreen;
        this.nextScreenClass = nextScreenClass;
        this.fadeIn = true;
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
        this.fadeIn = false;
        this.currentScreen.dispose();
        this.currentScreen = null;
        if(this.nextScreen == null && this.nextScreenClass != null){
            try {
                this.nextScreen = (Screen)(this.nextScreenClass.getConstructor(Game.class).newInstance(mainGame));
            } catch (Exception e) {
                e.printStackTrace();
                System.exit(1);
            }
        }
        this.startTime = System.currentTimeMillis();
    }

    private void endTransition(){
        this.mainGame.setScreen(this.nextScreen);
        this.dispose();
    }

    @Override
    public void render(float delta) {
        long currentTime = System.currentTimeMillis();
        float alpha;
        if(fadeIn) {
            alpha = (currentTime - this.startTime) / TRANSITION_TIME;
            if(alpha == 0)alpha = 0.01f;
            this.currentScreen.render(delta);
        }else{
            alpha = 1.0f - ((currentTime - this.startTime) / TRANSITION_TIME);
            if(alpha < 0)alpha = 0f;
            this.nextScreen.render(delta);
        }
        batch.begin();
        fade.draw(batch, alpha);
        batch.end();
        if (alpha >= 1 || alpha <= 0){
            if(fadeIn)
                this.halfTransition();
            else
                this.endTransition();
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

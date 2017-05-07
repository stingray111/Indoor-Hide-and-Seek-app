package com.csci3310.indoorhns;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.ScreenAdapter;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.NinePatchDrawable;

/**
 * Created by Edmund on 5/8/2017.
 */

public class WelcomeScreen implements Screen{
    private int width, height;
    private Game mainGame;

    private Stage stage;
    private Skin skin;
    private Table frame;
    private Label title;
    private TextButton joinRoom, createRoom;
    private Sprite background;
    private SpriteBatch batch;

    public WelcomeScreen(Game mainGame){
        this.mainGame = mainGame;
        this.width = Gdx.graphics.getWidth();
        this.height = Gdx.graphics.getHeight();
        createSkin();
        createStage();
    }

    private void createStage() {
        stage = new Stage();
        Gdx.input.setInputProcessor(stage);

        //background
        batch = new SpriteBatch();

        Pixmap pixmap = new Pixmap(width, height, Pixmap.Format.RGB888);
        pixmap.setColor(Color.BLACK);
        pixmap.fill();
        background = new Sprite(new Texture(pixmap));

//        background = new Sprite(new Texture(Gdx.files.internal("background.png")));

        //table
        frame = new Table();
        frame.setBounds(width*0.1f, height*0.2f, width*0.8f, height*0.6f);
        stage.addActor(frame);

        //title
        title = new Label("Indoor Hide & Seek", new Label.LabelStyle(skin.getFont("title"), Color.CYAN));
        frame.add(title).row();

        //joinRoom button
        joinRoom = new TextButton("Join Room", skin.get("button", TextButton.TextButtonStyle.class));
        frame.add(joinRoom).padTop(height*0.1f).width(width * 0.3f).height(height * 0.1f).row();

        //createRoom button
        createRoom = new TextButton("Create Room", skin.get("button", TextButton.TextButtonStyle.class));
        createRoom.setWidth(width * 0.3f);
        createRoom.setHeight(height * 0.1f);
        frame.add(createRoom).padTop(height*0.1f).width(width * 0.3f).height(height * 0.1f).row();
    }

    private void createSkin() {
        skin = new Skin();
        //font
        FreeTypeFontGenerator generator = new FreeTypeFontGenerator(Gdx.files.internal("Typo Angular Rounded Bold Demo.otf"));
        FreeTypeFontGenerator.FreeTypeFontParameter parameter = new FreeTypeFontGenerator.FreeTypeFontParameter();
        parameter.size = Math.round(this.height * 0.1f);
        skin.add("title", generator.generateFont(parameter));

        parameter.size = Math.round(this.height * 0.05f);
        skin.add("button", generator.generateFont(parameter));
        generator.dispose();

        //textButton
        TextureAtlas atlas = new TextureAtlas(Gdx.files.internal("textureAtlas.pack"));
        skin.add("buttonPatch", atlas.createPatch("welcomeScreenButton"));
        TextButton.TextButtonStyle style = new TextButton.TextButtonStyle();
        style.font = skin.getFont("button");
        style.fontColor = Color.WHITE;
        style.up = new NinePatchDrawable(skin.getPatch("buttonPatch"));
        style.down = new NinePatchDrawable((skin.getPatch("buttonPatch"))).tint(Color.DARK_GRAY);
        skin.add("button", style);
    }

    @Override
    public void show() {
    }

    @Override
    public void render(float delta) {
        Gdx.gl.glClearColor(1, 1, 1, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        batch.begin();
        background.draw(batch);
        batch.end();
        stage.act(delta);
        stage.draw();
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
        stage.dispose();
        skin.dispose();
    }
}

package com.csci3310.indoorhns;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.ui.TextField;
import com.badlogic.gdx.scenes.scene2d.utils.NinePatchDrawable;
import com.badlogic.gdx.scenes.scene2d.utils.SpriteDrawable;

/**
 * Created by edmund on 17/5/2017.
 */

public class GameScreen implements Screen {

    private IndoorHideAndSeek mainGame;
    private int roomId;
    private Player me;

    private int width, height;

    private Stage stage;
    private Skin skin;
    private Image floor09, floor10;
    private Button endGame;
    private SpriteBatch batch;
    private Sprite background;


    public GameScreen(IndoorHideAndSeek mainGame, int roomId, Player me){
        this.mainGame = mainGame;
        this.roomId = roomId;
        this.me = me;
        this.width = Gdx.graphics.getWidth();
        this.height = Gdx.graphics.getHeight();
        createStage();
        createSkin();
        bindListener();
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
        pixmap.dispose();

//        background = new Sprite(new Texture(Gdx.files.internal("background.png")));

        // floor 9
        floor09 = new Image(new Texture(Gdx.files.internal("09_blue.png")));

        // floor 10
        floor10 = new Image(new Texture(Gdx.files.internal("10_blue.png")));

    }

    private void createSkin() {
        skin = new Skin();
        //font
        FreeTypeFontGenerator generator = new FreeTypeFontGenerator(Gdx.files.internal("Audiowide-Regular.ttf"));
        FreeTypeFontGenerator.FreeTypeFontParameter parameter = new FreeTypeFontGenerator.FreeTypeFontParameter();
        parameter.size = Math.round(this.height * 0.1f);
        skin.add("title", generator.generateFont(parameter));

        parameter.size = Math.round(this.height * 0.05f);
        skin.add("button", generator.generateFont(parameter));

        parameter.size = Math.round(this.height * 0.05f);
        skin.add("text", generator.generateFont(parameter));
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

        //textfield
        Pixmap pixmap = new Pixmap((int)Math.floor(skin.getFont("text").getSpaceWidth()), (int)Math.floor(skin.getFont("text").getLineHeight()), Pixmap.Format.RGBA8888);
        pixmap.setColor(0, 0, 0, 0);
        pixmap.fill();
        pixmap.setColor(Color.WHITE);
        pixmap.fillRectangle(0, (int)Math.floor(pixmap.getHeight() * 0.9f), pixmap.getWidth(), (int)Math.floor(pixmap.getHeight() * 0.1f));
        SpriteDrawable cursor = new SpriteDrawable(new Sprite(new Texture(pixmap)));
        pixmap.setColor(1, 1, 1, 0.5f);
        pixmap.fill();
        SpriteDrawable selector = new SpriteDrawable(new Sprite(new Texture(pixmap)));
        pixmap.setColor(0,0,0,0);
        pixmap.fill();
        pixmap.setColor(Color.CYAN);
        pixmap.fillRectangle(0, (int)Math.floor(pixmap.getHeight() * 0.95f), pixmap.getWidth(), (int)Math.floor(pixmap.getHeight() * 0.05f));
        SpriteDrawable background = new SpriteDrawable(new Sprite(new Texture(pixmap)));
        pixmap.dispose();
        skin.add("textfield", new TextField.TextFieldStyle(skin.getFont("text"), Color.WHITE, cursor, selector, background));

        //popup
        skin.add("popupFrame", atlas.createPatch("gamePopupFrame"));
    }

    private void bindListener() {

    }

    @Override
    public void show() {

    }

    @Override
    public void render(float delta) {
        Gdx.gl.glClearColor(0, 0, 0, 0);
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
        batch.dispose();
    }
}

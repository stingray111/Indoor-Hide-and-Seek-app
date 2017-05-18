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
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Stack;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.ui.TextField;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.utils.NinePatchDrawable;
import com.badlogic.gdx.scenes.scene2d.utils.SpriteDrawable;
import com.badlogic.gdx.utils.Align;

import java.util.HashMap;
import java.util.concurrent.RunnableFuture;

import static com.badlogic.gdx.graphics.Pixmap.Format.RGBA8888;

/**
 * Created by edmund on 17/5/2017.
 */

public class GameScreen implements Screen {

    final private static Color[] HunterColorList = new Color[]{Color.GREEN, Color.ORANGE, Color.RED};
    final private static Color HunteeColor = Color.WHITE;
    final private static int[] HunterRadiusList = new int[]{30, 25, 20};
    final private static int HunteeRadius = 40;

    private IndoorHideAndSeek mainGame;
    private int roomId;
    private Player me;
    private HashMap<String, Player> playerMap;

    private int width, height;
    private float floor9ImageRatio, floor10ImageRatio;

    private Stage stage;
    private Skin skin;
    private Group floor9Group, floor10Group;
    private Image floor9, floor10;
    private Label floor9Label, floor10Label;
    private TextButton endGame, showName;
    private SpriteBatch batch;
    private Sprite background;

    private HashMap<String, Image> playerIndicatorMap;
    private HashMap<String, Label> playerNameLabelMap;

    private GamePointCoordinateConverter coordConverter;

    private boolean playerLocationUpdatePolling;
    private HashMap<String, String> fakeHashMap;

    public boolean getPlayerLocationUpdatePollingTrigger(){return playerLocationUpdatePolling;}
    public IndoorHideAndSeek getMainGame(){return mainGame;}

    public GameScreen(IndoorHideAndSeek mainGame, int roomId, Player me, HashMap<String, Player> playerMap){
        this.mainGame = mainGame;
        this.roomId = roomId;
        this.me = me;
        this.playerMap = playerMap;
        this.width = Gdx.graphics.getWidth();
        this.height = Gdx.graphics.getHeight();
        this.coordConverter = new GamePointCoordinateConverter();
        calculateImageRatio();
        createSkin();
        createStage();
        bindListener();
        mainGame.getAndroidConnector().getCoordinator().startWifiScan();
        playerLocationUpdatePolling = true;
        startPlayerLocationUpdatePolling(this);
    }

    private void startPlayerLocationUpdatePolling(final GameScreen gameScreen){
        mainGame.getNetworkManager().startPlayerLocationPolling(gameScreen, new NetworkManager.NetworkTaskFinishListener(){
            @Override
            public void onPlayerLocationUpdate(final HashMap<String, String> playerPointMap) {
                Gdx.app.postRunnable(new Runnable() {
                    @Override
                    public void run() {
                        updatePlayerCoordinate(playerPointMap);
                    }
                });
            }
            @Override
            public void onEndGame(){
                stopPlayerLocationUpdatePolling();
                mainGame.getAndroidConnector().getCoordinator().showToast("Huntee is caught. Game is Ended");
                Gdx.app.postRunnable(new Runnable() {
                    @Override
                    public void run() {
                        mainGame.getScreenManager().transitToWelcomeScreen();
                    }
                });
            }
        });
    }

    private void stopPlayerLocationUpdatePolling(){
        playerLocationUpdatePolling = false;
    }

    public void updatePlayerCoordinate(HashMap<String, String> playerPointMap){
        if(playerPointMap == null)return;
        for(String androidId : playerPointMap.keySet()){
            String point = playerPointMap.get(androidId);
            Coordinate newCoord = coordConverter.getCoordinate(point);
            if(newCoord == null)continue;
            int floor = coordConverter.getFloor(point);
            float ratio = floor == 9 ? floor9ImageRatio : floor10ImageRatio;
            float offsetX = floor == 9 ? floor9.getX() : floor10.getX();
            Player player = playerMap.get(androidId);
            if(player == null)continue;
            Image indicator = playerIndicatorMap.get(androidId);
            Label nameLabel = playerNameLabelMap.get(androidId);
            indicator.remove();
            nameLabel.remove();
            player.getCoordinate().setCoordinate(offsetX + newCoord.getX() * ratio, newCoord.getY() * ratio);
            indicator.setPosition(player.getCoordinate().getX() - indicator.getWidth()/2, player.getCoordinate().getY() - indicator.getHeight()/2);
            nameLabel.setPosition(indicator.getX() - nameLabel.getWidth()/2, indicator.getY() + nameLabel.getHeight() + indicator.getHeight()/2);
            if(me.getType() == Player.Type.Hunter && player.getType() == Player.Type.Huntee)continue;
            if(floor == 9){
                floor9Group.addActor(indicator);
                if(showName.isChecked())floor9Group.addActor(nameLabel);
            }else{
                floor10Group.addActor(indicator);
                floor10Group.addActor(nameLabel);
                if(showName.isChecked())floor10Group.addActor(nameLabel);
            }
        }
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
        floor9 = new Image(new Texture(Gdx.files.internal("09_blue.png")));
        floor9.setSize(floor9.getPrefWidth()*floor9ImageRatio, floor9.getPrefHeight()*floor9ImageRatio);
        floor9.setPosition((width - floor9.getWidth())/2, (height - floor9.getHeight())/2);

        //floor9 label
        floor9Label = new Label("Floor 9", new Label.LabelStyle(skin.getFont("text"), Color.GREEN));
        floor9Label.setPosition(0, height - floor9Label.getHeight());

        //floor 9 stack
        floor9Group = new Group();
//        floor9Group.setBounds(floor9.getX(), floor9.getY(), floor9.getWidth(), floor9.getHeight());
        floor9Group.addActor(floor9);
        floor9Group.addActor(floor9Label);
        stage.addActor(floor9Group);

        // floor 10
        floor10 = new Image(new Texture(Gdx.files.internal("10_blue.png")));
        floor10.setSize(floor10.getPrefWidth()*floor10ImageRatio, floor10.getPrefHeight()*floor10ImageRatio);
        floor10.setPosition((width - floor10.getWidth())/2, (height - floor10.getHeight())/2);

        //floor10 label
        floor10Label = new Label("Floor 10", new Label.LabelStyle(skin.getFont("text"), Color.GREEN));
        floor10Label.setPosition(0, height - floor10Label.getHeight());

        //floor10 stack
        floor10Group = new Group();
//        floor10Group.setBounds(floor10.getX(), floor10.getY(), floor10.getWidth(), floor10.getHeight());
        floor10Group.addActor(floor10);
        floor10Group.addActor(floor10Label);

        // show name button
        showName = new TextButton("Show Name", skin.get("button", TextButton.TextButtonStyle.class));
        showName.setPosition(0, 0);
        stage.addActor(showName);

        // end button
        endGame = new TextButton("Caught", skin.get("button", TextButton.TextButtonStyle.class));
        endGame.setPosition(width - endGame.getWidth(), height-endGame.getHeight());
        if(me.getType() == Player.Type.Huntee) {
            stage.addActor(endGame);
        }

        // player  indicator
        playerIndicatorMap = new HashMap<String, Image>();
        int loopCnt = 0;
        for(String androidId : playerMap.keySet()){
            if(playerMap.get(androidId).getType() == Player.Type.Hunter) {
                int radius = HunterRadiusList[loopCnt];
                pixmap = new Pixmap(radius * 2, radius * 2, RGBA8888);
                pixmap.setColor(HunterColorList[loopCnt]);
                for(int i=0; i<4; i++){
                    pixmap.drawCircle(radius, radius, radius-i);
                }
                loopCnt++;
            }else{
                pixmap = new Pixmap(HunteeRadius * 2, HunteeRadius * 2, RGBA8888);
                pixmap.setColor(HunteeColor);
                for(int i=0; i<4; i++){
                    pixmap.drawCircle(HunteeRadius, HunteeRadius, HunteeRadius-i);
                }

            }
            Image image = new Image(new Texture(pixmap));
            pixmap.dispose();
            image.setSize(image.getPrefWidth(), image.getPrefHeight());
            playerIndicatorMap.put(androidId, image);
        }

        // player name label
        playerNameLabelMap = new HashMap<String, Label>();
        for(String androidId: playerMap.keySet()){
            Image indicator = playerIndicatorMap.get(androidId);
            Label label = new Label(playerMap.get(androidId).getName(), new Label.LabelStyle(skin.getFont("text"), Color.GREEN));
            playerNameLabelMap.put(androidId, label);
        }

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
        Pixmap pixmap = new Pixmap((int)Math.floor(skin.getFont("text").getSpaceWidth()), (int)Math.floor(skin.getFont("text").getLineHeight()), RGBA8888);
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

    private void calculateImageRatio(){
        // floor 9 - original image height : 451
        floor9ImageRatio = height / 451f;
        // floor 10 - original image height: 882
        floor10ImageRatio = height / 882f;
    }

    private void bindListener() {
        floor9.addListener(new ClickListener(){
            @Override
            public void clicked(InputEvent event, float x, float y) {
                super.clicked(event, x, y);
                Gdx.app.postRunnable(new Runnable() {
                    @Override
                    public void run() {
                        floor9Group.remove();
                        stage.addActor(floor10Group);
                    }
                });
            }
        });
        floor10.addListener(new ClickListener(){
            @Override
            public void clicked(InputEvent event, float x, float y) {
                super.clicked(event, x, y);
                Gdx.app.postRunnable(new Runnable() {
                    @Override
                    public void run() {
                        floor10Group.remove();
                        stage.addActor(floor9Group);
                    }
                });
            }
        });
        endGame.addListener(new ClickListener(){
            @Override
            public void clicked(InputEvent event, float x, float y) {
                super.clicked(event, x, y);
                 mainGame.getNetworkManager().endGame(roomId);
                Gdx.app.postRunnable(new Runnable() {
                    @Override
                    public void run() {
                        mainGame.getScreenManager().transitToWelcomeScreen();
                    }
                });
            }
        });
        showName.addListener(new ClickListener(){
            @Override
            public void clicked(InputEvent event, float x, float y) {
                super.clicked(event, x, y);
                Gdx.app.postRunnable(new Runnable() {
                    @Override
                    public void run() {
                        if(showName.isChecked()){
                            showName.setText("Hide Name");
                        }else{
                            showName.setText("Show Name");
                        }
                    }
                });
            }
        });
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
        stopPlayerLocationUpdatePolling();
        mainGame.getAndroidConnector().getCoordinator().stopWifiScan();
    }
}

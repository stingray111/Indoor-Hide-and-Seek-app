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
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.actions.IntAction;
import com.badlogic.gdx.scenes.scene2d.ui.Cell;
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

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by Edmund on 5/15/2017.
 */

public class WaitingRoomScreen implements Screen {

    final static float CountdownTime = 30f;
    final static int RoomFullLimit = 4;

    private IndoorHideAndSeek mainGame;
    private int roomId;

    private int width, height;
    private Stage stage;
    private Skin skin;
    private SpriteBatch batch;
    private Sprite background;
    private Table mainFrame, roomIdFrame, huntersFrame, hunteesFrame, countdownPopupFrame;
    private TextButton leave;
    private Label roomIdTextLabel, roomIdLabel, isHuntingLabel, loadingPopupLabel, countdownPopupTextLabel, countdownPopupLabel;
    private Stack loadingPopupStack, countdownPopupStack;
    private ArrayList<Label> hunterLabelList, hunteeLabelList;
    private HashMap<String, Player> playerMap;
    private Player me;
    private boolean playerListUpdatePollingTrigger;
    private CountdownTimer timer;

    public boolean getPlayerListUpdatePollingTrigger(){return this.playerListUpdatePollingTrigger;}
    public HashMap<String, Player> getPlayerMap(){return this.playerMap;};
    public Player getMe(){return this.me;}

    public WaitingRoomScreen(final IndoorHideAndSeek mainGame, final int roomId, final Player me){
        this.mainGame = mainGame;
        this.roomId = roomId;
        this.width = Gdx.graphics.getWidth();
        this.height = Gdx.graphics.getHeight();
        this.me = me;
        this.playerMap = new HashMap<String, Player>();
        this.playerMap.put(me.getAndroidID(), me);
        createSkin();
        createStage();
        createLoadingPopup();
        createCountdownPopupStack();
        updateLabelList();
        bindListener();
        this.playerListUpdatePollingTrigger = true;
        startPlayerListUpdatePolling(this);
        this.timer = new CountdownTimer(CountdownTime, new CountdownTimer.CountdownTimerListener() {

            @Override
            public void onCountdownTimerStart() {
                stage.addActor(countdownPopupStack);
            }

            @Override
            public void onCountdownTimerStep() {
                countdownPopupLabel.setText(timer.timeToString());
            }

            @Override
            public void onCountdownTimerInterrupt() {
                countdownPopupStack.remove();
            }

            @Override
            public void onCountdownTimerSet() {
                mainGame.getScreenManager().transitToGameScreen(roomId, me, playerMap);
            }
        });
    }

    private void startPlayerListUpdatePolling(final WaitingRoomScreen waitingRoom){
        mainGame.getNetworkManager().startPlayerListPolling(waitingRoom, new NetworkManager.NetworkTaskFinishListener(){
            @Override
            public void onPlayerListUpdate() {
                Gdx.app.postRunnable(new Runnable() {
                    @Override
                    public void run() {
                        updateLabelList();
                        if(playerMap.size() == RoomFullLimit){
                            if(!timer.isRunning()) {
                                timer.start();
                            }
                        }
                        if(timer.isRunning() && playerMap.size() < RoomFullLimit){
                            timer.interrupt();
                        }
                    }
                });
            }

            @Override
            public void onEndGame() {
                playerListUpdatePollingTrigger = false;
                mainGame.getAndroidConnector().getCoordinator().showToast("Huntee is gone. Room is Closed.");
                Gdx.app.postRunnable(new Runnable() {
                    @Override
                    public void run() {
                        mainGame.getScreenManager().transitToWelcomeScreen();
                    }
                });
            }
        });
    }

    private void bindListener(){
        leave.addListener(new ClickListener(){
            @Override
            public void clicked(InputEvent event, float x, float y) {
                super.clicked(event, x, y);
                stage.addActor(loadingPopupStack);
                playerListUpdatePollingTrigger = false;
                Gdx.app.postRunnable(new Runnable() {
                    @Override
                    public void run() {
                        mainGame.getScreenManager().transitToWelcomeScreen();
                    }
                });
            }
        });
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

        //main frame
        mainFrame = new Table();
        mainFrame.setBounds(0, 0, width, height);
        stage.addActor(mainFrame);

        // leave button
        leave = new TextButton("Leave Room", skin.get("button", TextButton.TextButtonStyle.class));
        mainFrame.add(leave).left().top().padLeft(width*0.05f).padTop(height*0.05f).colspan(3).expandX().row();

        //roomId Frame
        roomIdFrame = new Table();
        mainFrame.add(roomIdFrame).colspan(3).expandX().center().row();

        //roomId Text Label
        roomIdTextLabel = new Label("Room ID: ", new Label.LabelStyle(skin.getFont("title"), Color.CYAN));
        roomIdFrame.add(roomIdTextLabel);

        //roomId Label
        roomIdLabel = new Label(Integer.toString(roomId), new Label.LabelStyle(skin.getFont("title"), Color.CYAN));
        roomIdFrame.add(roomIdLabel);

        //hunters Frame
        huntersFrame = new Table();
        huntersFrame.setBackground(new NinePatchDrawable(skin.getPatch("popupFrame")));
        mainFrame.add(huntersFrame).padLeft(width* 0.05f).right().expandX();

        //is hunting Label
        isHuntingLabel = new Label("is hunting", new Label.LabelStyle(skin.getFont("text"), Color.GREEN));
        mainFrame.add(isHuntingLabel).padLeft(width*0.05f).padRight(width*0.05f).expandY();

        //huntee Frame
        hunteesFrame = new Table();
        hunteesFrame.setBackground(new NinePatchDrawable(skin.getPatch("popupFrame")));
        mainFrame.add(hunteesFrame).padRight(width*0.05f).left().expandX().row();

        //huters label list
        hunterLabelList = new ArrayList<Label>();

        //huntee label list
        hunteeLabelList = new ArrayList<Label>();
    }

    private void createCountdownPopupStack(){
        // countdown popup stack
        countdownPopupStack = new Stack();
        Pixmap pixmap = new Pixmap(width, height, Pixmap.Format.RGBA8888);
        pixmap.setColor(0, 0, 0, 0.9f);
        pixmap.fill();
        countdownPopupStack.add(new Image(new SpriteDrawable(new Sprite(new Texture(pixmap)))));
        pixmap.dispose();

        countdownPopupStack.setBounds(0, 0, width, height);

        // countdown popup frame
        countdownPopupFrame = new Table();
        countdownPopupStack.add(countdownPopupFrame);

        //countdown popup text label
        countdownPopupTextLabel = new Label("Game Starts in", new Label.LabelStyle(skin.getFont("text"), Color.GREEN));
        countdownPopupFrame.add(countdownPopupTextLabel).padBottom(height*0.05f).row();

        //countdown popup label
        countdownPopupLabel = new Label(" ", new Label.LabelStyle(skin.getFont("text"), Color.GREEN));
        countdownPopupFrame.add(countdownPopupLabel).row();
    }

    private void createLoadingPopup(){

        //loading popup stack
        loadingPopupStack = new Stack();
        Pixmap pixmap = new Pixmap(width, height, Pixmap.Format.RGBA8888);
        pixmap.setColor(0, 0, 0, 0.9f);
        pixmap.fill();
        loadingPopupStack.add(new Image(new SpriteDrawable(new Sprite(new Texture(pixmap)))));
        pixmap.dispose();

        loadingPopupStack.setBounds(0, 0, width, height);

        //loading popup label
        loadingPopupLabel = new Label("Now Loading...", new Label.LabelStyle(skin.getFont("text"), Color.GREEN));
        loadingPopupLabel.setPosition(width/2, height/2);
        loadingPopupLabel.setAlignment(Align.center);
        loadingPopupStack.add(loadingPopupLabel);

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

    private void updateLabelList(){
        huntersFrame.clearChildren();
        hunterLabelList.clear();
        hunteesFrame.clearChildren();
        hunteeLabelList.clear();

        for(String androidId : playerMap.keySet()){
            Player player = playerMap.get(androidId);
            Label label = new Label(player.getName(), new Label.LabelStyle(skin.getFont("text"), Color.GREEN));
            if(player.getType() == Player.Type.Hunter){
                huntersFrame.add(label).right().padTop(height*0.02f).padBottom(height*0.02f).row();
                hunterLabelList.add(label);
            }else if(player.getType() == Player.Type.Huntee){
                hunteesFrame.add(label).left().padTop(height*0.02f).padBottom(height*0.02f).row();
                hunteeLabelList.add(label);
            }
        }
        if(hunterLabelList.size() > 0) {
            huntersFrame.setVisible(true);
            if (hunterLabelList.size() > 1) {
                isHuntingLabel.setText("are hunting");
            } else {
                isHuntingLabel.setText("is hunting");
            }
        }else{
            huntersFrame.setVisible(false);
        }

        if(hunteeLabelList.size() > 0) {
            hunteesFrame.setVisible(true);
            if (hunterLabelList.size() > 1) {
                isHuntingLabel.setText("are hunting");
            } else {
                isHuntingLabel.setText("is hunting");
            }
        }else{
            hunteesFrame.setVisible(false);
        }
    }

    @Override
    public void show() {

    }

    @Override
    public void render(float delta) {
        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        if (timer.isRunning()) {
            timer.step(delta);
        }
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

    public int getRoomId() {
        return roomId;
    }

    @Override
    public void dispose() {
        stage.dispose();
        skin.dispose();
        batch.dispose();
        this.playerListUpdatePollingTrigger = false;
        this.mainGame.getNetworkManager().leaveRoom(roomId,mainGame.getAndroidConnector().getCoordinator().getAndroidId());
    }
}

package com.csci3310.indoorhns;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
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

import javax.swing.GroupLayout;

/**
 * Created by Edmund on 5/8/2017.
 */

public class WelcomeScreen implements Screen{
    private int width, height;
    private IndoorHideAndSeek mainGame;

    private Stage stage;
    private Skin skin;
    private Table mainFrame, creditFrame, createRoomFrame, joinRoomFrame;
    private Label title, creditTitle, creditParagraph, createRoomTitle, joinRoomTitle, createRoomNameLabel, joinRoomNameLabel, joinRoomIdLabel, loadingPopupLabel;
    private TextButton joinRoom, createRoom, credit, creditClose, createRoomCancel, createRoomCreate, joinRoomCancel, joinRoomJoin;
    private TextField createRoomName, joinRoomName, joinRoomId;
    private Stack creditStack, createRoomStack, joinRoomStack, loadingPopupStack;
    private Sprite background;
    private SpriteBatch batch;
    private Camera camera;

    public WelcomeScreen(IndoorHideAndSeek mainGame){
        this.mainGame = mainGame;
        this.width = Gdx.graphics.getWidth();
        this.height = Gdx.graphics.getHeight();
        camera = new OrthographicCamera(width, height);
        camera.position.set(camera.viewportWidth/2, camera.viewportHeight/2, 0);
        camera.update();
        createSkin();
        createStage();
        createCreditPopup();
        createCreateRoomPopup();
        createJoinRoomPopup();
        createLoadingPopup();
        bindListener();
    }

    private void bindListener() {
        credit.addListener(new ClickListener(){
            @Override
            public void clicked(InputEvent event, float x, float y) {
                super.clicked(event, x, y);
                stage.addActor(creditStack);
            }
        });
        creditClose.addListener(new ClickListener(){

            @Override
            public void clicked(InputEvent event, float x, float y) {
                super.clicked(event, x, y);
                creditStack.remove();
            }
        });
        createRoom.addListener(new ClickListener(){
            @Override
            public void clicked(InputEvent event, float x, float y) {
                super.clicked(event, x, y);
                stage.addActor(createRoomStack);
            }
        });
        createRoomCancel.addListener(new ClickListener(){

            @Override
            public void clicked(InputEvent event, float x, float y) {
                super.clicked(event, x, y);
                createRoomName.setText("");
                createRoomStack.remove();
            }
        });
        joinRoom.addListener(new ClickListener(){
            @Override
            public void clicked(InputEvent event, float x, float y) {
                super.clicked(event, x, y);
                stage.addActor(joinRoomStack);
            }
        });
        joinRoomCancel.addListener(new ClickListener(){

            @Override
            public void clicked(InputEvent event, float x, float y) {
                super.clicked(event, x, y);
                joinRoomId.setText("");
                joinRoomName.setText("");
                joinRoomStack.remove();
            }
        });
        createRoomCreate.addListener(new ClickListener(){

            @Override
            public void clicked(InputEvent event, float x, float y) {
                super.clicked(event, x, y);
                int nameLength = createRoomName.getText().length();
                if(nameLength == 0){return;}
                stage.addActor(loadingPopupStack);
                Gdx.app.postRunnable(new Runnable() {
                    @Override
                    public void run() {
                        mainGame.getNetworkManager().createRoom(new NetworkManager.NetworkTaskFinishListener(){
                            @Override
                            public void onCreateRoomSuccess(final String roomId) {
                                Gdx.app.postRunnable(new Runnable() {
                                    @Override
                                    public void run() {
                                        String androidId = mainGame.getAndroidConnector().getCoordinator().getAndroidId();
                                        mainGame.getScreenManager().transitToWaitingRoomScreen(roomId, new Player(Player.Type.Huntee, createRoomName.getText(), androidId));
                                    }
                                });
                            }

                            @Override
                            public void onCreateRoomFail(String response) {
                                loadingPopupStack.remove();
                                mainGame.getAndroidConnector().getCoordinator().showToast(response);
                            }
                        });
                    }
                });
            }
        });
        joinRoomJoin.addListener(new ClickListener(){
            @Override
            public void clicked(InputEvent event, float x, float y) {
                super.clicked(event, x, y);
                int roomIdLength = joinRoomId.getText().length();
                int nameLength = joinRoomName.getText().length();
                if(nameLength == 0 || roomIdLength == 0){return;}
                stage.addActor(loadingPopupStack);
                Gdx.app.postRunnable(new Runnable() {
                    @Override
                    public void run() {
                        mainGame.getNetworkManager().joinRoom(joinRoomId.getText(), new NetworkManager.NetworkTaskFinishListener(){
                            @Override
                            public void onJoinRoomSuccess() {
                                Gdx.app.postRunnable(new Runnable() {
                                    @Override
                                    public void run() {
                                        String androidId = mainGame.getAndroidConnector().getCoordinator().getAndroidId();
                                        mainGame.getScreenManager().transitToWaitingRoomScreen(joinRoomId.getText(), new Player(Player.Type.Hunter, joinRoomName.getText(), androidId));
                                    }
                                });
                            }

                            @Override
                            public void onJoinRoomFail(String response) {
                                loadingPopupStack.remove();
                                mainGame.getAndroidConnector().getCoordinator().showToast(response);
                            }
                        });
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
        mainFrame.setBounds(width*0.1f, height*0.2f, width*0.8f, height*0.6f);
        stage.addActor(mainFrame);

        //title
        title = new Label("Indoor Hide & Seek", new Label.LabelStyle(skin.getFont("title"), Color.CYAN));
        mainFrame.add(title).row();

        //joinRoom button
        joinRoom = new TextButton("Join Room", skin.get("button", TextButton.TextButtonStyle.class));
        mainFrame.add(joinRoom).padTop(height*0.1f).width(width * 0.3f).height(height * 0.1f).row();

        //createRoom button
        createRoom = new TextButton("Create Room", skin.get("button", TextButton.TextButtonStyle.class));
        createRoom.setWidth(width * 0.3f);
        createRoom.setHeight(height * 0.1f);
        mainFrame.add(createRoom).padTop(height*0.1f).width(width * 0.3f).height(height * 0.1f).row();

        //credit button
        credit = new TextButton("Credit", skin.get("button", TextButton.TextButtonStyle.class));
        credit.setWidth(width * 0.3f);
        credit.setHeight(height * 0.1f);
        mainFrame.add(credit).padTop(height*0.1f).width(width * 0.3f).height(height * 0.1f).row();
    }

    private void createCreditPopup() {

        //credit stack
        creditStack = new Stack();
        Pixmap pixmap = new Pixmap(width, height, Pixmap.Format.RGBA8888);
        pixmap.setColor(0, 0, 0, 0.9f);
        pixmap.fill();
        creditStack.add(new Image(new SpriteDrawable(new Sprite(new Texture(pixmap)))));
        pixmap.dispose();

        //credit frame
        creditFrame = new Table();
        creditStack.addActor(creditFrame);

        //credit title
        creditTitle = new Label("Credit", new Label.LabelStyle(skin.getFont("title"), Color.CYAN));
        creditFrame.add(creditTitle).padTop(height*0.05f).row();

        //credit paragraph
        creditParagraph = new Label("This is a project product of CSCI3310\n\nThis project is contributed by:\nPing Shan LUK\nPak Yin TUNG\nCheuk Bun WONG\nWai Man YIP",
                new Label.LabelStyle(skin.getFont("text"), Color.WHITE));
        creditFrame.add(creditParagraph).padTop(height*0.05f).padLeft(width*0.1f).padRight(width*0.1f).row();

        //credit close
        creditClose = new TextButton("Close", skin.get("button", TextButton.TextButtonStyle.class));
        credit.setWidth(width * 0.2f);
        credit.setHeight(height * 0.1f);
        creditFrame.add(creditClose).padTop(height*0.05f).width(width * 0.2f).height(height * 0.1f).padBottom(height*0.05f).row();

        //credit stack size
        creditStack.setBounds((width - creditFrame.getMinWidth())/2, (height - creditFrame.getMinHeight())/2, creditFrame.getMinWidth(), creditFrame.getMinHeight());

        //credit frame background
        creditFrame.setBackground(new NinePatchDrawable((skin.getPatch("popupFrame"))).tint(new Color(1f, 1f, 1f, 1f)));
    }

    private void createCreateRoomPopup(){

        //createRoom stack
        createRoomStack = new Stack();
        Pixmap pixmap = new Pixmap(width, height, Pixmap.Format.RGBA8888);
        pixmap.setColor(0, 0, 0, 0.9f);
        pixmap.fill();
        createRoomStack.add(new Image(new SpriteDrawable(new Sprite(new Texture(pixmap)))));
        pixmap.dispose();

        //createRoom frame
        createRoomFrame = new Table();
        createRoomFrame.defaults().padTop(height*0.05f).padBottom(height*0.05f).padLeft(width*0.05f).padRight(width*0.05f);
        createRoomStack.addActor(createRoomFrame);

        //createRoom title
        createRoomTitle = new Label("Create Room", new Label.LabelStyle(skin.getFont("title"), Color.CYAN));
        createRoomFrame.add(createRoomTitle).colspan(2).center().row();

        //createRoom name label
        createRoomNameLabel = new Label("Player Name : ", new Label.LabelStyle(skin.getFont("text"), Color.WHITE));
        createRoomFrame.add(createRoomNameLabel).padRight(0).right();

        //createRoom name textfield
        createRoomName = new TextField("", skin.get("textfield", TextField.TextFieldStyle.class));
        createRoomFrame.add(createRoomName).width(width*0.3f).padLeft(0).left().row();

        //createRoom cancel
        createRoomCancel = new TextButton("Cancel", skin.get("button", TextButton.TextButtonStyle.class));
        createRoomFrame.add(createRoomCancel).width(width * 0.2f).height(height * 0.1f).padRight(0).left();

        //createRoom create
        createRoomCreate = new TextButton("Create", skin.get("button", TextButton.TextButtonStyle.class));
        createRoomFrame.add(createRoomCreate).width(width * 0.2f).height(height*0.1f).right().row();

        //createRoom stack size
        createRoomStack.setBounds((width - createRoomFrame.getMinWidth())/2, (height - createRoomFrame.getMinHeight())/2, createRoomFrame.getMinWidth(), createRoomFrame.getMinHeight());

        //joinRoom frame background
        createRoomFrame.setBackground(new NinePatchDrawable((skin.getPatch("popupFrame"))).tint(new Color(1f, 1f, 1f, 1f)));
    }

    private void createJoinRoomPopup(){

        //joinRoom stack
        joinRoomStack = new Stack();
        Pixmap pixmap = new Pixmap(width, height, Pixmap.Format.RGBA8888);
        pixmap.setColor(0, 0, 0, 0.9f);
        pixmap.fill();
        joinRoomStack.add(new Image(new SpriteDrawable(new Sprite(new Texture(pixmap)))));
        pixmap.dispose();

        //joinRoom frame
        joinRoomFrame = new Table();
        joinRoomFrame.defaults().padTop(height*0.05f).padBottom(height*0.05f).padLeft(width*0.05f).padRight(width*0.05f);
        joinRoomStack.addActor(joinRoomFrame);

        //joinRoom title
        joinRoomTitle = new Label("Join Room", new Label.LabelStyle(skin.getFont("title"), Color.CYAN));
        joinRoomFrame.add(joinRoomTitle).colspan(2).center().row();

        //joinRoom name label
        joinRoomIdLabel = new Label("Room ID : ", new Label.LabelStyle(skin.getFont("text"), Color.WHITE));
        joinRoomFrame.add(joinRoomIdLabel).padRight(0).right();

        //joinRoom name textfield
        joinRoomId = new TextField("", skin.get("textfield", TextField.TextFieldStyle.class));
        joinRoomFrame.add(joinRoomId).width(width*0.3f).padLeft(0).left().row();

        //joinRoom name label
        joinRoomNameLabel = new Label("Player Name : ", new Label.LabelStyle(skin.getFont("text"), Color.WHITE));
        joinRoomFrame.add(joinRoomNameLabel).padRight(0).right();

        //joinRoom name textfield
        joinRoomName = new TextField("", skin.get("textfield", TextField.TextFieldStyle.class));
        joinRoomFrame.add(joinRoomName).width(width*0.3f).padLeft(0).left().row();

        //joinRoom cancel
        joinRoomCancel = new TextButton("Cancel", skin.get("button", TextButton.TextButtonStyle.class));
        joinRoomFrame.add(joinRoomCancel).width(width * 0.2f).height(height * 0.1f).padRight(0).left();

        //joinRoom join
        joinRoomJoin = new TextButton("Join", skin.get("button", TextButton.TextButtonStyle.class));
        joinRoomFrame.add(joinRoomJoin).width(width * 0.2f).height(height*0.1f).right().row();

        //joinRoom stack size
        joinRoomStack.setBounds((width - joinRoomFrame.getMinWidth())/2, (height - joinRoomFrame.getMinHeight())/2, joinRoomFrame.getMinWidth(), joinRoomFrame.getMinHeight());

        //joinRoom frame background
        joinRoomFrame.setBackground(new NinePatchDrawable((skin.getPatch("popupFrame"))).tint(new Color(1f, 1f, 1f, 1f)));
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
        stage.getBatch().setProjectionMatrix(camera.combined);
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

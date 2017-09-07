package com.snake.game.states;

import com.badlogic.gdx.Application.ApplicationType;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Dialog;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.snake.game.GameStateController;
import com.snake.game.util.MyClient;

public class Mainmenu extends State {
    private static /* synthetic */ int[] $SWITCH_TABLE$com$badlogic$gdx$Application$ApplicationType = null;
    public static final int ID = 1;
    private TextButton creditButton;
    private TextButton exitButton;
    private TextButton highscoreButton;
    private TextButton optionButton;
    private TextButton playButton;
    public static String uiskin = "data/neon-ui.json";
    static /* synthetic */ int[] $SWITCH_TABLE$com$badlogic$gdx$Application$ApplicationType() {
        int[] iArr = $SWITCH_TABLE$com$badlogic$gdx$Application$ApplicationType;
        if (iArr == null) {
            iArr = new int[ApplicationType.values().length];
            try {
                iArr[ApplicationType.Android.ordinal()] = 1;
            } catch (NoSuchFieldError e) {
            }
            try {
                iArr[ApplicationType.Applet.ordinal()] = 4;
            } catch (NoSuchFieldError e2) {
            }
            try {
                iArr[ApplicationType.Desktop.ordinal()] = 2;
            } catch (NoSuchFieldError e3) {
            }
            try {
                iArr[ApplicationType.HeadlessDesktop.ordinal()] = 3;
            } catch (NoSuchFieldError e4) {
            }
            try {
                iArr[ApplicationType.WebGL.ordinal()] = 5;
            } catch (NoSuchFieldError e5) {
            }
            try {
                iArr[ApplicationType.iOS.ordinal()] = 6;
            } catch (NoSuchFieldError e6) {
            }
            $SWITCH_TABLE$com$badlogic$gdx$Application$ApplicationType = iArr;
        }
        return iArr;
    }

    public Mainmenu(GameStateController gsm) {
        super(gsm);
    }

    public void init() {
        this.camera = new OrthographicCamera(State.VIEW_WIDTH, State.VIEW_HEIGHT);
        this.viewPort = new FitViewport(State.VIEW_WIDTH, State.VIEW_HEIGHT);
        this.viewPort.setCamera(this.camera);
        this.viewPort.apply();
        this.camera.position.set(this.viewPort.getWorldWidth(), this.viewPort.getWorldHeight(), 0.0f);
        MyClient myClient = new MyClient();
        this.stage = new Stage();
        Gdx.input.setInputProcessor(this.stage);
        switch ($SWITCH_TABLE$com$badlogic$gdx$Application$ApplicationType()[Gdx.app.getType().ordinal()]) {
            case 1:
                this.skin = new Skin(Gdx.files.internal(uiskin));
                break;
            case 2:
                this.skin = new Skin(Gdx.files.internal(uiskin));
                break;
        }
        float x = this.viewPort.getWorldWidth() / 2.0f;
        float y = this.viewPort.getWorldHeight();
        this.playButton = new TextButton("Play", this.skin);
        this.playButton.setBounds(x - 200.0f, (y / 2.0f) - 100.0f, 400.0f, 200.0f);
        this.optionButton = new TextButton("Options", this.skin);
        this.optionButton.setBounds(this.viewPort.getWorldWidth() - 150.0f, y - 100.0f, 100.0f, 50.0f);
        this.creditButton = new TextButton("Credit", this.skin);
        this.creditButton.setBounds(75.0f, y - 100.0f, 100.0f, 50.0f);
        this.highscoreButton = new TextButton("Highscore", this.skin);
        this.highscoreButton.setBounds(75.0f, 100.0f, 100.0f, 50.0f);
        this.exitButton = new TextButton("Exit", this.skin);
        this.exitButton.setBounds(this.viewPort.getWorldWidth() - 150.0f, 75.0f, 100.0f, 50.0f);
        this.stage.addActor(this.playButton);
        this.stage.addActor(this.optionButton);
        this.stage.addActor(this.creditButton);
        this.stage.addActor(this.highscoreButton);
        this.stage.addActor(this.exitButton);
        this.stage.setViewport(this.viewPort);
        this.stage.getCamera().update();
        this.stage.getViewport().apply();
        this.stage.getViewport().update(Gdx.graphics.getWidth(), Gdx.graphics.getHeight(), true);
        this.playButton.addListener(new ClickListener() {
            public void clicked(InputEvent event, float x, float y) {
                Mainmenu.this.gsm.gameSelect();
            }
        });
        this.optionButton.addListener(new ClickListener() {
            public void clicked(InputEvent event, float x, float y) {
                Mainmenu.this.gsm.showOptions();
            }
        });
        this.creditButton.addListener(new ClickListener() {
            private Dialog creditdialog;

            public void clicked(InputEvent event, float x, float y) {
                this.creditdialog = new Dialog("", Mainmenu.this.skin);
                this.creditdialog.addListener(new ClickListener() {
                    public void clicked(InputEvent event, float x, float y) {
                        creditdialog.hide();
                    }
                });
                this.creditdialog.add("Game made by Mortmann.\nUISkin made by ray3k.wordpress.com");
                this.creditdialog.show(Mainmenu.this.stage);
                this.creditdialog.setBounds((Mainmenu.this.viewPort.getWorldWidth() / 2.0f) - 155.0f, Mainmenu.this.playButton.getY() + 200.0f, 310.0f, 100.0f);
            }
        });
        this.highscoreButton.addListener(new ClickListener() {
            public void clicked(InputEvent event, float x, float y) {
                Mainmenu.this.gsm.showScore();
            }
        });
        this.exitButton.addListener(new ClickListener() {
            public void clicked(InputEvent event, float x, float y) {
                Gdx.app.exit();
            }
        });
    }

    public void render() {
        this.camera.update();
        this.stage.getCamera().update();
        this.stage.draw();
    }

    public void update(float delta) {
        this.viewPort.update(Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        this.stage.act();
    }

    public int getID() {
        return 1;
    }

    public void onExit() {
        this.stage.dispose();
        Gdx.input.setInputProcessor(null);
    }

    public void onResize(int screenWidth, int screenHeight) {
        this.viewPort.update(screenWidth, screenHeight);
        this.stage.getViewport().update(960, 540, true);
    }

    public void onEnter() {
        Gdx.input.setInputProcessor(this.stage);
    }

    public void dispose() {
    }
}

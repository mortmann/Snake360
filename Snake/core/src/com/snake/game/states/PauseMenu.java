package com.snake.game.states;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.snake.game.GameStateController;

public class PauseMenu extends State {
    public static final int ID = 7;
    private TextButton exitButton;
    private TextButton optionButton;
    private TextButton resumeButton;
    private Skin skin;
    private Stage stage;

    public PauseMenu(GameStateController gsm) {
        super(gsm);
    }

    public void init() {
        this.camera = new OrthographicCamera((float) Gdx.graphics.getWidth(), (float) Gdx.graphics.getHeight());
        this.viewPort = new FitViewport(State.VIEW_WIDTH, State.VIEW_HEIGHT);
        this.viewPort.setCamera(this.camera);
        this.viewPort.apply();
        this.camera.position.set(this.viewPort.getWorldWidth(), this.camera.viewportHeight, 0.0f);
        this.skin = new Skin(Gdx.files.internal(Mainmenu.uiskin));
        this.resumeButton = new TextButton("Resume", this.skin);
        this.resumeButton.setBounds(280.0f, 432.0f - 125.0f, 400.0f, 100.0f);
        this.optionButton = new TextButton("Options", this.skin);
        this.optionButton.setBounds(280.0f, 432.0f - 225.0f, 400.0f, 100.0f);
        this.exitButton = new TextButton("Exit to mainmenu", this.skin);
        this.exitButton.setBounds(280.0f, 432.0f - 325.0f, 400.0f, 100.0f);
        this.stage = new Stage();
        this.stage.addActor(this.exitButton);
        this.stage.addActor(this.optionButton);
        this.stage.setViewport(this.viewPort);
        this.stage.addActor(this.resumeButton);
        this.stage.getCamera().update();
        this.stage.getViewport().apply();
        this.stage.getViewport().update(Gdx.graphics.getWidth(), Gdx.graphics.getHeight(), true);
        Gdx.input.setInputProcessor(this.stage);
        this.resumeButton.addListener(new ClickListener() {
            public void clicked(InputEvent event, float x, float y) {
                PauseMenu.this.gsm.backState();
            }
        });
        this.optionButton.addListener(new ClickListener() {
            public void clicked(InputEvent event, float x, float y) {
                PauseMenu.this.gsm.showOptions();
            }
        });
        this.exitButton.addListener(new ClickListener() {
            public void clicked(InputEvent event, float x, float y) {
                PauseMenu.this.gsm.openMainMenu();
            }
        });
    }

    public void onResize(int screenWidth, int screenHeight) {
        this.stage.getViewport().update(screenWidth, screenHeight, true);
    }

    public void render() {
        this.camera.update();
        this.stage.getCamera().update();
        this.stage.draw();
    }

    public void update(float delta) {
        this.stage.act();
    }

    public void onExit() {
        this.stage.dispose();
        Gdx.input.setInputProcessor(null);
    }

    public int getID() {
        return 7;
    }

    public void onEnter() {
        Gdx.input.setInputProcessor(this.stage);
    }

    public void dispose() {
    }
}

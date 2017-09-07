package com.snake.game.states;

import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.snake.game.GameStateController;

public abstract class State {
    public static int ID = 0;
    public static final float VIEW_HEIGHT = 540.0f;
    public static final float VIEW_WIDTH = 960.0f;
    protected OrthographicCamera camera;
    protected GameStateController gsm;
    protected Skin skin;
    protected Stage stage;
    protected FitViewport viewPort;

    public abstract void dispose();

    public abstract int getID();

    public abstract void init();

    public abstract void onEnter();

    public abstract void onExit();

    public abstract void onResize(int i, int i2);

    public abstract void render();

    public abstract void update(float f);

    State(GameStateController gsm) {
        this.gsm = gsm;
    }
}

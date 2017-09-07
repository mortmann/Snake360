package com.snake.game;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;

public class MainClass extends ApplicationAdapter {
    private GameStateController gsm;

    public void create() {
        this.gsm = new GameStateController();
    }

    public void render() {
        Gdx.gl.glClear(16384);
        this.gsm.getState().update(Gdx.graphics.getDeltaTime());
        this.gsm.getState().render();
    }

    public void resize(int width, int height) {
        this.gsm.resize(width, height);
    }

    public void pause() {
    }

    public void resume() {
    }

    public void dispose() {
    }
}

package com.snake.game;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;

public class MainClass extends ApplicationAdapter {

	private GameStateController gsm;

	@Override
	public void create () {
		gsm = new GameStateController();
	}

	@Override
	public void render () {
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
		gsm.getState().update(Gdx.graphics.getDeltaTime());
		gsm.getState().render();
	}
	@Override
	public void resize (int width, int height) {
		gsm.resize(width,height);
	}

	@Override
	public void pause () {
		
	}

	@Override
	public void resume () {
	}

	@Override
	public void dispose () {
	}
}
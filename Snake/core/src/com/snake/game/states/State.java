package com.snake.game.states;

import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.snake.game.GameStateController;


public abstract class State {
	public static int ID;
	public static final float VIEW_WIDTH = 960;
	public static final float VIEW_HEIGHT = 540;
	protected GameStateController gsm;
	protected FitViewport viewPort;
	protected OrthographicCamera camera;
	protected Stage stage;
	protected Skin skin;
	
	State(GameStateController gsm){
		this.gsm =gsm;
		//init();
	}
	public abstract int getID();
	public abstract void onResize(int screenWidth, int screenHeight);
	public abstract void render();
	public abstract void update(float delta);
	public abstract void init();
	public abstract void onExit();
	public abstract void onEnter();
	public abstract void dispose();
}

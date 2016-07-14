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

public class PauseMenu extends State{
	public static final int ID = 7;
	private TextButton resumeButton;
	private TextButton exitButton;
	private TextButton optionButton;
	private Skin skin;
	private Stage stage;
	
	
	public PauseMenu(GameStateController gsm){
		super(gsm);
	}
	
	@Override
	public void init() {
		camera = new OrthographicCamera(Gdx.graphics.getWidth(),Gdx.graphics.getHeight());
		
		viewPort=new FitViewport(960,540);// TODO ExtendViewport(960, 540,camera); MAYBE
		viewPort.setCamera(camera);
		viewPort.apply();
		float x=960/2 - 100;
		float y=540/1.25f *1f;
		
		
		camera.position.set(viewPort.getWorldWidth(), camera.viewportHeight,0);
		skin = new Skin(Gdx.files.internal("data/uiskin.json"));
		resumeButton=new TextButton("Resume", skin);
		resumeButton.setBounds(x, y- 125, 200, 50);
		optionButton=new TextButton("Options", skin);
		optionButton.setBounds(x, y-175, 200, 50);
		exitButton=new TextButton("Exit to mainmenu", skin);
		exitButton.setBounds(x, y-225, 200, 50);
		stage=new Stage();
		stage.addActor(exitButton);
		stage.addActor(optionButton);
		stage.setViewport(viewPort);
		stage.addActor(resumeButton);
		stage.getCamera().update();
		stage.getViewport().apply();
		stage.getViewport().update(Gdx.graphics.getWidth(),Gdx.graphics.getHeight(),true); 
		
		Gdx.input.setInputProcessor(stage);
		resumeButton.addListener(new ClickListener(){
			@Override
			public void clicked(InputEvent event, float x, float y)  {
				gsm.backState();
			}
		});	
		optionButton.addListener(new ClickListener(){
			@Override
			public void clicked(InputEvent event, float x, float y)  {
				gsm.showOptions();
			}
		});	
		exitButton.addListener(new ClickListener(){
			@Override
			public void clicked(InputEvent event, float x, float y)  {
				gsm.openMainMenu();
			}
		});	
	}
	@Override
	public void onResize(int screenWidth, int screenHeight) {
		stage.getViewport().update(screenWidth,screenHeight,true); 
	}

	@Override
	public void render() {
		camera.update();
		stage.getCamera().update();
		stage.draw();
	}

	@Override
	public void update(float delta) {
		stage.act();
		
	}



	@Override
	public void onExit() {
		stage.dispose();
		Gdx.input.setInputProcessor(null);
	}
	@Override
	public int getID() {
		return ID;
	}

	@Override
	public void onEnter() {
		Gdx.input.setInputProcessor(stage);
		
	}

	@Override
	public void dispose() {
		// TODO Auto-generated method stub
		
	}

}

package com.snake.game.states;

import com.badlogic.gdx.Application;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.snake.game.GameStateController;


public class Mainmenu extends State {
	public Mainmenu(GameStateController gsm) {
		super(gsm);
	}
	
	public static final int ID = 1;

	private TextButton playButton;
	private TextButton optionButton;
	private TextButton highscoreButton;
	private TextButton exitButton;

	
	@Override
	public void init(){
		//TODO change the music to mainmenu music
//		MyMusic music = new MyMusic();
//		music.playRadio();
		camera = new OrthographicCamera(VIEW_WIDTH,VIEW_HEIGHT);
		
		viewPort=new FitViewport(VIEW_WIDTH,VIEW_HEIGHT);
		viewPort.setCamera(camera);
		
		viewPort.apply();
		camera.position.set(viewPort.getWorldWidth(), viewPort.getWorldHeight(),0);
		
		stage=new Stage();
		Gdx.input.setInputProcessor(stage);
		switch(Gdx.app.getType()) {
		   case Android:
		   skin = new Skin(Gdx.files.internal("data/uiskin.json"));
		   break;
		   case Desktop:
		   skin = new Skin(Gdx.files.internal("data/uiskin.json"));
		   break;
		   case WebGL:break;
		default:
			break;
		}
		
		float x=viewPort.getWorldWidth()/2 - 150;
		float y=viewPort.getWorldHeight();
		
		playButton=new TextButton("Play", skin);
		playButton.setBounds(x-50, y-250, 400,200);

		optionButton=new TextButton("Options", skin);
		optionButton.setBounds(x,y-450, 300, 200);
		
		highscoreButton=new TextButton("Highscore", skin);
		highscoreButton.setBounds(0,y/2-75, 200, 150);
		
		exitButton=new TextButton("Exit", skin);
		exitButton.setBounds(viewPort.getWorldWidth()-200,0, 200, 100);

		stage.addActor(playButton);
		stage.addActor(optionButton);
		stage.addActor(highscoreButton);
		stage.addActor(exitButton);	
		stage.setViewport(viewPort);
		stage.getCamera().update();
		stage.getViewport().apply();
		stage.getViewport().update(Gdx.graphics.getWidth(),Gdx.graphics.getHeight(),true); 
		//Button handling
		playButton.addListener(new ClickListener(){
			@Override
			public void clicked(InputEvent event, float x, float y)  {
				gsm.gameSelect();
			}
		});	
		optionButton.addListener(new ClickListener(){
			@Override
			public void clicked(InputEvent event, float x, float y)  {
				gsm.showOptions();
			}
		});	
		highscoreButton.addListener(new ClickListener(){
			@Override
			public void clicked(InputEvent event, float x, float y)  {
				gsm.showScore();
			}
		});	
		exitButton.addListener(new ClickListener(){
			@Override
			public void clicked(InputEvent event, float x, float y)  {
				Gdx.app.exit();
			}
		});	

	}

	@Override
	public void render() {
		camera.update();
		stage.getCamera().update();
		stage.draw();
	}

	@Override
	public void update(float delta) {
		viewPort.update(Gdx.graphics.getWidth(),Gdx.graphics.getHeight());
		stage.act();

	}

	@Override
	public int getID() {
		return ID;
	}

	@Override
	public void onExit() {
		stage.dispose();
		Gdx.input.setInputProcessor(null);
	}

	@Override
	public void onResize(int screenWidth, int screenHeight) {
			viewPort.update(screenWidth, screenHeight);
			stage.getViewport().update(960, 540,true); 
			
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

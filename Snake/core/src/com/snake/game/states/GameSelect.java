package com.snake.game.states;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Preferences;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.CheckBox;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.snake.game.GameStateController;
import com.snake.game.util.Difficulty;

public class GameSelect extends State{

	private Table table;
	
	private TextButton buttonBack;
	
	private CheckBox buttonEasyMode;	
	private CheckBox buttonNormalMode;
	private CheckBox buttonHardMode;
	private CheckBox buttonVeryHardMode;
	
	private TextButton buttonFlappyMode;
	private TextButton buttonNormalGameMode;
	private TextButton buttonBigMapMode;
	private Preferences prefs;
	private String difficulty;
	
	
	public GameSelect(GameStateController gsm) {
		super(gsm);
	}
	@Override
	public void init() {
		camera = new OrthographicCamera(VIEW_WIDTH,VIEW_HEIGHT);
		prefs = Gdx.app.getPreferences("snake360");

		viewPort=new FitViewport(VIEW_WIDTH,VIEW_HEIGHT);
		camera.position.set(VIEW_WIDTH/2,VIEW_HEIGHT/2,1);
		viewPort.setCamera(camera);
		viewPort.apply();
		difficulty=prefs.getString("Difficulty", "Normal");
		stage=new Stage();
		Gdx.input.setInputProcessor(stage);
		skin = new Skin(Gdx.files.internal("data/uiskin.json"));
		stage.setViewport(viewPort);
		stage.getViewport().update(Gdx.graphics.getWidth(),Gdx.graphics.getHeight(),true); 
		//TODO Change to Image button
		//GAMEMODE BUTTONS HERE
		
		buttonEasyMode = new CheckBox("Easy Mode", skin);
		buttonEasyMode.addListener(new ClickListener(){
			@Override
			public void clicked(InputEvent event, float x, float y)  {
				difficulty=Difficulty.Easy.name();
				buttonNormalMode.setChecked(false);
				buttonHardMode.setChecked(false);
				buttonVeryHardMode.setChecked(false);
				buttonEasyMode.setChecked(true);
			}
		});
		buttonNormalMode = new CheckBox("Normal Mode", skin);
		buttonNormalMode.addListener(new ClickListener(){
			@Override
			public void clicked(InputEvent event, float x, float y)  {
				difficulty=Difficulty.Normal.name();
				buttonEasyMode.setChecked(false);
				buttonHardMode.setChecked(false);
				buttonVeryHardMode.setChecked(false);
				buttonNormalMode.setChecked(true);
			}
		});
		buttonHardMode = new CheckBox("Hard Mode", skin);
		buttonHardMode.addListener(new ClickListener(){
			@Override
			public void clicked(InputEvent event, float x, float y)  {
				difficulty=Difficulty.Hard.name();
				buttonNormalMode.setChecked(false);
				buttonEasyMode.setChecked(false);
				buttonVeryHardMode.setChecked(false);
				buttonHardMode.setChecked(true);
			}
		});	
		buttonVeryHardMode = new CheckBox("Very Hard", skin);
		buttonVeryHardMode.addListener(new ClickListener(){
			@Override
			public void clicked(InputEvent event, float x, float y)  {
				difficulty=Difficulty.VeryHard.name();
				buttonNormalMode.setChecked(false);
				buttonHardMode.setChecked(false);
				buttonEasyMode.setChecked(false);
				buttonVeryHardMode.setChecked(true);
			}
		});

		buttonFlappyMode = new TextButton("Flappy Mode", skin);
		buttonFlappyMode.addListener(new ClickListener(){
			@Override
			public void clicked(InputEvent event, float x, float y)  {
				prefs.putString("Difficulty", difficulty);
				prefs.flush();
				gsm.startGame("Flappy",difficulty);
			}
		});
		buttonNormalGameMode = new TextButton("Classic Mode", skin);
		buttonNormalGameMode.addListener(new ClickListener(){
			@Override
			public void clicked(InputEvent event, float x, float y)  {
				prefs.putString("Difficulty", difficulty);
				prefs.flush();
				gsm.startGame("Classic",difficulty);
			}
		});
		buttonBigMapMode = new TextButton("Big Map", skin);
		buttonBigMapMode.addListener(new ClickListener(){
			@Override
			public void clicked(InputEvent event, float x, float y)  {
				prefs.putString("Difficulty", difficulty);
				prefs.flush();
				gsm.startGame("BigMap",difficulty);
			}
		});

		if(difficulty.contains("Easy")){
			buttonEasyMode.setChecked(true);
		} else 
		if(difficulty.contains("Normal")){
			buttonNormalMode.setChecked(true);
		} else 
		if(difficulty.contains("VeryHard")){
			buttonVeryHardMode.setChecked(true);
		} else 
		if(difficulty.contains("Hard")){
			buttonHardMode.setChecked(true);
		} 
		//MENUBUTTON HERE
		buttonBack = new TextButton("BACK", skin);
		buttonBack.addListener(new ClickListener(){
			@Override
			public void clicked(InputEvent event, float x, float y)  {
				gsm.openMainMenu();
			}
		});
		table = new Table();
		table.setBounds(130, 70, 700, 400);
		table.add(buttonEasyMode).size(175, 175);
		table.add(buttonNormalMode).size(175, 175);
		table.add(buttonHardMode).size(175, 175);
		table.add(buttonVeryHardMode).size(175, 175);

		table.add().size(175, 200).row();
		table.add(buttonNormalGameMode).size(175, 175);
		table.add(buttonFlappyMode).size(175, 175);
		table.add(buttonBigMapMode).size(175, 175);
		table.add().size(175, 175);
		table.add().size(175, 200).row();
		table.add().size(175, 100);
		table.add().size(175, 100);
		table.add().size(175, 100);
		table.add().size(175, 100);
		table.add(buttonBack).size(175, 75);
		table.debug();
		stage.addActor(table);
	}
	@Override
	public int getID() {
		return 0;
	}

	@Override
	public void onResize(int screenWidth, int screenHeight) {
		viewPort.update(screenWidth, screenHeight);
		stage.getViewport().update(screenWidth, screenHeight,true); 
	}

	@Override
	public void render() {
		stage.draw();

		
	}

	@Override
	public void update(float delta) {
		
		
		stage.act(delta);
	}



	@Override
	public void onExit() {
		
	}

	@Override
	public void onEnter() {
		
	}

	@Override
	public void dispose() {
		
	}

}

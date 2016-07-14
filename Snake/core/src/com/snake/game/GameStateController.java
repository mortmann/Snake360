package com.snake.game;


import java.util.ArrayList;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Preferences;
import com.snake.game.states.*;
public class GameStateController  {
	public static final float gameVersionID = 0.001f;
	private ArrayList<State> states;
	private State state;
	private int height;
	private int width;
	private float soundVolume=0.1f;
	private float musicVolume=0.1f;
	private State backState; //used for pauseScreen
	private State optionsState; //used for opening the optionsMenu
	private String mapName;
	
	public GameStateController()  {

	Preferences prefs = Gdx.app.getPreferences("gdxgame");
	
	soundVolume=prefs.getFloat("soundVolume", 0.5f);
	musicVolume=prefs.getFloat("musicVolume", 0.5f);
	height=Gdx.graphics.getHeight();
	width=Gdx.graphics.getWidth();
    //add Gamestates
    states=new ArrayList<State>();
    state=new Mainmenu(this);
    state.init();
    
    states.add(state); // ID 1
    
    }
//    public void changeState(int id){
//    	state.onExit();
//    	if(state.toString().contains("MyGame")){
//    		states.set(4, new GameState(this) );
//    	}
//    	state=states.get(id);
//    	state.init();
//    }	
    public void showOptions() {
    	System.out.println(state.getID());
    	optionsState=state;
    	state=new Options(this);
		state.init();
	}
    public void optionsBack() {
    	state.onExit();
    	state=optionsState;
    	state.onResize(this.width, this.height);
    	state.init();
	}
    public void pauseMenu(){
    	backState=state;
    	state=new PauseMenu(this);

		state.init();
    }
    public void backState() {
    	state=backState;
    	state.onEnter();
	}
    public State getState() {
		return state;
	}
	public int getWidth() {
		return width;
	}
	public int getHeight() {
		return height;
	}
	public float getSoundVolume() {
		return soundVolume;
	}
	public void setSoundVolume(float volume) {
		this.soundVolume = volume;
	}
	public float getMusicVolume() {
		return musicVolume;
	}
	public void setMusicVolume(float musicVolume) {
		this.musicVolume = musicVolume;
	}
	public void setSelectedMap(String mapName) {
		this.mapName=mapName;	
	}
	public String getMapName() {
		return mapName;
	}
	public void resize(int width, int height) {
		state.onResize(width, height);
	}
	public void setHeight(int i) {
		this.height=i;
		
	}
	public void setWidth(int i) {
		this.width=i;
	}
	public void startGame(String gamemode, String difficulty) {
		state.onExit();
		GameState game=new GameState(this);
		state=game;
		state.init();
		game.setGameMode(gamemode,difficulty);
	}
	public void startGame() {
		state.onExit();
		GameState game=new GameState(this);
		state=game;
		state.init();
	}
	public void gameSelect() {
		state.onExit();
		state=new GameSelect(this);
		state.init();
	}
	public void openMainMenu() {
		state.onExit();
		state=new Mainmenu(this);
		state.init();
	}
	public void showScore() {
    	backState=state;
    	state=new Highscore(this);

		state.init();
	}



}
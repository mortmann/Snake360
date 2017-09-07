package com.snake.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Preferences;
import com.snake.game.states.GameSelect;
import com.snake.game.states.GameState;
import com.snake.game.states.Highscore;
import com.snake.game.states.Mainmenu;
import com.snake.game.states.Options;
import com.snake.game.states.PauseMenu;
import com.snake.game.states.State;
import java.util.ArrayList;

public class GameStateController {
    public static final float gameVersionID = 0.001f;
    private State backState;
    private int height;
    private String mapName;
    private float musicVolume = 0.1f;
    private State optionsState;
    private float soundVolume = 0.1f;
    private State state;
    private ArrayList<State> states;
    private int width;

    public GameStateController() {
        Preferences prefs = Gdx.app.getPreferences("gdxgame");
        this.soundVolume = prefs.getFloat("soundVolume", 0.5f);
        this.musicVolume = prefs.getFloat("musicVolume", 0.5f);
        this.height = Gdx.graphics.getHeight();
        this.width = Gdx.graphics.getWidth();
        this.states = new ArrayList();
        this.state = new Mainmenu(this);
        this.state.init();
        this.states.add(this.state);
    }

    public void showOptions() {
        System.out.println(this.state.getID());
        this.optionsState = this.state;
        this.state = new Options(this);
        this.state.init();
    }

    public void optionsBack() {
        this.state.onExit();
        this.state = this.optionsState;
        this.state.onResize(this.width, this.height);
        this.state.init();
    }

    public void pauseMenu() {
        this.backState = this.state;
        this.state = new PauseMenu(this);
        this.state.init();
    }

    public void backState() {
        this.state = this.backState;
        this.state.onEnter();
    }

    public State getState() {
        return this.state;
    }

    public int getWidth() {
        return this.width;
    }

    public int getHeight() {
        return this.height;
    }

    public float getSoundVolume() {
        return this.soundVolume;
    }

    public void setSoundVolume(float volume) {
        this.soundVolume = volume;
    }

    public float getMusicVolume() {
        return this.musicVolume;
    }

    public void setMusicVolume(float musicVolume) {
        this.musicVolume = musicVolume;
    }

    public void setSelectedMap(String mapName) {
        this.mapName = mapName;
    }

    public String getMapName() {
        return this.mapName;
    }

    public void resize(int width, int height) {
        this.state.onResize(width, height);
    }

    public void setHeight(int i) {
        this.height = i;
    }

    public void setWidth(int i) {
        this.width = i;
    }

    public void startGame(String gamemode, String difficulty) {
        this.state.onExit();
        GameState game = new GameState(this);
        this.state = game;
        this.state.init();
        game.setGameMode(gamemode, difficulty);
    }

    public void startGame() {
        this.state.onExit();
        this.state = new GameState(this);
        this.state.init();
    }

    public void gameSelect() {
        this.state.onExit();
        this.state = new GameSelect(this);
        this.state.init();
    }

    public void openMainMenu() {
        this.state.onExit();
        this.state = new Mainmenu(this);
        this.state.init();
    }

    public void showScore() {
        this.backState = this.state;
        this.state = new Highscore(this);
        this.state.init();
    }
}

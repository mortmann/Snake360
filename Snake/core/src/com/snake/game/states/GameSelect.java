package com.snake.game.states;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Preferences;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.Touchable;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.snake.game.GameStateController;
import com.snake.game.util.Difficulty;

public class GameSelect extends State {
    private TextButton buttonBack;
    private TextButton buttonBigMapMode;
    private TextButton buttonEasyMode;
    private TextButton buttonFlappyMode;
    private TextButton buttonHardMode;
    private TextButton buttonNormalGameMode;
    private TextButton buttonNormalMode;
    private TextButton buttonVeryHardMode;
    private String difficulty;
    private Preferences prefs;
    private Table table;

    public GameSelect(GameStateController gsm) {
        super(gsm);
    }

    public void init() {
        this.camera = new OrthographicCamera(State.VIEW_WIDTH, State.VIEW_HEIGHT);
        this.prefs = Gdx.app.getPreferences("snake360");
        this.viewPort = new FitViewport(State.VIEW_WIDTH, State.VIEW_HEIGHT);
        this.camera.position.set(480.0f, 270.0f, 1.0f);
        this.viewPort.setCamera(this.camera);
        this.viewPort.apply();
        this.difficulty = this.prefs.getString("Difficulty", "Normal");
        this.stage = new Stage();
        Gdx.input.setInputProcessor(this.stage);
        this.skin = new Skin(Gdx.files.internal(Mainmenu.uiskin));
        this.stage.setViewport(this.viewPort);
        this.stage.getViewport().update(Gdx.graphics.getWidth(), Gdx.graphics.getHeight(), true);
        this.buttonEasyMode = new TextButton("Easy Mode", this.skin, "checkbox");
        this.buttonEasyMode.addListener(new ClickListener() {
            public void clicked(InputEvent event, float x, float y) {
                GameSelect.this.difficulty = Difficulty.Easy.name();
                GameSelect.this.buttonNormalMode.setChecked(false);
                GameSelect.this.buttonHardMode.setChecked(false);
                GameSelect.this.buttonVeryHardMode.setChecked(false);
                GameSelect.this.buttonEasyMode.setChecked(true);
            }
        });
        this.buttonNormalMode = new TextButton("Normal Mode", this.skin, "checkbox");
        this.buttonNormalMode.addListener(new ClickListener() {
            public void clicked(InputEvent event, float x, float y) {
                GameSelect.this.difficulty = Difficulty.Normal.name();
                GameSelect.this.buttonEasyMode.setChecked(false);
                GameSelect.this.buttonHardMode.setChecked(false);
                GameSelect.this.buttonVeryHardMode.setChecked(false);
                GameSelect.this.buttonNormalMode.setChecked(true);
            }
        });
        this.buttonHardMode = new TextButton("Hard Mode", this.skin, "checkbox");
        this.buttonHardMode.addListener(new ClickListener() {
            public void clicked(InputEvent event, float x, float y) {
                GameSelect.this.difficulty = Difficulty.Hard.name();
                GameSelect.this.buttonNormalMode.setChecked(false);
                GameSelect.this.buttonEasyMode.setChecked(false);
                GameSelect.this.buttonVeryHardMode.setChecked(false);
                GameSelect.this.buttonHardMode.setChecked(true);
            }
        });
        this.buttonVeryHardMode = new TextButton("Very Hard Mode", this.skin, "checkbox");
        this.buttonVeryHardMode.addListener(new ClickListener() {
            public void clicked(InputEvent event, float x, float y) {
                GameSelect.this.difficulty = Difficulty.VeryHard.name();
                GameSelect.this.buttonNormalMode.setChecked(false);
                GameSelect.this.buttonHardMode.setChecked(false);
                GameSelect.this.buttonEasyMode.setChecked(false);
                GameSelect.this.buttonVeryHardMode.setChecked(true);
            }
        });
        this.buttonFlappyMode = new TextButton("Flappy Mode", this.skin);
        this.buttonFlappyMode.addListener(new ClickListener() {
            public void clicked(InputEvent event, float x, float y) {
                GameSelect.this.prefs.putString("Difficulty", GameSelect.this.difficulty);
                GameSelect.this.prefs.flush();
                GameSelect.this.gsm.startGame("Flappy", GameSelect.this.difficulty);
            }
        });
        this.buttonNormalGameMode = new TextButton("Classic Mode", this.skin);
        this.buttonNormalGameMode.addListener(new ClickListener() {
            public void clicked(InputEvent event, float x, float y) {
                GameSelect.this.prefs.putString("Difficulty", GameSelect.this.difficulty);
                GameSelect.this.prefs.flush();
                GameSelect.this.gsm.startGame("Classic", GameSelect.this.difficulty);
            }
        });
        this.buttonBigMapMode = new TextButton("Big Map\n(Coming Maybe)", this.skin, "disabled");
        this.buttonBigMapMode.addListener(new ClickListener() {
            public void clicked(InputEvent event, float x, float y) {
                GameSelect.this.prefs.putString("Difficulty", GameSelect.this.difficulty);
                GameSelect.this.prefs.flush();
                GameSelect.this.gsm.startGame("BigMap", GameSelect.this.difficulty);
            }
        });
        this.buttonBigMapMode.setDisabled(true);
        this.buttonBigMapMode.setTouchable(Touchable.disabled);
        if (this.difficulty.contains("Easy")) {
            this.buttonEasyMode.setChecked(true);
        } else if (this.difficulty.contains("Normal")) {
            this.buttonNormalMode.setChecked(true);
        } else if (this.difficulty.contains("VeryHard")) {
            this.buttonVeryHardMode.setChecked(true);
        } else if (this.difficulty.contains("Hard")) {
            this.buttonHardMode.setChecked(true);
        }
        this.buttonBack = new TextButton("BACK", this.skin);
        this.buttonBack.addListener(new ClickListener() {
            public void clicked(InputEvent event, float x, float y) {
                GameSelect.this.gsm.openMainMenu();
            }
        });
        this.table = new Table();
        this.table.setBounds(225.0f, 100.0f, 700.0f, 400.0f);
        this.table.add(this.buttonEasyMode).fill().size(175.0f, 175.0f);
        this.table.add(this.buttonNormalMode).fill().size(175.0f, 175.0f);
        this.table.add(this.buttonHardMode).fill().size(175.0f, 175.0f);
        this.table.add(this.buttonVeryHardMode).fill().size(175.0f, 175.0f);
        this.table.add().size(175.0f, 200.0f).row();
        this.table.add(this.buttonNormalGameMode).size(175.0f, 175.0f);
        this.table.add(this.buttonFlappyMode).size(175.0f, 175.0f);
        this.table.add(this.buttonBigMapMode).size(175.0f, 175.0f);
        this.table.add(this.buttonBack).size(75.0f, 75.0f).padRight(10.0f).padBottom(10.0f).align(20);
        this.table.add().size(175.0f, 200.0f).row();
        this.stage.addActor(this.table);
    }

    public int getID() {
        return 0;
    }

    public void onResize(int screenWidth, int screenHeight) {
        this.viewPort.update(screenWidth, screenHeight);
        this.stage.getViewport().update(screenWidth, screenHeight, true);
    }

    public void render() {
        this.stage.draw();
    }

    public void update(float delta) {
        this.stage.act(delta);
    }

    public void onExit() {
    }

    public void onEnter() {
    }

    public void dispose() {
    }
}

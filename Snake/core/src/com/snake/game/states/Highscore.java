package com.snake.game.states;

import com.badlogic.gdx.Gdx;
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
import com.snake.game.util.MyClient;
import com.snake.game.util.ScoreFileHandler;
import com.snake.game.util.Scoreboard;
import java.util.ArrayList;
import java.util.Hashtable;

public class Highscore extends State {
    private static final float HEIGHT_OF_BUTTONTABLE = 150.0f;
    private static final float HEIGHT_OF_SCOREBOARD = 250.0f;
    private static final int SIZE_OF_SCOREBOARD = 10;
    private static final float WIDTH_OF_BUTTONTABLE = 300.0f;
    private static final float WIDTH_OF_SCOREBOARD = ((Scoreboard.getNameLabelWidth() + Scoreboard.getScoreLabelWidth()) + Scoreboard.getSpaceWidth());
    private TextButton buttonBack;
    private TextButton buttonBig;
    private TextButton buttonClassic;
    private TextButton buttonEasy;
    private TextButton buttonFlappy;
    private TextButton buttonHard;
    private TextButton buttonNormal;
    private TextButton buttonOffline;
    private TextButton buttonOnline;
    private Table buttonTable;
    private TextButton buttonVeryHard;
    private String difficulty = Difficulty.Normal.name();
    private TextButton disabledDifficultyButton;
    private TextButton disabledGamemodeButton;
    private String gamemode = "Classic";
    private Hashtable<String, ArrayList<String>> hashTopScoresOnline;
    private boolean offline = true;
    private Scoreboard scoreBoard;
    private String scoreBoardName;
    private Table scoreTable;

    public Highscore(GameStateController gsm) {
        super(gsm);
    }

    public int getID() {
        return 0;
    }

    public void onResize(int screenWidth, int screenHeight) {
    }

    public void render() {
        this.stage.draw();
    }

    public void update(float delta) {
        this.stage.act();
    }

    public void init() {
        this.camera = new OrthographicCamera(State.VIEW_WIDTH, State.VIEW_HEIGHT);
        this.viewPort = new FitViewport(State.VIEW_WIDTH, State.VIEW_HEIGHT);
        this.viewPort.setCamera(this.camera);
        this.scoreBoardName = "Normalclassic";
        this.viewPort.apply();
        this.camera.position.set(this.viewPort.getWorldWidth(), this.viewPort.getWorldHeight(), 0.0f);
        this.stage = new Stage();
        Gdx.input.setInputProcessor(this.stage);
        this.skin = new Skin(Gdx.files.internal(Mainmenu.uiskin));
        this.stage.setViewport(this.viewPort);
        this.stage.getCamera().update();
        this.stage.getViewport().apply();
        this.stage.getViewport().update(Gdx.graphics.getWidth(), Gdx.graphics.getHeight(), true);
        MyClient.setHighscoreScreen(this);
        this.buttonBack = new TextButton("BACK", this.skin, "checkbox");
        this.buttonBack.addListener(new ClickListener() {
            public void clicked(InputEvent event, float x, float y) {
                Highscore.this.gsm.openMainMenu();
            }
        });
        this.buttonClassic = new TextButton("Classic", this.skin, "checkbox");
        this.buttonClassic.addListener(new ClickListener() {
            public void clicked(InputEvent event, float x, float y) {
                Highscore.this.gamemode = "Classic";
                System.out.println("normal");
                Highscore.this.changeGamemodeButton(Highscore.this.buttonClassic);
            }
        });
        this.buttonFlappy = new TextButton("Flappy", this.skin, "checkbox");
        this.buttonFlappy.addListener(new ClickListener() {
            public void clicked(InputEvent event, float x, float y) {
                Highscore.this.gamemode = "Flappy";
                Highscore.this.changeGamemodeButton(Highscore.this.buttonFlappy);
            }
        });
        this.buttonBig = new TextButton("Big", this.skin, "disabled");
        this.buttonBig.setTouchable(Touchable.disabled);
        this.buttonBig.addListener(new ClickListener() {
            public void clicked(InputEvent event, float x, float y) {
                Highscore.this.gamemode = "Big";
                Highscore.this.changeGamemodeButton(Highscore.this.buttonBig);
            }
        });
        this.buttonEasy = new TextButton("Easy", this.skin, "checkbox");
        this.buttonEasy.addListener(new ClickListener() {
            public void clicked(InputEvent event, float x, float y) {
                Highscore.this.difficulty = Difficulty.Easy.name();
                Highscore.this.changeDifficultyButton(Highscore.this.buttonEasy);
            }
        });
        this.buttonNormal = new TextButton("Normal", this.skin, "checkbox");
        this.buttonNormal.addListener(new ClickListener() {
            public void clicked(InputEvent event, float x, float y) {
                Highscore.this.difficulty = Difficulty.Normal.name();
                Highscore.this.changeDifficultyButton(Highscore.this.buttonNormal);
            }
        });
        this.buttonHard = new TextButton("Hard", this.skin, "checkbox");
        this.buttonHard.addListener(new ClickListener() {
            public void clicked(InputEvent event, float x, float y) {
                Highscore.this.difficulty = Difficulty.Hard.name();
                Highscore.this.changeDifficultyButton(Highscore.this.buttonHard);
            }
        });
        this.buttonVeryHard = new TextButton("Very", this.skin, "checkbox");
        this.buttonVeryHard.addListener(new ClickListener() {
            public void clicked(InputEvent event, float x, float y) {
                Highscore.this.difficulty = Difficulty.VeryHard.name();
                Highscore.this.changeDifficultyButton(Highscore.this.buttonVeryHard);
            }
        });
        this.buttonOffline = new TextButton("Offline", this.skin, "checkbox");
        this.buttonOffline.addListener(new ClickListener() {
            public void clicked(InputEvent event, float x, float y) {
                Highscore.this.offline = true;
                Highscore.this.buttonOnline.setChecked(false);
                Highscore.this.buttonOffline.setChecked(true);
                Highscore.this.buttonOnline.setDisabled(false);
                Highscore.this.buttonOffline.setDisabled(true);
                Highscore.this.updateScoreBoard();
            }
        });
        this.buttonOnline = new TextButton("Online", this.skin, "checkbox");
        this.buttonOnline.addListener(new ClickListener() {
            public void clicked(InputEvent event, float x, float y) {
                Highscore.this.offline = false;
                Highscore.this.buttonOnline.setChecked(true);
                Highscore.this.buttonOffline.setChecked(false);
                Highscore.this.buttonOnline.setDisabled(true);
                Highscore.this.buttonOffline.setDisabled(false);
                Highscore.this.updateScoreBoard();
            }
        });
        if (!MyClient.IsConnected()) {
            this.buttonOnline = new TextButton("Online", this.skin, "disabled");
            this.buttonOnline.setTouchable(Touchable.disabled);
        }
        this.buttonOffline.setChecked(true);
        this.buttonClassic.setChecked(true);
        this.buttonNormal.setChecked(true);
        this.disabledDifficultyButton = this.buttonNormal;
        this.disabledGamemodeButton = this.buttonClassic;
        this.buttonClassic.setTouchable(Touchable.disabled);
        this.buttonNormal.setTouchable(Touchable.disabled);
        this.hashTopScoresOnline = new Hashtable();
        this.buttonTable = new Table(this.skin);
        for (int i = 0; i < 12; i++) {
            this.buttonTable.add().setActorWidth(25.0f);
        }
        this.buttonTable.row();
        this.buttonTable.add(this.buttonOffline).size((float) HEIGHT_OF_BUTTONTABLE, 50.0f).fill().colspan(6);
        this.buttonTable.add(this.buttonOnline).size((float) HEIGHT_OF_BUTTONTABLE, 50.0f).fill().colspan(6).row();
        this.buttonTable.add(this.buttonClassic).size(100.0f, 50.0f).fill().colspan(4);
        this.buttonTable.add(this.buttonFlappy).size(100.0f, 50.0f).fill().colspan(4);
        this.buttonTable.add(this.buttonBig).size(100.0f, 50.0f).fill().colspan(4).row();
        this.buttonTable.add(this.buttonEasy).size(75.0f, 50.0f).fill().colspan(3);
        this.buttonTable.add(this.buttonNormal).size(75.0f, 50.0f).fill().colspan(3);
        this.buttonTable.add(this.buttonHard).size(75.0f, 50.0f).fill().colspan(3);
        this.buttonTable.add(this.buttonVeryHard).size(75.0f, 50.0f).fillX().colspan(3);
        this.buttonTable.setBounds(330.0f, 395.0f, WIDTH_OF_BUTTONTABLE, HEIGHT_OF_BUTTONTABLE);
        this.stage.addActor(this.buttonTable);
        this.scoreBoard = ScoreFileHandler.loadScoreTable("NormalClassic");
        if (this.scoreBoard == null) {
            System.out.println("null");
            this.scoreBoard = new Scoreboard(10, "NormalClassic");
        }
        setupScoreBoard();
        this.buttonBack.setBounds(0.0f, 0.0f, 175.0f, 100.0f);
        this.stage.addActor(this.buttonBack);
    }

    private void setupScoreBoard() {
        this.scoreTable = this.scoreBoard.getScoreTable(this.skin);
        this.scoreTable.setBounds(480.0f - (WIDTH_OF_SCOREBOARD / 2.0f), 145.0f, WIDTH_OF_SCOREBOARD, HEIGHT_OF_SCOREBOARD);
        this.stage.addActor(this.scoreTable);
    }

    public void onExit() {
    }

    public void onEnter() {
    }

    public void dispose() {
    }

    private void changeGamemodeButton(TextButton button) {
        button.setTouchable(Touchable.disabled);
        button.setChecked(true);
        if (this.disabledGamemodeButton != null) {
            this.disabledGamemodeButton.setChecked(false);
            this.disabledGamemodeButton.setTouchable(Touchable.enabled);
            this.disabledGamemodeButton = button;
            updateScoreBoard();
        }
    }

    private void changeDifficultyButton(TextButton button) {
        button.setTouchable(Touchable.disabled);
        button.setChecked(true);
        if (this.disabledDifficultyButton != null) {
            this.disabledDifficultyButton.setChecked(false);
            this.disabledDifficultyButton.setTouchable(Touchable.enabled);
            this.disabledDifficultyButton = button;
            updateScoreBoard();
        }
    }

    private void updateScoreBoard() {
        this.scoreBoardName = this.difficulty + this.gamemode;
        System.out.println("update");
        if (this.offline) {
            this.scoreBoard = null;
            this.stage.getActors().removeIndex(this.stage.getActors().indexOf(this.scoreTable, true));
            this.scoreBoard = ScoreFileHandler.loadScoreTable(this.scoreBoardName);
            System.out.println(this.scoreBoardName + this.scoreBoard);
            if (this.scoreBoard == null) {
                this.scoreBoard = new Scoreboard(10, this.scoreBoardName);
            }
            setupScoreBoard();
        } else if (this.hashTopScoresOnline.containsKey(this.scoreBoardName)) {
            this.stage.getActors().removeIndex(this.stage.getActors().indexOf(this.scoreTable, true));
            splitStringsIntoPieces((ArrayList) this.hashTopScoresOnline.get(this.scoreBoardName));
            setupScoreBoard();
        } else {
            System.out.println("GET HIGHSCORE " + this.scoreBoardName);
            MyClient.sendGetTopScore(this.scoreBoardName);
        }
    }

    public void setTopScore(String tableName, ArrayList<String> data) {
        System.out.println(this.hashTopScoresOnline + " " + tableName + " " + data);
        this.hashTopScoresOnline.put(tableName, data);
        updateScoreBoard();
    }

    public void splitStringsIntoPieces(ArrayList<String> data) {
        ArrayList<String> names = new ArrayList();
        ArrayList<Integer> scores = new ArrayList();
        int startRank = 0;
        for (int i = 0; i < data.size(); i++) {
            String[] strings = ((String) data.get(i)).split(";");
            if (strings.length >= 3) {
                if (i == 0) {
                    startRank = Integer.valueOf(strings[0]).intValue();
                }
                names.add(strings[1]);
                scores.add(Integer.valueOf(strings[2]));
            } else if (strings.length < 3) {
                names.add(strings[0]);
                scores.add(Integer.valueOf(strings[1]));
            }
        }
        this.scoreBoard = new Scoreboard(names, scores, startRank);
    }
}

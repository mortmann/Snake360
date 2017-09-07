package com.snake.game.states;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.physics.box2d.Box2DDebugRenderer;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Dialog;
import com.badlogic.gdx.scenes.scene2d.ui.ImageButton;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.utils.Drawable;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.snake.game.GameStateController;
import com.snake.game.entity.Snake;
import com.snake.game.map.BigMap;
import com.snake.game.map.ClassicMap;
import com.snake.game.map.FlappyMap;
import com.snake.game.map.Map;
import com.snake.game.util.MyClient;
import com.snake.game.util.ScoreFileHandler;
import com.snake.game.util.Scoreboard;

public class GameState extends State {
    private ImageButton backButton;
    private SpriteBatch batch;
    private ImageButton buttonLeft;
    private ImageButton buttonPause;
    private ImageButton buttonRight;
    private OrthographicCamera cameraStage;
    private float countdown = 3.0f;
    private Dialog dialogEndGame;
    private boolean dialogShown;
    private String difficulty;
    private int direction;
    private BitmapFont font;
    private String gamemode;
    private int level = 0;
    private Map map;
    private ImageButton nextLevelButton;
    private Box2DDebugRenderer renderer;
    private ImageButton replayButton;
    private Scoreboard scoreBoard;
    private Label scoreLabel;
    private Snake snake;
    private float tick;
    private FitViewport viewPortStage;

    public GameState(GameStateController gsm) {
        super(gsm);
    }

    public void init() {
        Gdx.input.setInputProcessor(this.stage);
        this.renderer = new Box2DDebugRenderer();
        this.camera = new OrthographicCamera(Map.MAX_X, Map.MAX_Y);
        this.viewPort = new FitViewport(Map.MAX_X, Map.MAX_Y);
        this.camera.position.set(4.7999997f, 2.7f, 1.0f);
        this.viewPort.setCamera(this.camera);
        this.viewPort.apply();
        this.stage = new Stage();
        Gdx.input.setInputProcessor(this.stage);
        this.skin = new Skin(Gdx.files.internal(Mainmenu.uiskin));
        this.cameraStage = new OrthographicCamera();
        this.cameraStage.setToOrtho(false, State.VIEW_WIDTH, State.VIEW_HEIGHT);
        this.viewPortStage = new FitViewport(State.VIEW_WIDTH, State.VIEW_HEIGHT);
        this.viewPortStage.setCamera(this.cameraStage);
        this.viewPortStage.apply();
        this.stage = new Stage();
        this.stage.setViewport(this.viewPortStage);
        this.stage.getViewport().update(Gdx.graphics.getWidth(), Gdx.graphics.getHeight(), true);
        Gdx.input.setInputProcessor(this.stage);
        this.dialogEndGame = new Dialog("", this.skin);
        this.backButton = new ImageButton(new TextureRegionDrawable(new TextureRegion(new Texture(Gdx.files.internal("gui/back.png")))));
        this.replayButton = new ImageButton(new TextureRegionDrawable(new TextureRegion(new Texture(Gdx.files.internal("gui/replay.png")))));
        this.nextLevelButton = new ImageButton(new TextureRegionDrawable(new TextureRegion(new Texture(Gdx.files.internal("gui/next.png")))));
        this.backButton.addListener(new ClickListener() {
            public void clicked(InputEvent event, float x, float y) {
                GameState.this.gsm.gameSelect();
            }
        });
        this.replayButton.addListener(new ClickListener() {
            public void clicked(InputEvent event, float x, float y) {
                GameState.this.map.closeDoor();
                GameState.this.dialogEndGame.hide(null);
                GameState.this.dialogEndGame.setVisible(false);
                GameState.this.dialogShown = false;
                GameState.this.level = 0;
                GameState.this.setUpSnakeMap();
            }
        });
        this.nextLevelButton.addListener(new ClickListener() {
            public void clicked(InputEvent event, float x, float y) {
                GameState.this.map.closeDoor();
                GameState.this.dialogEndGame.hide(null);
                GameState.this.dialogEndGame.setVisible(false);
                GameState.this.dialogShown = false;
                GameState gameState = GameState.this;
                gameState.level = gameState.level + 1;
                GameState.this.setUpSnakeMap();
            }
        });
        this.dialogEndGame.add(this.backButton).padBottom(5.0f);
        this.dialogEndGame.add(this.replayButton).padBottom(5.0f);
        this.dialogEndGame.add(this.nextLevelButton).padBottom(5.0f);
        this.dialogEndGame.setMovable(false);
        Drawable drawable = new TextureRegionDrawable(new TextureRegion(new Texture(Gdx.files.internal("gui/clear.png"))));
        this.buttonRight = new ImageButton(drawable);
        this.buttonLeft = new ImageButton(drawable);
        this.buttonLeft.setBounds(0.0f, 0.0f, 480.0f, State.VIEW_HEIGHT);
        this.buttonRight.setBounds(480.0f, 0.0f, 480.0f, State.VIEW_HEIGHT);
        this.buttonRight.addListener(new ClickListener() {
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                GameState.this.direction = -1;
                return true;
            }

            public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
                if (GameState.this.direction == -1) {
                    GameState.this.direction = 0;
                }
            }
        });
        this.buttonLeft.addListener(new ClickListener() {
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                GameState.this.direction = 1;
                return true;
            }

            public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
                if (GameState.this.direction == 1) {
                    GameState.this.direction = 0;
                }
            }
        });
        this.stage.addActor(this.buttonRight);
        this.stage.addActor(this.buttonLeft);
        this.buttonPause = new ImageButton(new TextureRegionDrawable(new TextureRegion(new Texture(Gdx.files.internal("gui/pauseButton.png")))));
        this.buttonPause.addListener(new ClickListener() {
            public void clicked(InputEvent event, float x, float y) {
                GameState.this.gsm.pauseMenu();
            }
        });
        this.buttonPause.setBounds(0.0f, 465.0f, 75.0f, 75.0f);
        this.scoreLabel = new Label((CharSequence) "0", this.skin);
        this.scoreLabel.setColor(255.0f, 0.0f, 0.0f, 1.0f);
        this.scoreLabel.setBounds(810.0f, 465.0f, 75.0f, 75.0f);
        this.stage.addActor(this.scoreLabel);
        this.stage.addActor(this.buttonPause);
        this.batch = new SpriteBatch();
        this.batch.setProjectionMatrix(this.viewPort.getCamera().combined);
        this.font = new BitmapFont();
    }

    private void setUpSnakeMap() {
        this.snake = new Snake();
        this.snake.setDifficulty(this.difficulty);
        if (this.gamemode.contains("Classic")) {
            if (!(this.snake == null || this.snake.isDead() || !this.snake.isWon())) {
                this.snake = new Snake(this.snake.getScore());
            }
            this.map = new ClassicMap(this.level, this.snake);
        } else if (this.gamemode.contains("Flappy")) {
            this.map = new FlappyMap(this.snake);
        } else if (this.gamemode.contains("Big")) {
            this.map = new BigMap(this.snake);
        }
        this.snake.setPosition(this.map.getSpawnPoint(), this.map.getWorld());
    }

    public int getID() {
        return 0;
    }

    public void onResize(int screenWidth, int screenHeight) {
        this.viewPort.update(screenWidth, screenHeight);
        this.stage.getViewport().update(screenWidth, screenHeight, true);
    }

    public void render() {
        this.batch.begin();
        this.map.render(this.batch);
        this.snake.render(this.batch);
        this.stage.getBatch().begin();
        if (this.countdown >= 0.0f) {
            this.font.draw(this.stage.getBatch(), new StringBuilder(String.valueOf(Math.round(this.countdown))).append("...").toString(), 480.0f, 270.0f);
        } else {
            this.font.newFontCache();
        }
        this.font.draw(this.stage.getBatch(), Integer.toString(Gdx.graphics.getFramesPerSecond()), 910.0f, 490.0f);
        this.stage.getBatch().end();
        this.batch.end();
        this.stage.draw();
    }

    public void update(float delta) {
        this.stage.act();
        if (this.countdown > 0.0f) {
            this.countdown -= delta;
            return;
        }
        this.scoreLabel.setText(Integer.toString(this.snake.getScore()));
        this.tick += delta;
        while (this.tick >= 0.016666f) {
            this.tick -= 0.016666f;
            if (!(this.snake.isDead() || (this.snake.isWon() && this.map.canWin()))) {
                this.map.update(0.016666f);
                this.snake.setTurnDir(this.direction);
                this.snake.update(0.016666f);
            }
        }
        if (((this.snake.isWon() && this.map.canWin()) || this.snake.isDead()) && !this.dialogShown) {
            this.dialogEndGame.setVisible(true);
            if (this.map.canWin() && this.snake.isWon()) {
                this.nextLevelButton.setVisible(true);
            } else {
                this.nextLevelButton.setVisible(false);
                if (this.scoreBoard.addScore("NAME_PLACEHOLDER", this.snake.getScore())) {
                    MyClient.sendHighScore(this.difficulty + this.gamemode, "NAME_PLACEHOLDER", this.snake.getScore());
                }
                
                ScoreFileHandler.saveScoreTable(this.scoreBoard);
            }
            this.dialogEndGame.show(this.stage);
            this.dialogShown = true;
        }
    }

    public void onExit() {
        System.out.println("exit");
    }

    public void onEnter() {
        this.countdown = 3.0f;
        Gdx.input.setInputProcessor(this.stage);
    }

    public void dispose() {
    }

    public void setGameMode(String gamemode, String difficulty) {
        this.gamemode = gamemode;
        this.difficulty = difficulty;
        setUpSnakeMap();
        System.out.println(new StringBuilder(String.valueOf(difficulty)).append(this.map.getNameOfGamemode()).toString());
        String nameScoreBoard = new StringBuilder(String.valueOf(difficulty)).append(this.map.getNameOfGamemode()).toString();
        scoreBoard = ScoreFileHandler.loadScoreTable(nameScoreBoard);
        if(scoreBoard==null){
        	this.scoreBoard = new Scoreboard(10, nameScoreBoard);
        } 
    }
}

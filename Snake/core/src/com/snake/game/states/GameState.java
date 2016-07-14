package com.snake.game.states;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Dialog;
import com.badlogic.gdx.scenes.scene2d.ui.ImageButton;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.snake.game.GameStateController;
import com.snake.game.entity.Snake;
import com.snake.game.map.BigMap;
import com.snake.game.map.FlappyMap;
import com.snake.game.map.Map;
import com.snake.game.util.MyClient;
import com.snake.game.util.ScoreFileHandler;
import com.snake.game.util.Scoreboard;
import com.snake.game.map.ClassicMap;

public class GameState extends State {

	private SpriteBatch batch;
	private ImageButton buttonRight;
	private ImageButton buttonLeft;
	private ImageButton buttonPause;
	private int direction;
	private Snake snake;
	private BitmapFont font;
	private Map map;
	private Label scoreLabel;
	private Dialog dialogEndGame;
	private ImageButton backButton;
	private ImageButton replayButton;
	private ImageButton nextLevelButton;
	private boolean dialogShown;
	private String gamemode;
	private int level=0;
	private String difficulty;
	private Scoreboard scoreBoard;
	private MyClient myClient;
	
	public GameState(GameStateController gsm) {
		super(gsm);
	}
	@Override
	public void init() {
		Gdx.input.setInputProcessor(stage);
		camera = new OrthographicCamera(VIEW_WIDTH,VIEW_HEIGHT);
		
		viewPort=new FitViewport(VIEW_WIDTH,VIEW_HEIGHT);
		camera.position.set(VIEW_WIDTH/2,VIEW_HEIGHT/2,1);
		viewPort.setCamera(camera);
		viewPort.apply();
		
		stage=new Stage();
		Gdx.input.setInputProcessor(stage);
		skin = new Skin(Gdx.files.internal("data/uiskin.json"));
		stage.setViewport(viewPort);
		stage.getViewport().update(Gdx.graphics.getWidth(),Gdx.graphics.getHeight(),true); 
		myClient =new MyClient();
		dialogEndGame=new Dialog("GameOver", skin);
		TextureRegionDrawable drawable =new TextureRegionDrawable(new TextureRegion(new Texture(Gdx.files.internal("gui/back.png"))));
		backButton=new ImageButton(drawable);
		drawable =new TextureRegionDrawable(new TextureRegion(new Texture(Gdx.files.internal("gui/replay.png"))));
		replayButton=new ImageButton(drawable);
		drawable =new TextureRegionDrawable(new TextureRegion(new Texture(Gdx.files.internal("gui/next.png"))));
		nextLevelButton=new ImageButton(drawable);
		backButton.addListener(new ClickListener(){
			@Override
			public void clicked(InputEvent event, float x, float y)  {
				gsm.gameSelect();
			}
		}); 
		replayButton.addListener(new ClickListener(){
			@Override
			public void clicked(InputEvent event, float x, float y)  {
				
				map.closeDoor();
				dialogEndGame.hide(null);
				dialogEndGame.setVisible(false);
				dialogShown=false;
				level=0;
				setUpSnakeMap();
			}
		});
		nextLevelButton.addListener(new ClickListener(){
			@Override
			public void clicked(InputEvent event, float x, float y)  {
				
				map.closeDoor();
				dialogEndGame.hide(null);
				dialogEndGame.setVisible(false);
				dialogShown=false;
				level++;
				setUpSnakeMap();
			}
		});
		dialogEndGame.add(backButton);
		dialogEndGame.add(replayButton);
		dialogEndGame.add(nextLevelButton);
		
		drawable =new TextureRegionDrawable(new TextureRegion(new Texture(Gdx.files.internal("gui/clear.png"))));
		buttonRight=new ImageButton(drawable);
		buttonLeft=new ImageButton(drawable);
		buttonLeft.setBounds(0,0, VIEW_WIDTH/2, VIEW_HEIGHT);
		buttonRight.setBounds(VIEW_WIDTH/2, 0, VIEW_WIDTH/2, VIEW_HEIGHT);
		buttonRight.addListener(new ClickListener(){
			@Override 
            public boolean touchDown (InputEvent event, float x, float y, int pointer, int button){
				direction=-1;
            	return true;
            }
            @Override 
        	public void touchUp (InputEvent event, float x, float y, int pointer, int button) {
            	if(direction==-1){
            		direction=0;
            	}
        	}
		});
		buttonLeft.addListener(new ClickListener(){
			@Override 
            public boolean touchDown (InputEvent event, float x, float y, int pointer, int button){
            	direction=1;
            	return true;
            }
            @Override 
        	public void touchUp (InputEvent event, float x, float y, int pointer, int button) {
            	if(direction==1){
            		direction=0;
            	}
        	}
		});
		stage.addActor(buttonRight);
		stage.addActor(buttonLeft);
		drawable =new TextureRegionDrawable(new TextureRegion(new Texture(Gdx.files.internal("gui/pauseButton.png"))));
		buttonPause=new ImageButton(drawable);
		buttonPause.addListener(new ClickListener(){
			@Override 
            public void clicked (InputEvent event, float x, float y){
            	gsm.pauseMenu();
            }
		});
		buttonPause.setBounds(0,VIEW_HEIGHT-75, 75, 75);
		
		scoreLabel = new Label("0", skin);
		scoreLabel.setColor(255, 0, 0, 1);
		scoreLabel.setBounds(VIEW_WIDTH-150,VIEW_HEIGHT-75, 75, 75);
		stage.addActor(scoreLabel);
		stage.addActor(buttonPause);
		
	
		
		batch=new SpriteBatch();
		batch.setProjectionMatrix(viewPort.getCamera().combined);
		font=new BitmapFont();
		

	}
	private void setUpSnakeMap(){
		if(gamemode.contains("Classic")){
			if(snake==null || snake.isDead()){
			snake = new Snake();
			} else {
				if(snake.isWon()){
					snake = new Snake(snake.getScore());
				}
			}
			map= new ClassicMap(level, snake);
		} else
		if(gamemode.contains("Flappy")){
			snake = new Snake();
			map= new FlappyMap(snake);
		} else
		if(gamemode.contains("Big")){
			snake = new Snake();
			map= new BigMap(snake);
		}
		snake.setPosition(map.getSpawnPoint());
		snake.setDifficulty(difficulty);
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
		
		batch.begin();
		map.render(batch);
		snake.render(batch);
		
		
		font.draw(batch,Integer.toString(Gdx.graphics.getFramesPerSecond()), VIEW_WIDTH-50,VIEW_HEIGHT-50);
		batch.end();
		stage.draw();
	}

	@Override
	public void update(float delta) {
		if(delta>0.033333f){
			delta=0.033333f;
		}		
		scoreLabel.setText(Integer.toString(snake.getScore()));
		if(!snake.isDead() && !snake.isWon()){	
			map.update(delta);
			
			snake.setTurnDir(direction);
			snake.update(delta);
			
		}

		  
		stage.act(delta);	
		if((snake.isWon()||snake.isDead()) && !dialogShown){
			dialogEndGame.setVisible(true);
			if(!snake.isWon()){
				nextLevelButton.setVisible(false);
				if(scoreBoard.addScore("NAME_PLACEHOLDER", snake.getScore())){
					myClient.sendHighScore(difficulty+gamemode, "NAME_PLACEHOLDER", snake.getScore());
				}
				ScoreFileHandler.saveScoreTable(scoreBoard);
			} else {
				nextLevelButton.setVisible(true);
			}
			
			dialogEndGame.show(stage);
			dialogShown=true;
		}
	}



	@Override
	public void onExit() {
		System.out.println("exit");
	}

	@Override
	public void onEnter() {
		Gdx.input.setInputProcessor(stage);
	}

	@Override
	public void dispose() {

	}
 
	public void setGameMode(String gamemode, String difficulty) {
		// TODO set gamemode to effect map and/or gamerules
		this.gamemode=gamemode;
		this.difficulty=difficulty;
		setUpSnakeMap();
		System.out.println(difficulty + map.getNameOfGamemode());
		scoreBoard=new Scoreboard(10, difficulty + map.getNameOfGamemode());
	}

}

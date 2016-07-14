package com.snake.game.states;

import java.util.ArrayList;
import java.util.Hashtable;

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
import com.sun.corba.se.impl.naming.pcosnaming.NameServer;

public class Highscore extends State{
	private static final int SIZE_OF_SCOREBOARD=10;
	private static final float WIDTH_OF_SCOREBOARD=Scoreboard.getNameLabelWidth() + Scoreboard.getScoreLabelWidth() + Scoreboard.getSpaceWidth();
	private static final float HEIGHT_OF_SCOREBOARD=25*SIZE_OF_SCOREBOARD;
	private static final float WIDTH_OF_BUTTONTABLE=300;
	private static final float HEIGHT_OF_BUTTONTABLE=150;

	private Scoreboard scoreBoard;

	private TextButton buttonBack;
	private String scoreBoardName;
	private TextButton buttonClassic;
	private TextButton buttonFlappy;
	private TextButton buttonBig;
	private TextButton buttonOffline;
	private TextButton buttonOnline;
	private Table buttonTable;
	private TextButton buttonEasy;
	private TextButton buttonNormal;
	private TextButton buttonHard;
	private TextButton buttonVeryHard;
	
	private TextButton disabledDifficultyButton;
	private TextButton disabledGamemodeButton;
	private boolean offline = true;
	private String difficulty = Difficulty.Normal.name();
	private String gamemode = "Classic";
	private Table scoreTable;
	private Hashtable<String, ArrayList<String>> hashTopScoresOnline;
	private MyClient myClient;
	
	public Highscore(GameStateController gsm) {
		super(gsm);
	}

	@Override
	public int getID() {
		return 0;
	}

	@Override
	public void onResize(int screenWidth, int screenHeight) {
		
	}

	@Override
	public void render() {
		stage.draw();
	}

	@Override
	public void update(float delta) {
		stage.act();

	}

	@Override
	public void init() {
				
		camera = new OrthographicCamera(VIEW_WIDTH,VIEW_HEIGHT);
		
		viewPort=new FitViewport(VIEW_WIDTH,VIEW_HEIGHT);
		viewPort.setCamera(camera);
		scoreBoardName="Normalclassic";
		viewPort.apply();
		camera.position.set(viewPort.getWorldWidth(), viewPort.getWorldHeight(),0);
		stage=new Stage();
		Gdx.input.setInputProcessor(stage);
		skin = new Skin(Gdx.files.internal("data/uiskin.json"));
		stage.setViewport(viewPort);
		stage.getCamera().update();
		stage.getViewport().apply();
		stage.getViewport().update(Gdx.graphics.getWidth(),Gdx.graphics.getHeight(),true); 
		myClient =new MyClient(this);
		
		
		buttonBack = new TextButton("BACK", skin);
		buttonBack.addListener(new ClickListener(){
			@Override
			public void clicked(InputEvent event, float x, float y)  {
				gsm.openMainMenu();
			}
		});
		buttonClassic = new TextButton("Classic", skin);
		buttonClassic.addListener(new ClickListener(){
			@Override
			public void clicked(InputEvent event, float x, float y)  {
				gamemode= "Classic";
				System.out.println("normal");
				changeGamemodeButton(buttonClassic);
			}
		});
		buttonFlappy = new TextButton("Flappy", skin);
		buttonFlappy.addListener(new ClickListener(){
			@Override
			public void clicked(InputEvent event, float x, float y)  {
				gamemode= "Flappy";
				changeGamemodeButton(buttonFlappy);
			}
		});
		buttonBig = new TextButton("Big", skin);
		buttonBig.addListener(new ClickListener(){
			@Override
			public void clicked(InputEvent event, float x, float y)  {
				gamemode= "Big";
				changeGamemodeButton(buttonBig);
			}
		});
		buttonEasy = new TextButton("Easy", skin);
		buttonEasy.addListener(new ClickListener(){
			@Override
			public void clicked(InputEvent event, float x, float y)  {
				difficulty = Difficulty.Easy.name();
				changeDifficultyButton(buttonEasy);
			}
		});
		buttonNormal = new TextButton("Normal", skin);
		buttonNormal.addListener(new ClickListener(){
			@Override
			public void clicked(InputEvent event, float x, float y)  {
				difficulty = Difficulty.Normal.name();
				changeDifficultyButton(buttonNormal);
			}
		});
		buttonHard = new TextButton("Hard", skin);
		buttonHard.addListener(new ClickListener(){
			@Override
			public void clicked(InputEvent event, float x, float y)  {
				difficulty = Difficulty.Hard.name();
				changeDifficultyButton(buttonHard);
			}
		});
		buttonVeryHard = new TextButton("Very Hard", skin);
		buttonVeryHard.addListener(new ClickListener(){
			@Override
			public void clicked(InputEvent event, float x, float y)  {
				difficulty = Difficulty.VeryHard.name();
				changeDifficultyButton(buttonVeryHard);
			}
		});
		buttonOffline = new TextButton("Offline", skin);
		buttonOffline.addListener(new ClickListener(){
			@Override
			public void clicked(InputEvent event, float x, float y)  {
				//TODO MAKE HERE CHANGE TO READ FROM FILE 
				offline =true;
				buttonOnline.setDisabled(false);
				buttonOffline.setDisabled(true);
				updateScoreBoard();
			}
		});
		buttonOnline = new TextButton("Online", skin);
		buttonOnline.addListener(new ClickListener(){
			@Override
			public void clicked(InputEvent event, float x, float y)  {
				offline = false;
				buttonOnline.setDisabled(true);
				buttonOffline.setDisabled(false);
				updateScoreBoard();
				//TODO MAKE ONLINE LEADERBOARDS
			}
		});
		disabledGamemodeButton=buttonClassic;
		disabledDifficultyButton=buttonNormal;
		buttonClassic.setTouchable(Touchable.disabled);
		buttonNormal.setTouchable(Touchable.disabled);
		hashTopScoresOnline= new Hashtable<String,ArrayList<String>>();
		buttonTable=new Table();
		for(int i=0; i<12; i++){
			buttonTable.add().setActorWidth(WIDTH_OF_BUTTONTABLE/12);
		}
		buttonTable.row();
		buttonTable.add(buttonOffline).size(WIDTH_OF_BUTTONTABLE/2, HEIGHT_OF_BUTTONTABLE/3).colspan(6);
		buttonTable.add(buttonOnline).size(WIDTH_OF_BUTTONTABLE/2, HEIGHT_OF_BUTTONTABLE/3).colspan(6).row();
		buttonTable.add(buttonClassic).size(WIDTH_OF_BUTTONTABLE/3, HEIGHT_OF_BUTTONTABLE/3).colspan(4);
		buttonTable.add(buttonFlappy).size(WIDTH_OF_BUTTONTABLE/3, HEIGHT_OF_BUTTONTABLE/3).colspan(4);
		buttonTable.add(buttonBig).size(WIDTH_OF_BUTTONTABLE/3, HEIGHT_OF_BUTTONTABLE/3).colspan(4).row();
		buttonTable.add(buttonEasy).size(WIDTH_OF_BUTTONTABLE/4, HEIGHT_OF_BUTTONTABLE/3).colspan(3);
		buttonTable.add(buttonNormal).size(WIDTH_OF_BUTTONTABLE/4, HEIGHT_OF_BUTTONTABLE/3).colspan(3);
		buttonTable.add(buttonHard).size(WIDTH_OF_BUTTONTABLE/4, HEIGHT_OF_BUTTONTABLE/3).colspan(3);
		buttonTable.add(buttonVeryHard).size(WIDTH_OF_BUTTONTABLE/4, HEIGHT_OF_BUTTONTABLE/3).colspan(3);
		buttonTable.setBounds(State.VIEW_WIDTH/2 - WIDTH_OF_BUTTONTABLE/2, State.VIEW_HEIGHT/2+HEIGHT_OF_SCOREBOARD/2, WIDTH_OF_BUTTONTABLE, HEIGHT_OF_BUTTONTABLE);;
		stage.addActor(buttonTable);
		scoreBoard = ScoreFileHandler.loadScoreTable("NormalClassic");
		if(scoreBoard==null){
			System.out.println("null");
			scoreBoard = new Scoreboard(10,"NormalClassic");
		}
		setupScoreBoard();
		buttonBack.setBounds(0, 0, 175, 100);
		stage.addActor(buttonBack);
		stage.setDebugAll(true);
	}

	private void setupScoreBoard() {
		scoreTable = scoreBoard.getScoreTable(skin);
		scoreTable.setBounds(State.VIEW_WIDTH/2-WIDTH_OF_SCOREBOARD/2, State.VIEW_HEIGHT/2-HEIGHT_OF_SCOREBOARD/2,WIDTH_OF_SCOREBOARD, HEIGHT_OF_SCOREBOARD);
		stage.addActor(scoreTable);
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
	
	private void changeGamemodeButton(TextButton button){
		button.setTouchable(Touchable.disabled);
		disabledGamemodeButton.setTouchable(Touchable.enabled);
		disabledGamemodeButton=button;
		updateScoreBoard();
	}
	private void changeDifficultyButton(TextButton button){
		button.setTouchable(Touchable.disabled);
		disabledDifficultyButton.setTouchable(Touchable.enabled);
		disabledDifficultyButton=button;
		updateScoreBoard();
	}
	private void updateScoreBoard(){
		scoreBoardName= difficulty + gamemode;
		System.out.println("update");
		if(offline){
			scoreBoard=null;
			stage.getActors().removeIndex(stage.getActors().indexOf(scoreTable, true));
			scoreBoard = ScoreFileHandler.loadScoreTable(scoreBoardName); 
			System.out.println(scoreBoardName + scoreBoard);
			if(scoreBoard==null){
				scoreBoard = new Scoreboard(10,scoreBoardName);
			}
			setupScoreBoard();
		} else {
			
			if(hashTopScoresOnline.containsKey(scoreBoardName)){
				stage.getActors().removeIndex(stage.getActors().indexOf(scoreTable, true));
				ArrayList<String> scores;
				scores = hashTopScoresOnline.get(scoreBoardName);
				splitStringsIntoPieces(scores);
				setupScoreBoard();
			} else {
				System.out.println("GET HIGHSCORE " + scoreBoardName) ;
				myClient.sendGetTopScore(scoreBoardName);
				
			}
			
			
			
		}
	}

	public void setTopScore(String tableName, ArrayList<String> data) {
		System.out.println(hashTopScoresOnline + " " + tableName + " " + data);
		hashTopScoresOnline.put(tableName, data);
		updateScoreBoard();
	}
	
	public void splitStringsIntoPieces(ArrayList<String> data){
		ArrayList<String> names = new ArrayList<String>();
		ArrayList<Integer> scores = new ArrayList<Integer>();
		int startRank=0;
		for (int i = 0; i < data.size(); i++) {
			String s = data.get(i);
			String[] strings = s.split(";");
			if(strings.length>=3){
			if(i == 0){
				startRank = Integer.valueOf(strings[0]);
			}
			names.add(strings[1]);
			scores.add(Integer.valueOf(strings[2]));
			} else
			if(strings.length<3){
				names.add(strings[0]);
				scores.add(Integer.valueOf(strings[1]));
			}
		}
 		this.scoreBoard=new Scoreboard(names,scores,startRank);
	}
}

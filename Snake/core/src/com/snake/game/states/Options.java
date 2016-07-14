package com.snake.game.states;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Preferences;
import com.badlogic.gdx.Application.ApplicationType;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Slider;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.snake.game.GameStateController;

public class Options extends State {
	public static final int ID = 2;
	private static final int[][]  resolution = {{ 800, 600, 1024, 768, 1280,
			960, 1600, 1200 }, { 960, 540, 1280, 720, 1376,
			768, 1600, 900, 1920, 1080 }};
	private boolean fullscreen = false;
	private int resMarker=0;
	//Ratio 43= 4:3 etc
	private static final int[] selectedRatio = {43 , 169 };
	private int ratioMarker=1;
	private Stage stage;
	private Skin skin;
	private OrthographicCamera camera;
	private TextButton resButton;
	private TextButton ratioButton;
	private TextButton fullButton;
	private TextButton exitButton;
	private Slider soundSlider;
	private Slider musicSlider;
	private Label soundLabel;
	private Label musicLabel;
	private BitmapFont font; 

	public Options(GameStateController gsm){
		super(gsm);
	}

	@Override
	public void init() {
		Preferences prefs = Gdx.app.getPreferences("snake360");
		fullscreen=prefs.getBoolean("fullscreen", false);
		if(prefs.getInteger("ratio", 169)==169){
			ratioMarker=1;
		} else if(prefs.getInteger("ratio", 169)==43){
			ratioMarker=0;
		}
		for(int i=0;i<resolution[ratioMarker].length;i+=2){
			if(resolution[ratioMarker][i]==prefs.getInteger("resWidth", 960)){
				resMarker=i;
			}
		}
		
		font=new BitmapFont();
		
		camera = new OrthographicCamera(Gdx.graphics.getWidth(),Gdx.graphics.getHeight());
		
		viewPort=new FitViewport(960, 540);// TODO ExtendViewport(960, 540,camera); MAYBE
		viewPort.setCamera(camera);
		camera.position.set(viewPort.getWorldWidth(), camera.viewportHeight,0);
		viewPort.apply(true);
		
		
		skin = new Skin(Gdx.files.internal("data/uiskin.json"));
		float x=viewPort.getWorldWidth()/2 - 100;
		float y=viewPort.getWorldHeight() *1f;
		if (Gdx.app.getType() != ApplicationType.Android) {  
		resButton=new TextButton("Resolution", skin);	
		
		
		//Resolution Button setup
		resButton.setBounds(x, y -125, 200, 50);
		Integer meinInteger = new Integer(resolution[ratioMarker][resMarker]);
        String s = meinInteger.toString();
        meinInteger = new Integer(resolution[ratioMarker][resMarker+1]);
        s = s +" "+ meinInteger.toString(); 
		resButton.setText("Resolution: " + s);
		//ratio Button setup
		ratioButton=new TextButton("Ratio", skin);
		ratioButton.setBounds(x, y -175, 200, 50);
		meinInteger = new Integer(selectedRatio[ratioMarker]);
        s = meinInteger.toString();
        ratioButton.setText("Resolution: " + s);
		//fullscreen Button setup
		fullButton=new TextButton("Fullscreen", skin);
		fullButton.setBounds(x, y -325, 200, 50);
		if(fullscreen){
			fullButton.setText("Windowed");
		} else {
			fullButton.setText("Fullscreen");
		}
		}
		//exit Button setup
		exitButton=new TextButton("Save & Back", skin);
		exitButton.setBounds(x, y -425, 200, 50);
		
		//SoundSlider setup
		soundSlider =new Slider(0, 1, 0.01f, false, skin);
		soundSlider.setBounds(x, y -225, 200, 50);
		soundSlider.setValue(prefs.getFloat("soundVolume", 0.5f));

		soundLabel=new Label("Sound Volume", skin);
		soundLabel.setBounds(x-110, y-225, 100, 50);
		//musicSlider setup
		musicSlider =new Slider(0, 1, 0.01f, false, skin);
		musicSlider.setBounds(x, y -275, 200, 50);
		musicSlider.setValue(prefs.getFloat("musicVolume", 0.5f));
		musicLabel=new Label("Music Volume", skin);
		musicLabel.setBounds(x-110, y-275, 100, 50);
		
		stage=new Stage();
		Gdx.input.setInputProcessor(stage);
		stage.addActor(soundSlider);
		stage.addActor(soundLabel);
		stage.addActor(musicSlider);
		stage.addActor(musicLabel);
		if (Gdx.app.getType() != ApplicationType.Android) {  
			stage.addActor(resButton);
			stage.addActor(ratioButton);
			stage.addActor(fullButton);
			resButton.addListener(new ClickListener(){
				@Override
				public boolean touchDown (InputEvent event, float x, float y, int pointer, int button)  {
				
					if(resMarker<resolution[ratioMarker].length-2){
						resMarker+=2;
					} else {
						resMarker=0;
					}
					System.out.println("res");
					Integer meinInteger = new Integer(resolution[ratioMarker][resMarker]);
			        String s = meinInteger.toString();
			        meinInteger = new Integer(resolution[ratioMarker][resMarker+1]);
			        s = s +" "+ meinInteger.toString(); 
					resButton.setText("Resolution: " + s);

					return false;
				}
			});		

			ratioButton.addListener(new ClickListener(){
				@Override
				public boolean touchDown (InputEvent event, float x, float y, int pointer, int button)  {
				
					if(ratioMarker<selectedRatio.length-1){
						ratioMarker++;
					} else {
						ratioMarker=0;
					}
					Integer meinInteger = new Integer(selectedRatio[ratioMarker]);
			        String s = meinInteger.toString();
			        ratioButton.setText("Ratio: " + s);
		
					return false;
					}
			});	
			
			fullButton.addListener(new ClickListener(){
				@Override
				public void clicked(InputEvent event, float x, float y)  {
					
					fullscreen=!fullscreen;	
					if(fullscreen){
						fullButton.setText("Set Windowed");
					} else {
						fullButton.setText("Set Fullscreen");
		
					}
				}
			
			});	
			
			
		}
		stage.addActor(exitButton);
		stage.setViewport(viewPort);
		stage.getCamera().update();
		stage.getViewport().apply();
		stage.getViewport().update(Gdx.graphics.getWidth(),Gdx.graphics.getHeight(),true); 

		
		exitButton.addListener(new ClickListener(){
			@Override
			public void clicked(InputEvent event, float x, float y)  {

				
				Preferences prefs = Gdx.app.getPreferences("snake360");
				if (Gdx.app.getType() != ApplicationType.Android) {  
				prefs.putInteger("resHeight", resolution[ratioMarker][resMarker+1]);
				prefs.putInteger("resWidth", resolution[ratioMarker][resMarker]);
				prefs.putInteger("ratio", ratioMarker);
				prefs.putBoolean("fullscreen", fullscreen);			
				Gdx.graphics.setDisplayMode(resolution[ratioMarker][resMarker], resolution[ratioMarker][resMarker+1], fullscreen); 
				camera.setToOrtho(false, resolution[ratioMarker][resMarker], resolution[ratioMarker][resMarker+1]);
				gsm.setHeight(resolution[ratioMarker][resMarker+1]);
				gsm.setWidth( resolution[ratioMarker][resMarker]);

				}
				prefs.putFloat("musicVolume", musicSlider.getValue());
				prefs.putFloat("soundVolume", soundSlider.getValue());
				prefs.flush();	
				gsm.setMusicVolume(musicSlider.getValue());
				gsm.setSoundVolume(soundSlider.getValue());
				gsm.optionsBack();
			}
		});	
	}
	
	//TODO Change placeholder
	@Override
	public void render() {
		camera.update();
		stage.getBatch().begin();
		font.draw(stage.getBatch(),Integer.toString(Gdx.graphics.getFramesPerSecond()), VIEW_WIDTH-50,VIEW_HEIGHT-50);
		stage.getBatch().end();
		stage.getCamera().update();
		stage.draw();
	}

	@Override
	public void update(float delta) {         
		stage.act(delta);

	
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
			stage.getViewport().update(screenWidth, screenHeight,true); 
	}

	@Override
	public void onEnter() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void dispose() {
		// TODO Auto-generated method stub
		
	}
}

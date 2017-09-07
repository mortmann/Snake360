package com.snake.game.states;

import com.badlogic.gdx.Application.ApplicationType;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Preferences;
import com.badlogic.gdx.graphics.GL20;
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
import com.snake.game.map.Map;
import org.objectweb.asm.Opcodes;

public class Options extends State {
    public static final int ID = 2;
    private static final int[][] resolution = new int[][]{new int[]{800, 600, 1024, GL20.GL_SRC_COLOR, GL20.GL_INVALID_ENUM, 960, 1600, 1200}, new int[]{960, 540, GL20.GL_INVALID_ENUM, 720, 1376, GL20.GL_SRC_COLOR, 1600, 900, 1920, 1080}};
    private static final int[] selectedRatio = new int[]{43, Opcodes.RET};
    private OrthographicCamera camera;
    private TextButton exitButton;
    private BitmapFont font;
    private TextButton fullButton;
    private boolean fullscreen = false;
    private Label musicLabel;
    private Slider musicSlider;
    private TextButton ratioButton;
    private int ratioMarker = 1;
    private TextButton resButton;
    private int resMarker = 0;
    private Skin skin;
    private Label soundLabel;
    private Slider soundSlider;
    private Stage stage;

    public Options(GameStateController gsm) {
        super(gsm);
    }

    public void init() {
        Preferences prefs = Gdx.app.getPreferences("snake360");
        this.fullscreen = prefs.getBoolean("fullscreen", false);
        if (prefs.getInteger("ratio", Opcodes.RET) == Opcodes.RET) {
            this.ratioMarker = 1;
        } else if (prefs.getInteger("ratio", Opcodes.RET) == 43) {
            this.ratioMarker = 0;
        }
        for (int i = 0; i < resolution[this.ratioMarker].length; i += 2) {
            if (resolution[this.ratioMarker][i] == prefs.getInteger("resWidth", 960)) {
                this.resMarker = i;
            }
        }
        this.font = new BitmapFont();
        this.camera = new OrthographicCamera((float) Gdx.graphics.getWidth(), (float) Gdx.graphics.getHeight());
        this.viewPort = new FitViewport(State.VIEW_WIDTH, State.VIEW_HEIGHT);
        this.viewPort.setCamera(this.camera);
        this.camera.position.set(this.viewPort.getWorldWidth(), this.camera.viewportHeight, 0.0f);
        this.viewPort.apply(true);
        this.skin = new Skin(Gdx.files.internal(Mainmenu.uiskin));
        float x = (this.viewPort.getWorldWidth() / 2.0f) - 100.0f;
        float y = this.viewPort.getWorldHeight() * 1.0f;
        if (Gdx.app.getType() != ApplicationType.Android) {
            this.resButton = new TextButton("Resolution", this.skin);
            this.resButton.setBounds(x, y - 125.0f, 200.0f, 50.0f);
            this.resButton.setText("Resolution: " + new StringBuilder(String.valueOf(new Integer(resolution[this.ratioMarker][this.resMarker]).toString())).append(" ").append(new Integer(resolution[this.ratioMarker][this.resMarker + 1]).toString()).toString());
            this.ratioButton = new TextButton("Ratio", this.skin);
            this.ratioButton.setBounds(x, y - 175.0f, 200.0f, 50.0f);
            this.ratioButton.setText("Resolution: " + new Integer(selectedRatio[this.ratioMarker]).toString());
            this.fullButton = new TextButton("Fullscreen", this.skin);
            this.fullButton.setBounds(x, y - 325.0f, 200.0f, 50.0f);
            if (this.fullscreen) {
                this.fullButton.setText("Windowed");
            } else {
                this.fullButton.setText("Fullscreen");
            }
        }
        this.exitButton = new TextButton("Save & Back", this.skin);
        this.exitButton.setBounds(x - 100.0f, y - 425.0f, 400.0f, 100.0f);
        this.soundSlider = new Slider(0.0f, 1.0f, (float) Map.PPM, false, this.skin);
        this.soundSlider.setBounds(x, y - 225.0f, 200.0f, 50.0f);
        this.soundSlider.setValue(prefs.getFloat("soundVolume", 0.5f));
        this.soundLabel = new Label((CharSequence) "Sound Volume", this.skin);
        this.soundLabel.setBounds(x - 110.0f, y - 225.0f, 100.0f, 50.0f);
        this.musicSlider = new Slider(0.0f, 1.0f, (float) Map.PPM, false, this.skin);
        this.musicSlider.setBounds(x, y - 275.0f, 200.0f, 50.0f);
        this.musicSlider.setValue(prefs.getFloat("musicVolume", 0.5f));
        this.musicLabel = new Label((CharSequence) "Music Volume", this.skin);
        this.musicLabel.setBounds(x - 110.0f, y - 275.0f, 100.0f, 50.0f);
        this.stage = new Stage();
        Gdx.input.setInputProcessor(this.stage);
        this.stage.addActor(this.soundSlider);
        this.stage.addActor(this.soundLabel);
        this.stage.addActor(this.musicSlider);
        this.stage.addActor(this.musicLabel);
        this.stage.addActor(this.exitButton);
        this.stage.setViewport(this.viewPort);
        this.stage.getCamera().update();
        this.stage.getViewport().apply();
        this.stage.getViewport().update(Gdx.graphics.getWidth(), Gdx.graphics.getHeight(), true);
        this.exitButton.addListener(new ClickListener() {
            public void clicked(InputEvent event, float x, float y) {
                Preferences prefs = Gdx.app.getPreferences("snake360");
                if (Gdx.app.getType() != ApplicationType.Android) {
                    prefs.putInteger("resHeight", Options.resolution[Options.this.ratioMarker][Options.this.resMarker + 1]);
                    prefs.putInteger("resWidth", Options.resolution[Options.this.ratioMarker][Options.this.resMarker]);
                    prefs.putInteger("ratio", Options.this.ratioMarker);
                    prefs.putBoolean("fullscreen", Options.this.fullscreen);
                    Gdx.graphics.setDisplayMode(Options.resolution[Options.this.ratioMarker][Options.this.resMarker], Options.resolution[Options.this.ratioMarker][Options.this.resMarker + 1], Options.this.fullscreen);
                    Options.this.camera.setToOrtho(false, (float) Options.resolution[Options.this.ratioMarker][Options.this.resMarker], (float) Options.resolution[Options.this.ratioMarker][Options.this.resMarker + 1]);
                    Options.this.gsm.setHeight(Options.resolution[Options.this.ratioMarker][Options.this.resMarker + 1]);
                    Options.this.gsm.setWidth(Options.resolution[Options.this.ratioMarker][Options.this.resMarker]);
                }
                prefs.putFloat("musicVolume", Options.this.musicSlider.getValue());
                prefs.putFloat("soundVolume", Options.this.soundSlider.getValue());
                prefs.flush();
                Options.this.gsm.setMusicVolume(Options.this.musicSlider.getValue());
                Options.this.gsm.setSoundVolume(Options.this.soundSlider.getValue());
                Options.this.gsm.optionsBack();
            }
        });
    }

    public void render() {
        this.camera.update();
        this.stage.getBatch().begin();
        this.font.draw(this.stage.getBatch(), Integer.toString(Gdx.graphics.getFramesPerSecond()), 910.0f, 490.0f);
        this.stage.getBatch().end();
        this.stage.getCamera().update();
        this.stage.draw();
    }

    public void update(float delta) {
        this.stage.act(delta);
    }

    public int getID() {
        return 2;
    }

    public void onExit() {
        this.stage.dispose();
        Gdx.input.setInputProcessor(null);
    }

    public void onResize(int screenWidth, int screenHeight) {
        this.viewPort.update(screenWidth, screenHeight);
        this.stage.getViewport().update(screenWidth, screenHeight, true);
    }

    public void onEnter() {
    }

    public void dispose() {
    }
}

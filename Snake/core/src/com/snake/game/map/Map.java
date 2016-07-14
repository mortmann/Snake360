package com.snake.game.map;

import java.util.ArrayList;

import org.dyn4j.dynamics.World;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.snake.game.entity.Bubble;
import com.snake.game.entity.PickUP;
import com.snake.game.entity.Snake;
import com.snake.game.util.MyRectangle;

public abstract class Map {
	protected ArrayList<Wall> walls;
	protected ArrayList<Bubble> bubbles;

	protected MyRectangle door;
	protected Image background;
	protected boolean doorOpen;
	protected int level;
	protected Snake snake;
	protected PickUP pickUp;
	protected Vector2 spawnPoint;
	protected int amountPickUPsToWin;
	protected World world;
	
	public abstract void render(SpriteBatch batch);
	
	public abstract void update(float delta);
	
	public ArrayList<Wall> getWalls() {
		return walls;
	}

	public abstract void openDoor();

	public boolean isDoorOpen() {
		return doorOpen;
	}

	public void setDoorOpen(boolean doorOpen) {
		this.doorOpen = doorOpen;
	}

	public MyRectangle getDoor() {
		return door;
	}

	public abstract void closeDoor();

	public Vector2 getSpawnPoint() {
		return spawnPoint;
	}

	public void setSpawnPoint(Vector2 spawnPoint) {
		this.spawnPoint = spawnPoint;
	}

	public int getAmountPickUPsToWin() {
		return amountPickUPsToWin;
	}

	public void setAmountPickUPsToWin(int amountPickUPsToWin) {
		this.amountPickUPsToWin = amountPickUPsToWin;
	}
	
	public abstract String getNameOfGamemode();
	
	
	
	
	
	
	
	
}

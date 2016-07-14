package com.snake.game.map;

import java.util.ArrayList;

import org.dyn4j.collision.Bounds;
import org.dyn4j.collision.Collidable;
import org.dyn4j.dynamics.World;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.snake.game.entity.*;
import com.snake.game.states.State;
import com.snake.game.util.MyRectangle;

public class ClassicMap extends Map{
	private static final float WALL_SIZE=60;
	private static final float LEVEL_SHRINK_SIZE=75;
	private static final float DOOR_SIZE=160;
	private static final int AMOUNT_TO_ADVANCE_TO_NEXT_LEVEL=9;
	private static final int AMOUNT_MORE_PICKUPS_PER_LEVEL=3;
	private static final String NAME_OF_GAMEMODE="Classic";

	public ClassicMap(int level,Snake snake) {
		world = new World();
		world.setGravity(World.ZERO_GRAVITY);
		this.level=level;
		if(this.level==3){
			this.level=3;
		} 
		walls=new ArrayList<Wall>();
		bubbles = new ArrayList<Bubble>();
		//DOWN
		walls.add(new Wall(0,0,State.VIEW_WIDTH ,WALL_SIZE));
		world.addBody(walls.get(0).getBody());
		//left
		walls.add(new Wall(0,0,WALL_SIZE+this.level*LEVEL_SHRINK_SIZE,State.VIEW_HEIGHT));
		world.addBody(walls.get(1).getBody());
		//right
		walls.add(new Wall(State.VIEW_WIDTH-WALL_SIZE-this.level*LEVEL_SHRINK_SIZE,0,WALL_SIZE+this.level*LEVEL_SHRINK_SIZE,State.VIEW_HEIGHT));
		world.addBody(walls.get(2).getBody());
		//up
		walls.add(new Wall(0,State.VIEW_HEIGHT-WALL_SIZE,State.VIEW_WIDTH,WALL_SIZE));
		world.addBody(walls.get(3).getBody());
		this.snake = snake;
		bubbles.add(new Bubble(new Vector2(State.VIEW_WIDTH/2,State.VIEW_HEIGHT/2 ),snake));
		world.addBody(bubbles.get(0).getBody());
		
		
		background=new Image(new TextureRegion(new Texture(Gdx.files.internal("map/background_grass.png"))));
		background.setSize(State.VIEW_WIDTH,State.VIEW_HEIGHT);
		pickUp= new PickUP(new Vector2(State.VIEW_WIDTH/2 + 100,State.VIEW_HEIGHT/2 + 100));
		pickUp.respawn(walls);
		spawnPoint=new Vector2(State.VIEW_WIDTH/2,State.VIEW_HEIGHT/2);
		amountPickUPsToWin=AMOUNT_TO_ADVANCE_TO_NEXT_LEVEL;
		

		
	}
	@Override
	public void update(float delta) {
		if(!doorOpen){
			if(snake.getPickups()==amountPickUPsToWin + AMOUNT_MORE_PICKUPS_PER_LEVEL * level){
				openDoor();
			} 
		} else {
			snake.checkForDoorCollision(door,true);
		}
		snake.checkForWallCollision(walls);
	
		snake.checkForPickUpCollision(pickUp,true);
		
		for (Bubble bubble : bubbles) {
			bubble.update(delta);
		}
		
		if(pickUp.isPickedUP()){
			
			pickUp.respawn(walls);
		}
	}
	
	public void render(SpriteBatch batch){
		background.draw(batch, 1);
		for(Wall w : walls){
			w.render(batch);
		}
		for (Bubble bubble : bubbles) {
			bubble.render(batch);
		}
		pickUp.render(batch);
	}

	public ArrayList<Wall> getWalls() {
		return walls;
	}

	public void openDoor() {
		walls.set(3,new Wall(0,State.VIEW_HEIGHT-WALL_SIZE,State.VIEW_WIDTH/2-DOOR_SIZE/2,WALL_SIZE));
		walls.add(new Wall(State.VIEW_WIDTH/2+DOOR_SIZE/2,State.VIEW_HEIGHT-WALL_SIZE,State.VIEW_WIDTH/2-DOOR_SIZE/2,WALL_SIZE));
		door=new MyRectangle(State.VIEW_WIDTH/2-DOOR_SIZE/2,State.VIEW_HEIGHT-20,160,10);
		doorOpen=true;
	}

	public boolean isDoorOpen() {
		return doorOpen;
	}

	public void setDoorOpen(boolean doorOpen) {
		this.doorOpen = doorOpen;
	}

	public MyRectangle getDoor() {
		return door;
	}

	public void closeDoor() {
		walls=new ArrayList<Wall>();
		//DOWN
		walls.add(new Wall(0,0,State.VIEW_WIDTH ,WALL_SIZE));
		//left
		walls.add(new Wall(0,0,WALL_SIZE+this.level*LEVEL_SHRINK_SIZE,State.VIEW_HEIGHT));
		//right
		walls.add(new Wall(State.VIEW_WIDTH-WALL_SIZE-this.level*LEVEL_SHRINK_SIZE,0,WALL_SIZE+this.level*LEVEL_SHRINK_SIZE,State.VIEW_HEIGHT));
		//up
		walls.add(new Wall(0,State.VIEW_HEIGHT-WALL_SIZE,State.VIEW_WIDTH,WALL_SIZE));
		doorOpen=false;
	}
	@Override
	public String getNameOfGamemode() {
		return NAME_OF_GAMEMODE;
	}

}

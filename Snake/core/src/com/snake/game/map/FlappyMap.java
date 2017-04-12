package com.snake.game.map;

import java.util.ArrayList;
import java.util.Random;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.snake.game.entity.PickUP;
import com.snake.game.entity.Snake;
import com.snake.game.states.State;
import com.snake.game.util.MyRectangle;

public class FlappyMap extends Map {
	private static final float WALL_SIZE=60;
	private static final float DOOR_DISTANZ=500;
	private static final int DOOR_MIN_SIZE=75;
	private static final int DOOR_MAX_SIZE=150;
	private static final int DOOR_MIN_Y=(int) (WALL_SIZE + DOOR_MIN_SIZE);
	private static final int DOOR_MAX_Y = (int) (State.VIEW_HEIGHT - WALL_SIZE*2 );
	private static final float MAP_SPEED = 100;
	private static final float SECURITY_DISTANZ_TO_WALL = 50;
	private static final String NAME_OF_GAMEMODE="Flappy";
	private static final int STATIC_WALLS = 3;
	
	private float distanceLastDoor=DOOR_DISTANZ;
	private ArrayList<Wall> doors;
	private ArrayList<Wall> doorParts;

	private MyRectangle nextSpawnArea;
	
	public FlappyMap(Snake snake){
		this.snake = snake;
		walls=new ArrayList<Wall>();
		doors=new ArrayList<Wall>();
		doorParts=new ArrayList<Wall>();
		//DOWN
		walls.add(new Wall(0,0,State.VIEW_WIDTH*2,WALL_SIZE));
		//up
		walls.add(new Wall(0,State.VIEW_HEIGHT-WALL_SIZE,State.VIEW_WIDTH*2,WALL_SIZE));
		//BEHIND (left out of Screen)
		walls.add(new Wall(0-WALL_SIZE-WALL_SIZE/2,0,WALL_SIZE ,State.VIEW_HEIGHT));
		//DOOR 
		//-Riht ot of Screen
		walls.add(new Wall(State.VIEW_WIDTH+WALL_SIZE+WALL_SIZE/2,0,WALL_SIZE ,State.VIEW_HEIGHT));

		doors.add(new Wall(State.VIEW_WIDTH/2,0,WALL_SIZE,State.VIEW_HEIGHT));
		doors.add(new Wall(State.VIEW_WIDTH,0,WALL_SIZE,State.VIEW_HEIGHT));
		doors.add(new Wall(State.VIEW_WIDTH + DOOR_DISTANZ,0,WALL_SIZE,State.VIEW_HEIGHT));
		

		
		background=new Image(new TextureRegion(new Texture(Gdx.files.internal("map/background_grass.png"))));
		background.setSize(State.VIEW_WIDTH,State.VIEW_HEIGHT);
		pickUp= new PickUP(new Vector2(State.VIEW_WIDTH/2 + 100,State.VIEW_HEIGHT/2 + 100));
		nextSpawnArea= new MyRectangle(0+50,0 + 2*WALL_SIZE,State.VIEW_WIDTH/2-100, State.VIEW_HEIGHT - 4*WALL_SIZE );
		pickUp.respawnIn(nextSpawnArea);
		spawnPoint=new Vector2(State.VIEW_WIDTH/2-300,State.VIEW_HEIGHT/2);
		//you cant win in flappy mode
		amountPickUPsToWin=-1;
		snake.setMaxCircleAmount(50);
	}
	
	
	@Override
	public void render(SpriteBatch batch) {
		background.draw(batch, 1);
		
		for(Wall w : walls){
			w.render(batch);
		}
		for(Wall w : doors){
			w.render(batch);
		}
		for(Wall w : doorParts){
			w.render(batch);
		}
//		if(door!=null)
//			door.drawDebug(batch,Color.RED);
//		if(nextSpawnArea!=null)
//			nextSpawnArea.drawDebug(batch,Color.BLACK);
		pickUp.render(batch);

	}

	@Override
	public void update(float delta) {
		distanceLastDoor+=MAP_SPEED*delta;
		
		if(distanceLastDoor>=DOOR_DISTANZ){
			doors.add(new Wall(State.VIEW_WIDTH+2*DOOR_DISTANZ,0,WALL_SIZE,State.VIEW_HEIGHT));
			distanceLastDoor=0;
		}
		
		snake.checkForWallCollision(walls);
		snake.checkForWallCollision(doors);
		snake.checkForWallCollision(doorParts);
		
		if(!doorOpen){
			if(snake.getPickups()==1){
				openDoor();
				
			} 
		} else {
			if(snake.checkForDoorCollision(door,false)){
				closeDoor();
			}
		}
		snake.checkForPickUpCollision(pickUp,false);

		
		Vector2 v = new Vector2();
		for(int i=doors.size()-1; i>=0; i--){
			doors.get(i).getMyRectangle().getPosition(v);
			doors.get(i).getMyRectangle().setPosition(v.sub(MAP_SPEED*delta, 0));
			if(v.x<=0-WALL_SIZE){
				doors.remove(i);
			}
		}
		for(int i=doorParts.size()-1; i>=0; i--){
			doorParts.get(i).getMyRectangle().getPosition(v);
			doorParts.get(i).getMyRectangle().setPosition(v.sub(MAP_SPEED*delta, 0));
			if(v.x<=0-WALL_SIZE){
				doorParts.remove(i);
			}
		}
		if(pickUp.getMyRectangle().x>=0){
			pickUp.getMyRectangle().getPosition(v);
			pickUp.getMyRectangle().setPosition(v.sub(MAP_SPEED*delta, 0));
		}
		if(door!=null){
			door.getPosition(v);
			door.setPosition(v.sub(MAP_SPEED*delta, 0));
		}
		
	}

	@Override
	public void openDoor() {
		this.doorOpen=true;
		Wall w = doors.remove(0);

		float x = w.getMyRectangle().x+WALL_SIZE+SECURITY_DISTANZ_TO_WALL;
		nextSpawnArea= new MyRectangle(x ,WALL_SIZE,doors.get(0).getMyRectangle().x-x,State.VIEW_HEIGHT-2*(WALL_SIZE)-SECURITY_DISTANZ_TO_WALL);
		pickUp.respawnIn(nextSpawnArea);
			
		Random r = new Random();
		float doorSize=DOOR_MIN_SIZE + r.nextInt(DOOR_MAX_SIZE - DOOR_MIN_SIZE);
		float y = DOOR_MIN_Y + r.nextInt(DOOR_MAX_Y-DOOR_MIN_Y);
		//DOOR TOP SIDE
		doorParts.add(new Wall(w.getMyRectangle().x,y+doorSize/2,WALL_SIZE,State.VIEW_HEIGHT/2 + doorSize));
		//DOOR DOWN SIDE
		doorParts.add(new Wall(w.getMyRectangle().x,0-doorSize/2,WALL_SIZE,y  ));
		
		door=new MyRectangle(w.getMyRectangle().x,y-doorSize/2,WALL_SIZE,doorSize);
	}

	@Override
	public void closeDoor() {
		this.doorOpen=false;
		door=null;
	}


	@Override
	public String getNameOfGamemode() {
		return NAME_OF_GAMEMODE;
	}

}

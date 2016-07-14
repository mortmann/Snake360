package com.snake.game.entity;

import java.util.ArrayList;
import java.util.Random;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.snake.game.map.Wall;
import com.snake.game.util.MyRectangle;

public class PickUP {
	private static final float SECURITY_DISTANCE_SPAWN_IN = 25;
	private Texture texture;
	private float size;
	private MyRectangle myRectangle;
	private boolean pickedUP;
	
	public PickUP(Vector2 position){
		texture=new Texture(Gdx.files.internal("entity/pickup_apple.png"));
		size=25f;
		myRectangle=new MyRectangle(position.x,position.y,size,size,this);
		
	}
	public PickUP(float x, float y) {
		texture=new Texture(Gdx.files.internal("entity/pickup_apple.png"));
		size=25f;
		myRectangle=new MyRectangle(x,y,size,size,this);
	}
	public void render(SpriteBatch batch){
		batch.draw(texture, myRectangle.x, myRectangle.y, size, size);
	}
	public MyRectangle getMyRectangle() {
		return myRectangle;
	}
	public void respawn(ArrayList<Wall> walls){
		boolean spawned=false;
		MyRectangle rect =new MyRectangle();
		while(!spawned){
			Vector2 temp = new Vector2();
			Random r = new Random();
			temp.x=r.nextInt(960);
			temp.y=r.nextInt(540);
			spawned=true;
			rect =new MyRectangle(temp,size);
			for(Wall w : walls){
				if(w.getMyRectangle().overlaps(rect)){
					spawned=false;
				}
			}
		}
		this.myRectangle=rect;
		pickedUP=false;
	}
	
	
	
	public void pickUP() {
		pickedUP=true;
	}
	public boolean isPickedUP() {
		return pickedUP;
	}
	public void respawnIn(MyRectangle myRect) {
		//TODO FIX IT
		MyRectangle rect =new MyRectangle();

			Vector2 temp = new Vector2();
			Random r = new Random();
			temp.x=SECURITY_DISTANCE_SPAWN_IN/2 + myRect.x+r.nextInt((int) (myRect.width-SECURITY_DISTANCE_SPAWN_IN/2));
			temp.y=SECURITY_DISTANCE_SPAWN_IN/2 + myRect.y+r.nextInt((int) (myRect.height-SECURITY_DISTANCE_SPAWN_IN/2));

			rect =new MyRectangle(temp,size);

		this.myRectangle=rect;
		pickedUP=false;
	}
}

package com.snake.game.util;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer.ShapeType;
import com.badlogic.gdx.math.Matrix4;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;

public class MyRectangle extends Rectangle {
	private static final long serialVersionUID = -3160874626585931530L;
	
	private Object myData;
	private ShapeRenderer shapeRenderer;
	
	public MyRectangle (float x, float y, float width, float height, Object myData) {
		this.setMyData(myData);
		this.x = x;
		this.y = y;
		this.width = width;
		this.height = height;
	}
	public MyRectangle (float x, float y, float width, float height) {
		this.setMyData(myData);
		this.x = x;
		this.y = y;
		this.width = width;
		this.height = height;
	}
	public MyRectangle(){
	}

	public MyRectangle(Vector2 temp, float size) {
		this.x = temp.x;
		this.y = temp.y;
		this.width = size;
		this.height = size;
	}
	public Object getMyData() {
		return myData;
	}


	public void setMyData(Object myData) {
		this.myData = myData;
	}
	public void drawDebug(SpriteBatch batch){
		batch.end();
		if(shapeRenderer==null){
			shapeRenderer=new ShapeRenderer();
			shapeRenderer.setProjectionMatrix(batch.getProjectionMatrix());

		}
		shapeRenderer.setAutoShapeType(true);
		shapeRenderer.begin();
		shapeRenderer.setColor(0.5f, 0.5f, 0.5f, 1);
		shapeRenderer.set(ShapeType.Filled);
		shapeRenderer.rect(x, y, width, height);
		shapeRenderer.end();
		batch.begin();
	}
	public void drawDebug(Matrix4 projectionMatrix){
		if(shapeRenderer==null){
			shapeRenderer=new ShapeRenderer();
			shapeRenderer.setProjectionMatrix(projectionMatrix);
		}
		shapeRenderer.rect(x, y, width, height);
	}
	public void drawDebug(SpriteBatch batch, Color color) {
		if(shapeRenderer==null){
			shapeRenderer=new ShapeRenderer();
			shapeRenderer.setProjectionMatrix(batch.getProjectionMatrix());
		}
		shapeRenderer.setAutoShapeType(true);
		shapeRenderer.begin();
		shapeRenderer.setColor(color);
		shapeRenderer.set(ShapeType.Filled);
		shapeRenderer.rect(x, y, width, height);
		shapeRenderer.end();
	}
	
	
}

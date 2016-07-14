package com.snake.game.map;

import org.dyn4j.dynamics.Body;
import org.dyn4j.dynamics.BodyFixture;
import org.dyn4j.geometry.Rectangle;
import org.dyn4j.geometry.Transform;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.snake.game.util.MyRectangle;

public class Wall {
	private MyRectangle myRectangle;
	private Rectangle rectangle;
	private Body body;
	private TextureRegion texReg;
	public Wall(float x, float y, float width,float height) {
		myRectangle=new MyRectangle(x,y,width,height);
		rectangle = new Rectangle(width, height);
		body = new Body();
		Transform t = new Transform();
		t.setTranslation(x, y);
		BodyFixture bf = new BodyFixture(rectangle);
		body.addFixture(bf);
		body.setTransform(t);
		
		Texture temp = new Texture(Gdx.files.internal("map/wall.png"));
		texReg=new TextureRegion(temp);
		texReg=texReg.split(60, 60)[0][0];
	
	}
	public void render(SpriteBatch batch) {
		myRectangle.drawDebug(batch);
	}
	
	public MyRectangle getMyRectangle() {
		return myRectangle;
	}
	public void setMyRectangle(MyRectangle myRectangle) {
		this.myRectangle = myRectangle;
	}
	public Body getBody() {
		return body;
	}
	public void setBody(Body body) {
		this.body = body;
	}
	
	
	

}

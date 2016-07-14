package com.snake.game.entity;

import java.util.ArrayList;

import org.dyn4j.dynamics.Body;
import org.dyn4j.dynamics.BodyFixture;
import org.dyn4j.geometry.Convex;
import org.dyn4j.geometry.Transform;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer.ShapeType;
import com.badlogic.gdx.math.Circle;
import com.badlogic.gdx.math.Vector2;

public class Bubble  {
	
	private Snake snake;
	private Vector2 position;
	private Vector2 direction;
	private float size;
	private float speed;
	private Circle circle;
	private ShapeRenderer shapeRenderer;
	private Body body;

	public Bubble(Vector2 pos, Snake snake){
		this.position = pos;
		this.direction = pos.cpy().sub(snake.getPosition()).nor();
		size = 25f;
		circle = new Circle();
		circle.setRadius(size);
		circle.setPosition(position);
		shapeRenderer = new ShapeRenderer();
		speed =3* (snake.getSpeed()/4);
		
		body = new Body();
		Transform t = new Transform();
		t.setTranslation(pos.x, pos.y);
		org.dyn4j.geometry.Circle c = new org.dyn4j.geometry.Circle(size);
		BodyFixture bf = new BodyFixture(c);
		body.addFixture(bf);
		body.setTransform(t);
		
	}
	
	public void render(SpriteBatch batch){
		batch.end();
		shapeRenderer.setProjectionMatrix(batch.getProjectionMatrix());
		shapeRenderer.setAutoShapeType(true);
		shapeRenderer.begin();		
		shapeRenderer.set(ShapeType.Filled);
		shapeRenderer.setColor(Color.BLUE);
		shapeRenderer.circle(circle.x, circle.y, size/2);
		shapeRenderer.end();
		batch.begin();

	}
	public void update(float delta){
		position.add(direction.x*speed*delta,direction.y*speed*delta);
		circle.setPosition(position);
	}
	public void updateDirection(){
		direction.setAngle(-direction.angle());
	}

}

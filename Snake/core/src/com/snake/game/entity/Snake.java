package com.snake.game.entity;

import java.util.ArrayList;
import java.util.LinkedList;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer.ShapeType;
import com.badlogic.gdx.math.Circle;
import com.badlogic.gdx.math.Intersector;
import com.badlogic.gdx.math.Vector2;
import com.snake.game.map.Wall;
import com.snake.game.util.Difficulty;
import com.snake.game.util.MyRectangle;

public class Snake {
	private static final float EASY_SPEED=250;
	private static final float NORMAL_SPEED=350;
	private static final float HARD_SPEED=450;
	private static final float VERYHARD_SPEED=550;
	private static final float TURN_ADD_SPEED=200;
	private static final float DISTANZ_CIRCLE_MODIFIER = 1.6666f;
	private static final float MIN_DISTANCE_CIRCLE =2.9f;// 5.8330f;
	private static final int ADD_CIRCLE_AMOUNT = 5;
	private static final float deltaCircleTime = 0.016666f;
	
	private int difficulty = 0;
	
	private Circle myCircle;
	private MyRectangle rectangle;
	private LinkedList<Circle> circles;

	private ShapeRenderer shapeRenderer;
	private int circleAmount=5;
	private int pickups=0;
	private float deltaSum;
	private float length;
	private float tick;
	private boolean dead;
	private boolean won;
	private boolean addedCircle;
	private Vector2 position;
	private Vector2 move;
	private float angle;
	private float size;
	private float speed;
	private float turnspeed;
	private int turnDir;
	private Circle lastCircle;
	private int score=0;
	private int maxCircleAmount=-1;
	
	public Snake() {
		setDead(false);
		size=10f;
		move=new Vector2();
		move.y=1;
		angle=0f;

		rectangle=new MyRectangle();
		rectangle.setSize(size);

		circles=new LinkedList<Circle>();
		
		shapeRenderer = new ShapeRenderer();
	}
	
	public Snake(int score) {
		this.score=score;
		setDead(false);

		

		size=10f;
		
		move=new Vector2();
		move.y=1;
		
		angle=0f;
		
		rectangle=new MyRectangle();
		rectangle.setSize(size);
		
		circles=new LinkedList<Circle>();
		
		shapeRenderer = new ShapeRenderer();
	}

	public void update(float delta){
			tick+=delta;
			deltaSum+=delta;
			int f = Math.round((deltaSum)/deltaCircleTime);
			if(tick>=deltaCircleTime){
				while(tick>=0.016666f){
				tick-=0.016666f;
				delta=0.016666f;
				length=0;
				for(int i = circles.size()-1; i>=1;i-=2){
					Vector2 v1 = new Vector2(circles.get(i).x,circles.get(i).y);
					Vector2 v2 = new Vector2(circles.get(i-1).x,circles.get(i-1).y);		
					length+=v1.dst(v2);
					length=Math.round(length);
				}

				angle+=turnDir*turnspeed*delta;
				angle%=360;
				angle=Math.round(angle);
				move=new Vector2();
				move.y=1;
				move.setAngle(angle);	
				move.nor();
				if(lastCircle!=null){
					Vector2 v1 = new Vector2(lastCircle.x,lastCircle.y);
					Vector2 v2 = new Vector2(circles.getFirst().x,circles.getFirst().y);
					v2.sub(v1);
					v2.nor();
					v1.add(v2.x*speed*delta,v2.y*speed*delta);
					
					lastCircle.setPosition(v1);
				}
				position.add(move.x*speed*delta,move.y*speed*delta);
				rectangle.setCenter(position);
				
				myCircle.setPosition(position);
				Vector2 v1 = new Vector2(myCircle.x,myCircle.y);
				Vector2 v2 = new Vector2(circles.getLast().x,circles.getLast().y);		
				float dst=v1.dst(v2);
//				System.out.println("dst " +dst);
				if(circles.size()>1){
					if(dst>=MIN_DISTANCE_CIRCLE* 2 + difficulty*DISTANZ_CIRCLE_MODIFIER ){
						circles.add(new Circle(myCircle));
						addedCircle=true;
					}
				} else {
					circles.add(new Circle(myCircle));
				}
				if(deltaSum>deltaCircleTime){	
							for(int i = f; i>=0;i--){
								if(length>=10*circleAmount && addedCircle){ //circles.size()>circleAmount
									lastCircle=new Circle(circles.removeFirst());
								}
							}
							deltaSum=0;
				}
				}
			}
		
	}
	
	
	public void render(SpriteBatch batch){
		batch.end();
		shapeRenderer.setProjectionMatrix(batch.getProjectionMatrix());
		shapeRenderer.setAutoShapeType(true);
		shapeRenderer.begin();		
		shapeRenderer.set(ShapeType.Filled);
		for(int i = 0; i<circles.size();i++){
			if(i==circles.size()-1){
				shapeRenderer.setColor(Color.BLACK);
				shapeRenderer.circle(circles.get(i).x, circles.get(i).y, size/2);
				//shapeRenderer.rectLine(circles.get(i).x, circles.get(i).y, circles.get(i-1).x, circles.get(i-1).y, size);
			} else {
				shapeRenderer.setColor(Color.GRAY);
				shapeRenderer.circle(circles.get(i).x, circles.get(i).y, size/2);
//				shapeRenderer.setColor(Color.DARK_GRAY);
				shapeRenderer.rectLine(circles.get(i).x, circles.get(i).y, circles.get(i+1).x, circles.get(i+1).y, size);
			}
			
		}
		if(lastCircle!=null){
			shapeRenderer.setColor(Color.GRAY);
			
			
			
			shapeRenderer.circle(lastCircle.x, lastCircle.y, size/2);
			shapeRenderer.rectLine(lastCircle.x, lastCircle.y, circles.getFirst().x, circles.getFirst().y, size);
		}
		shapeRenderer.end();
		batch.begin();
	}
	public void checkForBodyCollision(ArrayList<Bubble> bubbles){
		if(circles.size()>10 || length>20)
		for(int i = circles.size()-10; i>=1;i-=1){
			if(myCircle.overlaps(circles.get(i))){
				dead=true;
			}
		}
	}


	public void checkForWallCollision(ArrayList<Wall> walls){
		for(Wall w : walls){
			if(Intersector.overlaps(rectangle, w.getMyRectangle())){
				dead=true;
			}
		}
	}
	public void checkForPickUpCollision(PickUP pickup, boolean isScore){
		if(Intersector.overlaps(myCircle,pickup.getMyRectangle())){
			if(maxCircleAmount==-1 || circleAmount<=maxCircleAmount){
				circleAmount+=ADD_CIRCLE_AMOUNT;
			}
			pickup.pickUP();
			pickups++;
			if(isScore){
				score++;
			}
		}
	}
	public boolean checkForDoorCollision(MyRectangle door, boolean canWin){
		
		if(Intersector.overlaps(myCircle,door) ){
			if(canWin){
				won= true;
			} else {
				if(pickups==1){
					score+=1;
				}
				pickups=0;
			}
			return true;
		}
		return false;
	}
	public Vector2 getCenter(){
		return position;
	}

	public boolean isDead() {
		return dead;
	}

	public void setDead(boolean dead) {
		this.dead = dead;
	}

	public int getPickups() {
		return pickups;
	}

	public void setPickups(int pickups) {
		this.pickups = pickups;
	}

	public boolean isWon() {
		return won;
	}

	public void setWon(boolean won) {
		this.won = won;
	}
	
	public void setTurnDir(int turnDir) {
		this.turnDir = turnDir;
	}
	
	public void setDifficulty(String gamemode) {
		if(gamemode.contains(Difficulty.Easy.name())){
			speed=EASY_SPEED;
			difficulty=0;
		} else 
		if(gamemode.contains(Difficulty.Normal.name())){
			speed=NORMAL_SPEED;
			difficulty=1;
		}  else 
		if(gamemode.contains(Difficulty.VeryHard.name())){
			speed=VERYHARD_SPEED;
			difficulty=3;
		} else 
		if(gamemode.contains(Difficulty.Hard.name())){
			speed=HARD_SPEED;
			difficulty=2;
		}
		turnspeed=TURN_ADD_SPEED+speed;
	} 
	public Vector2 getPosition() {
		return position;
	}

	public void setPosition(Vector2 position) {
		this.position=position;
		rectangle.setCenter(position);
		myCircle=new Circle(position.x,position.y,size);
		circles.add(myCircle);
	}

	public int getScore() {
		return score;
	}

	public void addScore() {
		this.score++;
	}

	public int getMaxCircleAmount() {
		return maxCircleAmount;
	}

	public void setMaxCircleAmount(int maxCircleAmount) {
		this.maxCircleAmount = maxCircleAmount;
	}
	public Circle getMyCircle() {
		return myCircle;
	}

	public void setMyCircle(Circle myCircle) {
		this.myCircle = myCircle;
	}

	public LinkedList<Circle> getCircles() {
		return circles;
	}

	public void setCircles(LinkedList<Circle> circles) {
		this.circles = circles;
	}

	public float getSpeed() {
		return speed;
	}

	public void setSpeed(float speed) {
		this.speed = speed;
	}

}

package com.snake.game.entity;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer.ShapeType;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.BodyDef.BodyType;
import com.badlogic.gdx.physics.box2d.CircleShape;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.World;
import java.util.Random;

public class Bubble {
    private static final float SIZE = 0.25f;
    private Body body;
    private Vector2 direction;
    private ShapeRenderer shapeRenderer = new ShapeRenderer();
    private float speed;

    public Bubble(Vector2 pos, Snake snake, World world) {
        this.direction = pos.cpy().sub(snake.getPosition()).nor().scl(-1.0f);
        int i = new Random().nextInt(60) + 30;
        System.out.println(i);
        this.speed = ((float) i) * snake.getSpeed();
        BodyDef bDef = new BodyDef();
        bDef.type = BodyType.DynamicBody;
        bDef.position.set(pos);
        FixtureDef fDef = new FixtureDef();
        CircleShape sh = new CircleShape();
        sh.setRadius(0.125f);
        fDef.shape = sh;
        fDef.friction = 0.0f;
        fDef.restitution = 1.0f;
        this.body = world.createBody(bDef);
        this.body.setUserData(this);
        this.body.createFixture(fDef);
        this.body.applyForceToCenter(this.direction.x * this.speed, this.direction.y * this.speed, true);
    }

    public void render(SpriteBatch batch) {
        batch.end();
        this.shapeRenderer.setProjectionMatrix(batch.getProjectionMatrix());
        this.shapeRenderer.setAutoShapeType(true);
        this.shapeRenderer.begin();
        this.shapeRenderer.set(ShapeType.Filled);
        this.shapeRenderer.setColor(Color.BLUE);
        this.shapeRenderer.circle(this.body.getPosition().x, this.body.getPosition().y, 0.125f, 50);
        this.shapeRenderer.end();
        batch.begin();
    }

    public void update(float delta) {
    }

    public void OnCollision(Body body) {
        body.applyForceToCenter(this.direction.x * this.speed, this.direction.y * this.speed, true);
    }
}

package com.snake.game.entity;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.BodyDef.BodyType;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.World;
import com.snake.game.map.Map;
import com.snake.game.util.MyRectangle;
import java.util.Random;

public class PickUP {
    public static final float SECURITY_DISTANCE_SPAWN_IN = 0.25f;
    public static final float SIZE = 0.25f;
    private Body body;
    private boolean pickedUP;
    private float respawnTime = 0.1f;
    private Body sensorbody;
    private Texture texture = new Texture(Gdx.files.internal("entity/pickup_apple.png"));

    public PickUP(Vector2 position, World world) {
        BodyDef bDef = new BodyDef();
        bDef.type = BodyType.StaticBody;
        position.add(0.125f, 0.125f);
        bDef.position.set(position);
        FixtureDef fDef = new FixtureDef();
        PolygonShape sh = new PolygonShape();
        sh.setAsBox(0.125f, 0.125f, Vector2.Zero, 0.0f);
        fDef.shape = sh;
        this.body = world.createBody(bDef);
        this.body.createFixture(fDef);
        this.body.setUserData(this);
        bDef.type = BodyType.DynamicBody;
        this.sensorbody = world.createBody(bDef);
        fDef.isSensor = true;
        this.sensorbody.createFixture(fDef);
        this.sensorbody.setUserData(this);
    }

    public PickUP(float x, float y) {
    }

    public void render(SpriteBatch batch) {
        if (this.respawnTime > 0.0f) {
            this.sensorbody.setLinearVelocity(new Vector2(Map.PPM, 0.0f));
            this.respawnTime -= Gdx.graphics.getDeltaTime();
            return;
        }
        this.sensorbody.setLinearVelocity(new Vector2(0.0f, 0.0f));
        batch.draw(this.texture, this.body.getPosition().x - 0.125f, this.body.getPosition().y - 0.125f, 0.25f, 0.25f);
    }

    public void respawn() {
        this.respawnTime = 0.1f;
        Vector2 temp = new Vector2();
        Random r = new Random();
        temp.x = (float) r.nextInt(960);
        temp.y = (float) r.nextInt(540);
        this.body.setTransform(temp.x * Map.PPM, temp.y * Map.PPM, 0.0f);
        this.sensorbody.setTransform(temp.x * Map.PPM, temp.y * Map.PPM, 0.0f);
        this.pickedUP = false;
    }

    public void move(Vector2 move) {
        move.add(this.body.getPosition());
        this.body.setTransform(move, 0.0f);
        this.sensorbody.setTransform(move, 0.0f);
    }

    public void pickUP() {
        this.pickedUP = true;
    }

    public boolean isPickedUP() {
        return this.pickedUP;
    }

    public void respawnIn(MyRectangle myRect) {
        Vector2 temp = new Vector2();
        Random r = new Random();
        System.out.println(myRect);
        temp.x = (myRect.x + 0.25f) + (r.nextFloat() * (myRect.width - 0.25f));
        temp.y = (myRect.y + 0.25f) + (r.nextFloat() * (myRect.height - 0.25f));
        this.body.setTransform(temp, 0.0f);
        this.sensorbody.setTransform(temp, 0.0f);
        this.pickedUP = false;
    }

    public Vector2 getPosition() {
        return this.body.getPosition();
    }
}

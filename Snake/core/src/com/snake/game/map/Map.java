package com.snake.game.map;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.BodyDef.BodyType;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.snake.game.entity.Bubble;
import com.snake.game.entity.PickUP;
import com.snake.game.entity.Snake;
import java.util.ArrayList;

public abstract class Map {
    public static final float MAX_X = 9.599999f;
    public static final float MAX_Y = 5.4f;
    public static final float PPM = 0.01f;
    protected int amountPickUPsToWin;
    protected Image background;
    protected ArrayList<Bubble> bubbles;
    protected Body door;
    protected boolean doorOpen;
    protected int level;
    protected PickUP pickUp;
    protected Snake snake;
    protected Vector2 spawnPoint;
    protected ArrayList<Wall> walls;
    protected World world;

    public abstract void closeDoor();

    public abstract String getNameOfGamemode();

    public abstract void openDoor();

    public abstract void render(SpriteBatch spriteBatch);

    public abstract void update(float f);

    public ArrayList<Wall> getWalls() {
        return this.walls;
    }

    public void CreateDoorBody(float x, float y, float width, float height) {
        BodyDef bDef = new BodyDef();
        bDef.type = BodyType.StaticBody;
        Vector2 pos = new Vector2(x, y);
        pos.add(width / 2.0f, height / 2.0f);
        bDef.position.set(pos);
        FixtureDef fDef = new FixtureDef();
        fDef.restitution = 1.0f;
        fDef.friction = 0.0f;
        PolygonShape sh = new PolygonShape();
        sh.setAsBox(width / 2.0f, height / 2.0f, Vector2.Zero, 0.0f);
        fDef.shape = sh;
        this.door = this.world.createBody(bDef);
        this.door.setUserData("DOOR");
        this.door.createFixture(fDef);
    }

    public void CreateDoorSensorBody(float x, float y, float width, float height) {
        BodyDef bDef = new BodyDef();
        bDef.type = BodyType.DynamicBody;
        Vector2 pos = new Vector2(x, y);
        pos.add(width / 2.0f, height / 2.0f);
        bDef.position.set(pos);
        FixtureDef fDef = new FixtureDef();
        fDef.restitution = 1.0f;
        fDef.friction = 0.0f;
        fDef.isSensor = true;
        PolygonShape sh = new PolygonShape();
        sh.setAsBox(width / 2.0f, height / 2.0f, Vector2.Zero, 0.0f);
        fDef.shape = sh;
        this.door = this.world.createBody(bDef);
        this.door.setUserData("DOOR");
        this.door.createFixture(fDef);
    }

    public boolean isDoorOpen() {
        return this.doorOpen;
    }

    public void setDoorOpen(boolean doorOpen) {
        this.doorOpen = doorOpen;
    }

    public Vector2 getSpawnPoint() {
        return this.spawnPoint;
    }

    public void setSpawnPoint(Vector2 spawnPoint) {
        this.spawnPoint = spawnPoint;
    }

    public int getAmountPickUPsToWin() {
        return this.amountPickUPsToWin;
    }

    public void setAmountPickUPsToWin(int amountPickUPsToWin) {
        this.amountPickUPsToWin = amountPickUPsToWin;
    }

    public World getWorld() {
        return this.world;
    }

    public void setWorld(World world) {
        this.world = world;
    }

    public boolean canWin() {
        return true;
    }
}

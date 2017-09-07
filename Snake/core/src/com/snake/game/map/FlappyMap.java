package com.snake.game.map;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Contact;
import com.badlogic.gdx.physics.box2d.ContactImpulse;
import com.badlogic.gdx.physics.box2d.ContactListener;
import com.badlogic.gdx.physics.box2d.Manifold;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.snake.game.entity.Bubble;
import com.snake.game.entity.PickUP;
import com.snake.game.entity.Snake;
import com.snake.game.util.MyRectangle;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Random;

public class FlappyMap extends Map {
    private static final float DOOR_DISTANZ = 5.0f;
    private static final float DOOR_MAX_SIZE = 1.5f;
    private static final float DOOR_MAX_Y = 4.0f;
    private static final float DOOR_MIN_SIZE = 0.75f;
    private static final float DOOR_MIN_Y = 1.0f;
    private static final float MAP_SPEED = 0.29999998f;
    private static final String NAME_OF_GAMEMODE = "Flappy";
    private static final float SECURITY_DISTANZ_TO_WALL = 0.5f;
    private static final float WALL_MAX_DISTANCE = 5.0f;
    private static final float WALL_MIN_DISTANCE = 2.5f;
    private static final float WALL_SIZE = 0.59999996f;
    private float distanceLastDoor = 5.0f;
    private ArrayList<Wall> doorParts;
    private ArrayList<Wall> doors;
    private MyRectangle nextSpawnArea;
    private float real_map_speed;
    private boolean respawnPickUP;

    public FlappyMap(Snake snake) {
        this.snake = snake;
        this.walls = new ArrayList<Wall>();
        this.doors = new ArrayList<Wall>();
        this.doorParts = new ArrayList<Wall>();
        this.world = new World(new Vector2(0.0f, 0.0f), false);
        this.world.setContactListener(new ContactListener() {
            public void beginContact(Contact contact) {
                FlappyMap.this.checkCollision(contact);
            }

            public void endContact(Contact contact) {
                FlappyMap.this.AfterCollision(contact);
            }

            public void preSolve(Contact contact, Manifold oldManifold) {
            }

            public void postSolve(Contact contact, ContactImpulse impulse) {
            }
        });
        this.walls.add(new Wall(0.0f, 0.0f, 19.199999f, WALL_SIZE, this.world));
        this.walls.add(new Wall(0.0f, 4.8f, 19.199999f, WALL_SIZE, this.world));
        this.walls.add(new Wall(-0.59999996f, 0.0f, WALL_SIZE, Map.MAX_Y, this.world));
        this.walls.add(new Wall(11.4f, 0.0f, WALL_SIZE, Map.MAX_Y, this.world));
        this.real_map_speed = snake.getSpeed() * MAP_SPEED;
        System.out.println(snake.getSpeed());
        this.doors.add(new Wall(4.7999997f, 0.0f, WALL_SIZE, Map.MAX_Y, this.world));
        this.doors.add(new Wall(Map.MAX_X, 0.0f, WALL_SIZE, Map.MAX_Y, this.world));
        this.doors.add(new Wall(14.599999f, 0.0f, WALL_SIZE, Map.MAX_Y, this.world));
        this.doors.add(new Wall(19.599998f, 0.0f, WALL_SIZE, Map.MAX_Y, this.world));
        this.background = new Image(new TextureRegion(new Texture(Gdx.files.internal("map/background_grass.png"))));
        this.background.setSize(Map.MAX_X, Map.MAX_Y);
        this.pickUp = new PickUP(new Vector2(5.7999997f, 3.7f), this.world);
        this.nextSpawnArea = new MyRectangle(SECURITY_DISTANZ_TO_WALL, 1.1999999f, 3.7999997f, 3.0000002f);
        this.pickUp.respawnIn(this.nextSpawnArea);
        this.spawnPoint = new Vector2(1.7999997f, 2.7f);
        this.amountPickUPsToWin = -1;
        snake.setMaxCircleAmount(50);
        openDoor();
    }

    public void render(SpriteBatch batch) {
        this.background.draw(batch, DOOR_MIN_Y);
        Iterator it = this.walls.iterator();
        while (it.hasNext()) {
            ((Wall) it.next()).render(batch);
        }
        it = this.doors.iterator();
        while (it.hasNext()) {
            ((Wall) it.next()).render(batch);
        }
        it = this.doorParts.iterator();
        while (it.hasNext()) {
            ((Wall) it.next()).render(batch);
        }
        this.pickUp.render(batch);
    }

    public void update(float delta) {
        int i;
        this.distanceLastDoor -= this.real_map_speed * delta;
        if (!this.doorOpen) {
            this.world.destroyBody(this.door);
            openDoor();
        }
        if (this.distanceLastDoor <= 0.0f) {
            this.distanceLastDoor = (new Random().nextFloat() * WALL_MIN_DISTANCE) + WALL_MIN_DISTANCE;
            System.out.println(new StringBuilder(String.valueOf(this.doors.size())).append(" ").append(this.distanceLastDoor).toString());
            this.doors.add(new Wall(19.599998f + this.distanceLastDoor, 0.0f, WALL_SIZE, Map.MAX_Y, this.world));
        }
        this.world.step(delta, 6, 2);
        this.world.clearForces();
        for (i = this.doors.size() - 1; i >= 0; i--) {
            ((Wall) this.doors.get(i)).move(new Vector2(-1.0f, 0.0f).scl(this.real_map_speed * delta));
            if (((Wall) this.doors.get(i)).getPosition().x <= -0.59999996f) {
                this.doors.remove(i);
            }
        }
        for (i = this.doorParts.size() - 1; i >= 0; i--) {
            ((Wall) this.doorParts.get(i)).move(new Vector2(-1.0f, 0.0f).scl(this.real_map_speed * delta));
            if (((Wall) this.doorParts.get(i)).getPosition().x <= -0.59999996f) {
                this.doorParts.remove(i);
            }
        }
        if (this.door != null) {
            this.door.setLinearVelocity(new Vector2(this.real_map_speed * -1.0f, 0.0f));
        }
        this.nextSpawnArea.setCenter(this.nextSpawnArea.getCenter(new Vector2()).x - (this.real_map_speed * delta), 2.7f);
        if (this.pickUp.isPickedUP() || this.respawnPickUP) {
            this.pickUp.respawnIn(this.nextSpawnArea);
            this.respawnPickUP = false;
            return;
        }
        this.pickUp.move(new Vector2(-1.0f, 0.0f).scl(this.real_map_speed * delta));
        if (this.pickUp.getPosition().x <= -0.25f) {
            this.pickUp.respawnIn(this.nextSpawnArea);
        }
    }

    public void checkCollision(Contact contact) {
        Object a = contact.getFixtureA().getBody().getUserData();
        Object b = contact.getFixtureB().getBody().getUserData();
        if (a instanceof Snake) {
            if ((b instanceof String) && b == "DOOR") {
                this.doorOpen = false;
            }
            this.snake.OnCollision(contact.getFixtureB().getBody());
        } else if (b instanceof Snake) {
            if ((a instanceof String) && a == "DOOR") {
                this.doorOpen = false;
            }
            this.snake.OnCollision(contact.getFixtureA().getBody());
        } else if (a instanceof PickUP) {
            if (b instanceof Wall) {
                this.respawnPickUP = true;
            }
        } else if ((b instanceof PickUP) && (a instanceof Wall)) {
            this.respawnPickUP = true;
        }
    }

    public void AfterCollision(Contact contact) {
        Object a = contact.getFixtureA().getBody().getUserData();
        Object b = contact.getFixtureB().getBody().getUserData();
        if (a instanceof Bubble) {
            ((Bubble) a).OnCollision(contact.getFixtureB().getBody());
        }
        if (b instanceof Bubble) {
            ((Bubble) b).OnCollision(contact.getFixtureA().getBody());
        }
    }

    public void openDoor() {
        this.doorOpen = true;
        Wall w = (Wall) this.doors.remove(0);
        w.Destroy(this.world);
        float x = (w.getMyRectangle().x + WALL_SIZE) + SECURITY_DISTANZ_TO_WALL;
        this.nextSpawnArea = new MyRectangle(x, WALL_SIZE, (((Wall) this.doors.get(0)).getMyRectangle().x - x) - SECURITY_DISTANZ_TO_WALL, 3.7000003f);
        Random r = new Random();
        float doorSize = ((r.nextFloat() * DOOR_MIN_SIZE) + DOOR_MIN_SIZE) + DOOR_MIN_SIZE;
        float y = ((r.nextFloat() * 3.0f) + DOOR_MIN_Y) + DOOR_MIN_Y;
        this.doorParts.add(new Wall(w.getMyRectangle().x, (doorSize / 2.0f) + y, WALL_SIZE, 2.7f + doorSize, this.world));
        this.doorParts.add(new Wall(w.getMyRectangle().x, 0.0f - (doorSize / 2.0f), WALL_SIZE, y, this.world));
        CreateDoorSensorBody(w.getMyRectangle().x, y - (doorSize / 2.0f), WALL_SIZE, doorSize);
    }

    public void closeDoor() {
        this.doorOpen = false;
        this.door = null;
    }

    public String getNameOfGamemode() {
        return NAME_OF_GAMEMODE;
    }

    public boolean canWin() {
        return false;
    }
}

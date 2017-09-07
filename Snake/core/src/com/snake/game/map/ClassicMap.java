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

public class ClassicMap extends Map {
    private static final int AMOUNT_MORE_PICKUPS_PER_LEVEL = 3;
    private static final int AMOUNT_TO_ADVANCE_TO_NEXT_LEVEL = 9;
    private static final float DOOR_SIZE = 1.5999999f;
    private static final float LEVEL_SHRINK_SIZE = 0.75f;
    private static final String NAME_OF_GAMEMODE = "Classic";
    private static final float WALL_SIZE = 0.59999996f;
    private MyRectangle nextSpawnArea;

    public ClassicMap(int level, Snake snake) {
        float shrink;
        this.level = level;
        this.world = new World(new Vector2(0.0f, 0.0f), false);
        this.world.setContactListener(new ContactListener() {
            public void beginContact(Contact contact) {
                ClassicMap.this.checkCollision(contact);
            }

            public void endContact(Contact contact) {
                ClassicMap.this.AfterCollision(contact);
            }

            public void preSolve(Contact contact, Manifold oldManifold) {
            }

            public void postSolve(Contact contact, ContactImpulse impulse) {
            }
        });
        this.walls = new ArrayList();
        this.bubbles = new ArrayList();
        this.walls.add(new Wall(0.0f, 0.0f, Map.MAX_X, WALL_SIZE, this.world));
        if (level > 3) {
            shrink = 3.0f;
        } else {
            shrink = (float) level;
        }
        this.walls.add(new Wall(0.0f, 0.0f, WALL_SIZE + (shrink * LEVEL_SHRINK_SIZE), Map.MAX_Y, this.world));
        this.walls.add(new Wall(8.999999f - (shrink * LEVEL_SHRINK_SIZE), 0.0f, WALL_SIZE + (((float) this.level) * LEVEL_SHRINK_SIZE), Map.MAX_Y, this.world));
        this.walls.add(new Wall(0.0f, 4.8f, Map.MAX_X, WALL_SIZE, this.world));
        this.snake = snake;
        this.nextSpawnArea = new MyRectangle((shrink * LEVEL_SHRINK_SIZE) + WALL_SIZE, WALL_SIZE, Map.MAX_X - (2.0f * ((shrink * LEVEL_SHRINK_SIZE) + WALL_SIZE)), 4.2000003f);
        this.background = new Image(new TextureRegion(new Texture(Gdx.files.internal("map/background_grass.png"))));
        this.background.setSize(Map.MAX_X, Map.MAX_Y);
        this.pickUp = new PickUP(new Vector2(5.7999997f, 3.7f), this.world);
        this.spawnPoint = new Vector2(4.7999997f, 2.7f);
        this.amountPickUPsToWin = 9;
    }

    public void update(float delta) {
        this.world.step(0.016666668f, 6, 2);
        this.world.clearForces();
        if (!this.doorOpen && this.snake.getPickups() == this.amountPickUPsToWin + (this.level * 3)) {
            openDoor();
        }
        if ((this.snake.getPickups() > this.amountPickUPsToWin + (this.level * 3) || (this.level > 3 && this.bubbles.size() < this.level - 3)) && this.pickUp.isPickedUP()) {
            this.bubbles.add(new Bubble(new Vector2(4.7999997f, 2.7f), this.snake, this.world));
        }
        Iterator it = this.bubbles.iterator();
        while (it.hasNext()) {
            ((Bubble) it.next()).update(delta);
        }
        if (this.pickUp.isPickedUP()) {
            this.pickUp.respawnIn(this.nextSpawnArea);
        }
    }

    public void checkCollision(Contact contact) {
        Object a = contact.getFixtureA().getBody().getUserData();
        Object b = contact.getFixtureB().getBody().getUserData();
        if (a instanceof Snake) {
            this.snake.OnCollision(contact.getFixtureB().getBody());
        } else if (b instanceof Snake) {
            this.snake.OnCollision(contact.getFixtureA().getBody());
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

    public void render(SpriteBatch batch) {
        this.background.draw(batch, 1.0f);
        Iterator it = this.walls.iterator();
        while (it.hasNext()) {
            ((Wall) it.next()).render(batch);
        }
        it = this.bubbles.iterator();
        while (it.hasNext()) {
            ((Bubble) it.next()).render(batch);
        }
        this.pickUp.render(batch);
    }

    public ArrayList<Wall> getWalls() {
        return this.walls;
    }

    public void openDoor() {
        ((Wall) this.walls.get(3)).Destroy(this.world);
        this.walls.set(3, new Wall(0.0f, 4.8f, 3.9999998f, WALL_SIZE, this.world));
        this.walls.add(new Wall(5.5999994f, 4.8f, 3.9999998f, WALL_SIZE, this.world));
        CreateDoorBody(3.9999998f, 5.2000003f, DOOR_SIZE, 0.099999994f);
        this.doorOpen = true;
    }

    public boolean isDoorOpen() {
        return this.doorOpen;
    }

    public void setDoorOpen(boolean doorOpen) {
        this.doorOpen = doorOpen;
    }

    public void closeDoor() {
        Iterator it = this.walls.iterator();
        while (it.hasNext()) {
            ((Wall) it.next()).Destroy(this.world);
        }
        this.walls = new ArrayList();
        this.walls.add(new Wall(0.0f, 0.0f, Map.MAX_X, WALL_SIZE, this.world));
        this.walls.add(new Wall(0.0f, 0.0f, WALL_SIZE + (((float) this.level) * LEVEL_SHRINK_SIZE), Map.MAX_Y, this.world));
        this.walls.add(new Wall(8.999999f - (((float) this.level) * LEVEL_SHRINK_SIZE), 0.0f, WALL_SIZE + (((float) this.level) * LEVEL_SHRINK_SIZE), Map.MAX_Y, this.world));
        this.walls.add(new Wall(0.0f, 4.8f, Map.MAX_X, WALL_SIZE, this.world));
        this.doorOpen = false;
    }

    public String getNameOfGamemode() {
        return NAME_OF_GAMEMODE;
    }
}

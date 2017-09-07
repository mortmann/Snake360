package com.snake.game.entity;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer.ShapeType;
import com.badlogic.gdx.math.Circle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.BodyDef.BodyType;
import com.badlogic.gdx.physics.box2d.CircleShape;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.World;
import com.snake.game.map.Wall;
import com.snake.game.util.Difficulty;
import java.util.LinkedList;

public class Snake {
    private static final int ADD_CIRCLE_AMOUNT = 5;
    private static final float DISTANZ_CIRCLE_MODIFIER = 0.016665999f;
    private static final float EASY_SPEED = 1.5f;
    private static final float HARD_SPEED = 3.5f;
    private static final float MIN_DISTANCE_CIRCLE = 0.029000001f;
    private static final float NORMAL_SPEED = 2.5f;
    private static final float SIZE = 0.099999994f;
    private static final float TURN_ADD_SPEED = 200.0f;
    private static final float VERYHARD_SPEED = 4.5f;
    private static final float deltaCircleTime = 0.016666f;
    private boolean addedCircle;
    private float angle;
    private LinkedList<Body> bodies;
    private Body body;
    private int circleAmount = 5;
    private LinkedList<Circle> circles;
    private boolean dead;
    private float deltaSum;
    private int difficulty = 0;
    private Circle lastCircle;
    private float length;
    private int maxCircleAmount = -1;
    private Vector2 move;
    private Circle myCircle;
    private int pickups = 0;
    private int score = 0;
    private ShapeRenderer shapeRenderer;
    private float speed;
    private float tick;
    private int turnDir;
    private float turnspeed;
    private boolean won;
    private World world;

    public Snake() {
        SetUp();
    }

    public Snake(int score) {
        this.score = score;
        SetUp();
    }

    private void SetUp() {
        setDead(false);
        this.move = new Vector2();
        this.move.y = 1.0f;
        this.angle = 0.0f;
        this.bodies = new LinkedList();
        this.circles = new LinkedList();
        this.shapeRenderer = new ShapeRenderer();
        this.won = false;
        this.dead = false;
    }

    public void update(float delta) {
        this.tick += delta;
        this.deltaSum += delta;
        int f = Math.round(this.deltaSum / deltaCircleTime);
        if (this.tick >= deltaCircleTime) {
            int i;
            this.tick -= deltaCircleTime;
            this.length = 0.0f;
            for (i = this.circles.size() - 1; i >= 1; i -= 2) {
                this.length += new Vector2(((Circle) this.circles.get(i)).x, ((Circle) this.circles.get(i)).y).dst(new Vector2(((Circle) this.circles.get(i - 1)).x, ((Circle) this.circles.get(i - 1)).y));
                this.length = (float) Math.round(this.length);
            }
            this.angle += (((float) this.turnDir) * this.turnspeed) * delta;
            this.angle %= 360.0f;
            this.angle = (float) Math.round(this.angle);
            this.move = new Vector2();
            this.move.y = 1.0f;
            this.move.setAngle(this.angle);
            this.move.nor();
            if (this.lastCircle != null) {
                Vector2 v1 = new Vector2(this.lastCircle.x, this.lastCircle.y);
                Vector2 v2 = new Vector2(((Circle) this.circles.getFirst()).x, ((Circle) this.circles.getFirst()).y);
                v2.sub(v1);
                v2.nor();
                v1.add((v2.x * this.speed) * delta, (v2.y * this.speed) * delta);
                this.lastCircle.setPosition(v1);
            }
            this.body.setLinearVelocity(this.move.scl(this.speed));
            this.myCircle.setPosition(this.body.getPosition());
            float dst = new Vector2(this.myCircle.x, this.myCircle.y).dst(new Vector2(((Circle) this.circles.getLast()).x, ((Circle) this.circles.getLast()).y));
            if (this.circles.size() <= 1) {
                this.circles.add(new Circle(this.myCircle));
                this.bodies.add(CreateCircle(this.myCircle));
            } else if (dst >= 0.058000002f + (((float) this.difficulty) * DISTANZ_CIRCLE_MODIFIER)) {
                this.circles.add(new Circle(this.myCircle));
                this.bodies.add(CreateCircle(this.myCircle));
                this.addedCircle = true;
            }
            if (this.deltaSum > deltaCircleTime) {
                for (i = f; i >= 0; i--) {
                    if (this.circles.size() > this.circleAmount && this.addedCircle) {
                        this.world.destroyBody((Body) this.bodies.removeFirst());
                        this.lastCircle = new Circle((Circle) this.circles.removeFirst());
                    }
                }
                this.deltaSum = 0.0f;
            }
        }
    }

    private Body CreateCircle(Circle c) {
        BodyDef bDef = new BodyDef();
        bDef.type = BodyType.StaticBody;
        bDef.position.set(new Vector2(c.x, c.y));
        FixtureDef fDef = new FixtureDef();
        CircleShape sh = new CircleShape();
        sh.setRadius(0.049999997f);
        fDef.shape = sh;
        Body b = this.world.createBody(bDef);
        b.createFixture(fDef);
        return b;
    }

    public void render(SpriteBatch batch) {
        batch.end();
        Gdx.gl.glBlendFunc(GL20.GL_SRC_ALPHA, GL20.GL_ONE_MINUS_SRC_ALPHA);
        this.shapeRenderer.setProjectionMatrix(batch.getProjectionMatrix());
        this.shapeRenderer.setAutoShapeType(true);
        this.shapeRenderer.begin();
        this.shapeRenderer.set(ShapeType.Filled);
        for (int i = this.circles.size() - 1; i > 0; i--) {
            this.shapeRenderer.setColor(Color.GRAY);
            this.shapeRenderer.circle(((Circle) this.circles.get(i)).x, ((Circle) this.circles.get(i)).y, 0.049999997f, 20);
            this.shapeRenderer.rectLine(((Circle) this.circles.get(i)).x, ((Circle) this.circles.get(i)).y, ((Circle) this.circles.get(i - 1)).x, ((Circle) this.circles.get(i - 1)).y, SIZE);
        }
        this.shapeRenderer.setColor(Color.BLACK);
        this.shapeRenderer.circle(this.myCircle.x, this.myCircle.y, 0.049999997f, 20);
        if (this.lastCircle != null) {
            Color r = Color.LIGHT_GRAY;
            r.a = 0.5f;
            this.shapeRenderer.setColor(r);
            this.shapeRenderer.circle(this.lastCircle.x, this.lastCircle.y, 0.049999997f, 20);
            this.shapeRenderer.rectLine(this.lastCircle.x, this.lastCircle.y, ((Circle) this.circles.getFirst()).x, ((Circle) this.circles.getFirst()).y, SIZE);
        }
        this.shapeRenderer.end();
        batch.begin();
    }

    public void OnCollision(Body body) {
        if (body.getUserData() instanceof Wall) {
            this.dead = true;
        } else if (this.bodies.contains(body)) {
            if (this.bodies.indexOf(body) < this.bodies.size() - 4) {
                this.dead = true;
            }
        } else if ((body.getUserData() instanceof PickUP) && !((Fixture) body.getFixtureList().get(0)).isSensor()) {
            if (this.maxCircleAmount == -1 || this.circleAmount <= this.maxCircleAmount) {
                this.circleAmount += 5;
            }
            ((PickUP) body.getUserData()).pickUP();
            this.pickups++;
            this.score++;
        } else if (body.getUserData() instanceof Bubble) {
            this.dead = true;
        } else if ((body.getUserData() instanceof String) && ((String) body.getUserData()).contains("DOOR")) {
            System.out.println("ISDOOR");
            this.won = true;
        }
    }

    public boolean isDead() {
        return this.dead;
    }

    public void setDead(boolean dead) {
        this.dead = dead;
    }

    public int getPickups() {
        return this.pickups;
    }

    public void setPickups(int pickups) {
        this.pickups = pickups;
    }

    public boolean isWon() {
        return this.won;
    }

    public void setWon(boolean won) {
        this.won = won;
    }

    public void setTurnDir(int turnDir) {
        this.turnDir = turnDir;
    }

    public void setDifficulty(String gamemode) {
        if (gamemode.contains(Difficulty.Easy.name())) {
            this.speed = EASY_SPEED;
            this.difficulty = 0;
        } else if (gamemode.contains(Difficulty.Normal.name())) {
            this.speed = NORMAL_SPEED;
            this.difficulty = 1;
        } else if (gamemode.contains(Difficulty.VeryHard.name())) {
            this.speed = VERYHARD_SPEED;
            this.difficulty = 3;
        } else if (gamemode.contains(Difficulty.Hard.name())) {
            this.speed = HARD_SPEED;
            this.difficulty = 2;
        }
        this.turnspeed = TURN_ADD_SPEED + (this.speed * 100.0f);
    }

    public void setPosition(Vector2 position, World world) {
        this.world = world;
        BodyDef bDef = new BodyDef();
        bDef.type = BodyType.DynamicBody;
        bDef.position.set(position);
        FixtureDef fDef = new FixtureDef();
        fDef.isSensor = true;
        CircleShape sh = new CircleShape();
        sh.setRadius(0.049999997f);
        fDef.shape = sh;
        this.body = world.createBody(bDef);
        this.body.setUserData(this);
        this.body.createFixture(fDef);
        this.myCircle = new Circle(position.x, position.y, 0.049999997f);
        this.circles.add(this.myCircle);
    }

    public int getScore() {
        return this.score;
    }

    public void addScore() {
        this.score++;
    }

    public int getMaxCircleAmount() {
        return this.maxCircleAmount;
    }

    public void setMaxCircleAmount(int maxCircleAmount) {
        this.maxCircleAmount = maxCircleAmount;
    }

    public Circle getMyCircle() {
        return this.myCircle;
    }

    public void setMyCircle(Circle myCircle) {
        this.myCircle = myCircle;
    }

    public LinkedList<Circle> getCircles() {
        return this.circles;
    }

    public void setCircles(LinkedList<Circle> circles) {
        this.circles = circles;
    }

    public float getSpeed() {
        return this.speed;
    }

    public void setSpeed(float speed) {
        this.speed = speed;
    }

    public Vector2 getPosition() {
        return this.body.getPosition();
    }
}

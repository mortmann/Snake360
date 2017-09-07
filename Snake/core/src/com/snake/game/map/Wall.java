package com.snake.game.map;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.BodyDef.BodyType;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.World;
import com.snake.game.util.MyRectangle;

public class Wall {
    private Body body;
    private MyRectangle myRectangle;
    private TextureRegion texReg;

    public Wall(float x, float y, float width, float height, World world) {
        this.myRectangle = new MyRectangle(x, y, width, height);
        this.texReg = new TextureRegion(new Texture(Gdx.files.internal("map/wall.png")));
        this.texReg = this.texReg.split(60, 60)[0][0];
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
        this.body = world.createBody(bDef);
        this.body.createFixture(fDef);
        this.body.setUserData(this);
    }

    public void Destroy(World w) {
        w.destroyBody(this.body);
    }

    public void render(SpriteBatch batch) {
        this.myRectangle.drawDebug(batch);
    }

    public MyRectangle getMyRectangle() {
        return this.myRectangle;
    }

    public void setMyRectangle(MyRectangle myRectangle) {
        this.myRectangle = myRectangle;
    }

    public void move(Vector2 move) {
        move.add(this.body.getPosition());
        this.body.setTransform(move, 0.0f);
        this.myRectangle.setCenter(this.body.getWorldCenter());
    }

    public Vector2 getPosition() {
        return this.body.getPosition();
    }
}

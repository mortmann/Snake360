package com.snake.game.util;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer.ShapeType;
import com.badlogic.gdx.math.Matrix4;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;

public class MyRectangle extends Rectangle {
    private static final long serialVersionUID = -3160874626585931530L;
    private Object myData;
    private ShapeRenderer shapeRenderer;

    public MyRectangle(float x, float y, float width, float height, Object myData) {
        setMyData(myData);
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
    }

    public MyRectangle(float x, float y, float width, float height) {
        setMyData(this.myData);
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
    }

    public MyRectangle(Vector2 temp, float size) {
        this.x = temp.x;
        this.y = temp.y;
        this.width = size;
        this.height = size;
    }

    public Object getMyData() {
        return this.myData;
    }

    public void setMyData(Object myData) {
        this.myData = myData;
    }

    public void drawDebug(SpriteBatch batch) {
        batch.end();
        if (this.shapeRenderer == null) {
            this.shapeRenderer = new ShapeRenderer();
            this.shapeRenderer.setProjectionMatrix(batch.getProjectionMatrix());
        }
        this.shapeRenderer.setAutoShapeType(true);
        this.shapeRenderer.begin();
        this.shapeRenderer.setColor(0.5f, 0.5f, 0.5f, 1.0f);
        this.shapeRenderer.set(ShapeType.Filled);
        this.shapeRenderer.rect(this.x, this.y, this.width, this.height);
        this.shapeRenderer.end();
        batch.begin();
    }

    public void drawDebug(Matrix4 projectionMatrix) {
        if (this.shapeRenderer == null) {
            this.shapeRenderer = new ShapeRenderer();
            this.shapeRenderer.setProjectionMatrix(projectionMatrix);
        }
        this.shapeRenderer.rect(this.x, this.y, this.width, this.height);
    }

    public void drawDebug(SpriteBatch batch, Color color) {
        if (this.shapeRenderer == null) {
            this.shapeRenderer = new ShapeRenderer();
            this.shapeRenderer.setProjectionMatrix(batch.getProjectionMatrix());
        }
        this.shapeRenderer.setAutoShapeType(true);
        this.shapeRenderer.begin();
        this.shapeRenderer.setColor(color);
        this.shapeRenderer.set(ShapeType.Filled);
        this.shapeRenderer.rect(this.x, this.y, this.width, this.height);
        this.shapeRenderer.end();
    }
}

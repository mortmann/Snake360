package com.snake.game.map;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.snake.game.entity.Snake;

public class BigMap extends Map{
	private static final String NAME_OF_GAMEMODE="Big";
	public BigMap(Snake snake) {
		this.snake = snake;
	}

	@Override
	public void render(SpriteBatch batch) {
		
	}

	@Override
	public void update(float delta) {
		
	}

	@Override
	public void openDoor() {
		
	}

	@Override
	public void closeDoor() {
		
	}

	@Override
	public String getNameOfGamemode() {
		return NAME_OF_GAMEMODE;
	}



}

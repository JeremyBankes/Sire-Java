package com.jeremy.sire.graphics;

import com.jeremy.sire.Game;

public class Camera {

	private Game game;

	public Camera(Game game) {
		this.game = game;
	}

	public int transformX(float x) {
		return (int) Math.floor(game.getRenderer().getWidth() * x);
	};

	public int transformY(float y) {
		return (int) Math.floor(game.getRenderer().getHeight() * y);
	}

	public int transformWidth(float width) {
		return (int) Math.ceil(game.getRenderer().getWidth() * width);
	}

	public int transformHeight(float height) {
		return (int) Math.ceil(game.getRenderer().getHeight() * height);
	}

}

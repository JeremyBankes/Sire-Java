package com.jeremy.sire.helpers.world;

import com.jeremy.sire.Game;
import com.jeremy.sire.graphics.Camera;
import com.jeremy.sire.graphics.Renderer;

public class WorldCamera extends Camera {

	private float x, y;
	private final Renderer renderer;
	private final float aspectRatio;
	private float scale;

	public WorldCamera(Game game, float aspectRatio, float scale) {
		super(game);
		this.aspectRatio = aspectRatio;
		this.scale = scale;
		renderer = game.getRenderer();
	}

	@Override
	public int transformX(float x) {
		return Math.round(renderer.getWidth() * (x - this.x) * scale) + renderer.getWidth() / 2;
	}

	public float transformX(int x) {
		return (float) (x - renderer.getWidth() / 2) / (renderer.getWidth() * scale);
	}

	@Override
	public int transformY(float y) {
		return Math.round(renderer.getHeight() * (y - this.y) * scale * -aspectRatio) + renderer.getHeight() / 2;
	}

	public float transformY(int y) {
		return (float) (y - renderer.getHeight() / 2) / -(renderer.getHeight() * scale * aspectRatio);
	}

	@Override
	public int transformWidth(float width) {
		return Math.round(renderer.getWidth() * width * scale);
	}

	@Override
	public int transformHeight(float height) {
		return Math.round(renderer.getHeight() * height * scale * aspectRatio);
	}

	public float getScale() {
		return scale;
	}

	public void setScale(float scale) {
		this.scale = scale;
	}

	public float getX() {
		return x;
	}

	public float getY() {
		return y;
	}

	public void setX(float x) {
		this.x = x;
	}

	public void setY(float y) {
		this.y = y;
	}

}

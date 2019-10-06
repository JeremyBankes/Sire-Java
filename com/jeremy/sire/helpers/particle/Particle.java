package com.jeremy.sire.helpers.particle;

import java.awt.Color;
import java.awt.image.BufferedImage;
import java.util.Random;

import com.jeremy.sire.graphics.Renderer;
import com.jeremy.sire.helpers.Collidable;
import com.jeremy.sire.helpers.animation.Animation;
import com.jeremy.sire.helpers.tools.Anchor;
import com.jeremy.sire.helpers.tools.Bounds;

public abstract class Particle implements Collidable {

	protected static final Random RANDOM = new Random();

	private final Bounds bounds;

	private int lifetime;
	private int age;

	private float xVelocity, yVelocity;
	// private float rotation;
	// TODO: private float scale = 0.25f;

	private Animation animation;
	private BufferedImage texture;
	private Color color;

	public Particle(float x, float y, float width, float height, int lifetime) {
		bounds = new Bounds(Anchor.CENTER, x, y, width, height);
		this.lifetime = lifetime;
	}

	public abstract void onSpawn();

	public void tick() {
		bounds.setX(bounds.getX() + xVelocity);
		bounds.setY(bounds.getY() + yVelocity);
		age++;
	}

	public void render(Renderer renderer) {
		if (getAnimation() != null) {
			renderer.drawImage(getAnimation().getFrame(), //
					bounds.getLeft(), //
					bounds.getTop(), //
					bounds.getRight() - bounds.getLeft(), //
					bounds.getTop() - bounds.getBottom()//
			);
		}
		if (getTexture() != null) {
			renderer.drawImage(getTexture(), //
					bounds.getLeft(), //
					bounds.getTop(), //
					bounds.getRight() - bounds.getLeft(), //
					bounds.getTop() - bounds.getBottom()//
			);
		}
		if (getColor() != null) {
			renderer.setColor(getColor());
			renderer.fillRectangle( //
					bounds.getLeft(), //
					bounds.getTop(), //
					bounds.getRight() - bounds.getLeft(), //
					bounds.getTop() - bounds.getBottom()//
			);
		}
	}

	public boolean isRemoved() {
		return age >= lifetime;
	}

	public int getLifetime() {
		return lifetime;
	}

	public void setLifetime(int lifetime) {
		this.lifetime = lifetime;
	}

	public int getAge() {
		return age;
	}

	public void setAge(int age) {
		this.age = age;
	}

	public float getXVelocity() {
		return xVelocity;
	}

	public void setXVelocity(float xVelocity) {
		this.xVelocity = xVelocity;
	}

	public float getYVelocity() {
		return yVelocity;
	}

	public void setYVelocity(float yVelocity) {
		this.yVelocity = yVelocity;
	}

	public Animation getAnimation() {
		return animation;
	}

	public void setAnimation(Animation animation) {
		this.animation = animation;
	}

	public BufferedImage getTexture() {
		return texture;
	}

	public void setTexture(BufferedImage texture) {
		this.texture = texture;
	}

	public Color getColor() {
		return color;
	}

	public void setColor(Color color) {
		this.color = color;
	}

	public Bounds getCollisionBox() {
		return bounds;
	}

	protected float random(float min, float max) {
		return min + RANDOM.nextFloat() * (max - min);
	}

}

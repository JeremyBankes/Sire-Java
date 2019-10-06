package com.jeremy.sire.helpers.entity;

import java.awt.Color;
import java.awt.image.BufferedImage;
import java.util.UUID;

import com.jeremy.sire.graphics.Renderer;
import com.jeremy.sire.helpers.Collidable;
import com.jeremy.sire.helpers.animation.Animation;
import com.jeremy.sire.helpers.tools.Anchor;
import com.jeremy.sire.helpers.tools.Bounds;
import com.jeremy.sire.helpers.world.World;

public abstract class Entity implements Collidable {

	private final UUID uuid;

	private Bounds collisionBox;
	private Bounds displayBox;
	private float xVelocity, yVelocity;

	private World world;

	private long age;
	private boolean removed;

	private Animation animation;
	private BufferedImage texture;
	private Color color;

	public Entity(UUID uuid, float width, float height, Anchor anchor) {
		this.uuid = uuid;
		collisionBox = new Bounds(anchor, 0, 0, width, height);
		displayBox = new Bounds(collisionBox);
	}

	public void tick() {
		age += 1;
		if (animation != null) {
			animation.tick();
		}
		setLocation(collisionBox.getX() + getXVelocity(), collisionBox.getY() + getYVelocity());
	}

	public void render(Renderer renderer) {
		if (getAnimation() != null) {
			renderer.drawImage(getAnimation().getFrame(), //
					displayBox.getLeft(), //
					displayBox.getTop(), //
					displayBox.getRight() - displayBox.getLeft(), //
					displayBox.getTop() - displayBox.getBottom()//
			);
		}
		if (getTexture() != null) {
			renderer.drawImage(getTexture(), //
					displayBox.getLeft(), //
					displayBox.getTop(), //
					displayBox.getRight() - displayBox.getLeft(), //
					displayBox.getTop() - displayBox.getBottom()//
			);
		}
		if (getColor() != null) {
			renderer.setColor(getColor());
			renderer.fillRectangle( //
					displayBox.getLeft(), //
					displayBox.getTop(), //
					displayBox.getRight() - displayBox.getLeft(), //
					displayBox.getTop() - displayBox.getBottom()//
			);
		}
		if (world.isShowCollisionBoxes()) {
			renderer.setColor(Color.WHITE);
			renderer.traceRectangle( //
					collisionBox.getLeft(), //
					collisionBox.getTop(), //
					collisionBox.getRight() - collisionBox.getLeft(), //
					collisionBox.getTop() - collisionBox.getBottom()//
			);
		}
	}

	public void setLocation(float x, float y) {
		collisionBox.setX(x);
		collisionBox.setY(y);
		displayBox.setX(x);
		displayBox.setY(y);
	}

	public void setVelocity(float xVelocity, float yVelocity) {
		setXVelocity(xVelocity);
		setYVelocity(yVelocity);
	}

	public void setVelocity(float magnitude, double radians) {
		yVelocity = (float) (magnitude * Math.sin(radians));
		xVelocity = (float) (magnitude * Math.cos(radians));
	}

	public void addXVelocity(float xVelocity) {
		setXVelocity(getXVelocity() + xVelocity);
	}

	public void addYVelocity(float yVelocity) {
		setYVelocity(getYVelocity() + yVelocity);
	}

	public void addVelocity(float xVelocity, float yVelocity) {
		addXVelocity(yVelocity);
		addYVelocity(xVelocity);
	}

	public boolean isRemoved() {
		return removed;
	}

	public void remove() {
		removed = true;
	}

	public UUID getUUID() {
		return uuid;
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

	public float getX() {
		return getCollisionBox().getX();
	}

	public float getY() {
		return getCollisionBox().getY();
	}

	public long getAge() {
		return age;
	}

	public World getWorld() {
		return world;
	}

	public void setWorld(World world) {
		this.world = world;
	}

	public Bounds getDisplayBox() {
		return displayBox;
	}

	public void setDisplayBox(Bounds displayBox) {
		this.displayBox = displayBox;
	}

	public Bounds getCollisionBox() {
		return collisionBox;
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

}

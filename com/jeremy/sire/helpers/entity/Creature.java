package com.jeremy.sire.helpers.entity;

import java.awt.Color;
import java.util.UUID;

import com.jeremy.sire.graphics.Renderer;
import com.jeremy.sire.helpers.tools.Anchor;

public abstract class Creature extends Entity {

	private String name;
	private boolean nameShown;
	private float maxHealth, health;
	private boolean alive;

	public Creature(UUID uuid, float width, float height, Anchor anchor, float maxHealth) {
		super(uuid, width, height, anchor);
		this.maxHealth = health = maxHealth;
		name = getClass().getSimpleName().replaceAll("(?!^)(?=[A-Z])", " ");
		nameShown = true;
	}

	@Override
	public void tick() {
		super.tick();
		if (health <= 0) {
			alive = false;
		}
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public float getMaxHealth() {
		return maxHealth;
	}

	public void setMaxHealth(float maxHealth) {
		this.maxHealth = maxHealth;
	}

	public float getHealth() {
		return health;
	}

	public void setHealth(float health) {
		this.health = health;
	}

	public void addHealth(float health) {
		setHealth(getHealth() + health);
	}

	public boolean isAlive() {
		return alive;
	}

	public void setAlive(boolean alive) {
		this.alive = alive;
	}

	public boolean isNameShown() {
		return nameShown;
	}

	public void setNameShown(boolean nameShown) {
		this.nameShown = nameShown;
	}

	@Override
	public void render(Renderer renderer) {
		super.render(renderer);
		renderName(renderer);
	}

	protected void renderName(Renderer renderer) {
		if (isNameShown()) {
			renderer.setColor(Color.WHITE);
			renderer.drawText(getName(), getX(), getDisplayBox().getTop());
		}
	}

}

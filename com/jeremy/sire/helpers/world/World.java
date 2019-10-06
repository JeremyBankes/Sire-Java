package com.jeremy.sire.helpers.world;

import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.UUID;

import com.jeremy.sire.graphics.Camera;
import com.jeremy.sire.graphics.Renderer;
import com.jeremy.sire.helpers.entity.Entity;
import com.jeremy.sire.helpers.particle.Particle;

public abstract class World {

	private final HashSet<Entity> toSpawn;
	private final HashMap<UUID, Entity> entities;
	private final HashSet<Particle> particles;

	private boolean showCollisionBoxes = false;

	private Camera camera;

	public World(Camera camera) {
		this.camera = camera;
		toSpawn = new HashSet<Entity>();
		entities = new HashMap<UUID, Entity>();
		particles = new HashSet<Particle>();
	}

	public void tick() {
		toSpawn.forEach(entity -> entities.put(entity.getUUID(), entity));
		getEntities().forEach(entity -> entity.tick());
		entities.entrySet().removeIf(entry -> {
			Entity entity = entry.getValue();
			if (entity.isRemoved()) {
				entity.setWorld(null);
				return true;
			}
			return false;
		});
		particles.forEach(particle -> particle.tick());
		particles.removeIf(particle -> particle.isRemoved());
	}

	public void render(Renderer renderer) {
		renderer.setCamera(getCamera());
		renderDynamicObjects(renderer);
	}

	protected void renderDynamicObjects(Renderer renderer) {
		getEntities().forEach(entity -> entity.render(renderer));
		particles.forEach(particle -> particle.render(renderer));
	}

	public void spawnEntity(Entity entity, float x, float y) {
		entity.setLocation(x, y);
		toSpawn.add(entity);
		entity.setWorld(this);
	}

	public void spawnEntity(Entity entity) {
		spawnEntity(entity, entity.getX(), entity.getY());
	}

	public Entity getEntity(UUID uuid) {
		return entities.get(uuid);
	}

	public Collection<Entity> getEntities() {
		return entities.values();
	}

	public void spawnParticles(Particle particle, int count) {
		for (int i = 0; i < count; i++) {
			particles.add(particle);
			particle.onSpawn();
		}
	}

	public boolean isShowCollisionBoxes() {
		return showCollisionBoxes;
	}

	public void setShowCollisionBoxes(boolean showCollisionBoxes) {
		this.showCollisionBoxes = showCollisionBoxes;
	}

	public Camera getCamera() {
		return camera;
	}

	public void setCamera(Camera camera) {
		this.camera = camera;
	}

}

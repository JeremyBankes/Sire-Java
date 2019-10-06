package com.jeremy.sire.helpers;

import com.jeremy.sire.helpers.entity.Entity;
import com.jeremy.sire.helpers.tools.Bounds;
import com.jeremy.sire.helpers.world.tile.Tile;
import com.jeremy.sire.helpers.world.tile.TileWorld;

public class Physics {

	public static void smoothMovement(Entity entity, Controls controls, float maxVelocity, float acceleration) {
		if (controls.isMoveUp()) {
			if (entity.getYVelocity() < maxVelocity) {
				entity.addYVelocity(acceleration);
			}
		}
		if (controls.isMoveDown()) {
			if (entity.getYVelocity() > -maxVelocity) {
				entity.addYVelocity(-acceleration);
			}
		}
		if (controls.isMoveRight()) {
			if (entity.getXVelocity() < maxVelocity) {
				entity.addXVelocity(acceleration);
			}
		}
		if (controls.isMoveLeft()) {
			if (entity.getXVelocity() > -maxVelocity) {
				entity.addXVelocity(-acceleration);
			}
		}
	}

	public static void slow(Entity entity, Controls controls, float deceleration) {
		if (!controls.isMoveRight()) {
			if (entity.getXVelocity() > 0) {
				entity.addXVelocity(-deceleration);
				if (entity.getXVelocity() < 0) {
					entity.setXVelocity(0);
				}
			}
		}
		if (!controls.isMoveLeft()) {
			if (entity.getXVelocity() < 0) {
				entity.addXVelocity(deceleration);
				if (entity.getXVelocity() > 0) {
					entity.setXVelocity(0);
				}

			}
		}
		if (!controls.isMoveUp()) {
			if (entity.getYVelocity() > 0) {
				entity.addYVelocity(-deceleration);
				if (entity.getYVelocity() < 0) {
					entity.setYVelocity(0);
				}
			}
		}
		if (!controls.isMoveDown()) {
			if (entity.getYVelocity() < 0) {
				entity.addYVelocity(deceleration);
				if (entity.getYVelocity() > 0) {
					entity.setYVelocity(0);
				}
			}
		}
	}

	public void drag(Entity entity, float drag) {
		if (entity.getXVelocity() > 0) {
			entity.addXVelocity(-drag);
			if (entity.getXVelocity() < 0) {
				entity.setXVelocity(0);
			}
		} else if (entity.getXVelocity() < 0) {
			entity.addXVelocity(drag);
			if (entity.getXVelocity() > 0) {
				entity.setXVelocity(0);
			}
		}
		if (entity.getYVelocity() > 0) {
			entity.addYVelocity(-drag);
			if (entity.getYVelocity() < 0) {
				entity.setYVelocity(0);
			}
		} else if (entity.getYVelocity() < 0) {
			entity.addYVelocity(drag);
			if (entity.getYVelocity() > 0) {
				entity.setYVelocity(0);
			}
		}
	}

	public static void collision(Collidable collidable, TileWorld world) {
		Bounds collisionBox = collidable.getCollisionBox();

		final float check = 0.25f;
		final float roundDown = 0.0001f;

		// TODO: Use other pole velocity in future position checks for more accurate collisions
		
		Bounds collisionWith = null;
		float bottomFuture = collisionBox.getBottom() + collidable.getYVelocity();
		for (float x = collisionBox.getLeft(); x < collisionBox.getRight() + check && collisionWith == null; x += check) {
			Tile tile = world.getTile(Math.round(Math.min(collisionBox.getRight() - roundDown, x)), Math.round(bottomFuture));
			if (tile == null || !tile.isSolid()) {
				continue;
			}
			Bounds tileBounds = tile.getCollisionBox();
			if (bottomFuture < tileBounds.getTop()) {
				collisionWith = tileBounds;
			}
		}
		if (collisionWith != null) {
			collidable.setYVelocity(collisionWith.getTop() - collisionBox.getBottom());
			collisionWith = null;
		}
		float topFuture = collisionBox.getTop() + collidable.getYVelocity();
		for (float x = collisionBox.getLeft(); x < collisionBox.getRight() + check && collisionWith == null; x += check) {
			Tile tile = world.getTile(Math.round(Math.min(collisionBox.getRight() - roundDown, x)), Math.round(topFuture));
			if (tile == null || !tile.isSolid()) {
				continue;
			}
			Bounds tileBounds = tile.getCollisionBox();
			if (topFuture > tileBounds.getBottom()) {
				collisionWith = tileBounds;
			}
		}
		if (collisionWith != null) {
			collidable.setYVelocity(collisionWith.getBottom() - collisionBox.getTop());
			collisionWith = null;
		}

		float rightFuture = collisionBox.getRight() + collidable.getXVelocity();
		for (float y = collisionBox.getBottom(); y < collisionBox.getTop() + check && collisionWith == null; y += check) {
			Tile tile = world.getTile(Math.round(rightFuture), Math.round(Math.min(collisionBox.getTop() - roundDown, y)));
			if (tile == null || !tile.isSolid()) {
				continue;
			}
			Bounds tileBounds = tile.getCollisionBox();
			if (rightFuture > tileBounds.getLeft()) {
				collisionWith = tileBounds;
			}
		}
		if (collisionWith != null) {
			collidable.setXVelocity(collisionWith.getLeft() - collisionBox.getRight());
			collisionWith = null;
		}

		float leftFuture = collisionBox.getLeft() + collidable.getXVelocity();
		for (float y = collisionBox.getBottom(); y < collisionBox.getTop() + check && collisionWith == null; y += check) {
			Tile tile = world.getTile(Math.round(leftFuture), Math.round(Math.min(collisionBox.getTop() - roundDown, y)));
			if (tile == null || !tile.isSolid()) {
				continue;
			}
			Bounds tileBounds = tile.getCollisionBox();
			if (leftFuture < tileBounds.getRight()) {
				collisionWith = tileBounds;
			}
		}
		if (collisionWith != null) {
			collidable.setXVelocity(collisionWith.getRight() - collisionBox.getLeft());
			collisionWith = null;
		}
	}

}

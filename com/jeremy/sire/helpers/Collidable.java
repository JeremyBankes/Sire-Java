package com.jeremy.sire.helpers;

import com.jeremy.sire.helpers.tools.Bounds;

public interface Collidable {

	public abstract Bounds getCollisionBox();

	public abstract float getXVelocity();

	public abstract void setXVelocity(float xVelocity);

	public abstract float getYVelocity();

	public abstract void setYVelocity(float yVelocity);

}

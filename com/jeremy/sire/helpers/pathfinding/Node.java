package com.jeremy.sire.helpers.pathfinding;

public interface Node {

	public abstract int getX();

	public abstract int getY();

	public abstract boolean isWalkable();

	public default boolean equals(Node node) {
		return getX() == node.getX() && getY() == node.getY();
	}

}

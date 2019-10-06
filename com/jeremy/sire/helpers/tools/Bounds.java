package com.jeremy.sire.helpers.tools;

public class Bounds {

	private float x, y;
	private float width, height;
	private float top, right, bottom, left;
	private Anchor anchor;

	public Bounds(Anchor anchor, float x, float y, float width, float height) {
		this.anchor = anchor;
		this.x = x;
		this.y = y;
		this.width = width;
		this.height = height;
		updateBounds(true, true);
	}

	public Bounds(Bounds bounds) {
		this(bounds.anchor, bounds.x, bounds.y, bounds.width, bounds.height);
	}

	public float getX() {
		return x;
	}

	public void setX(float x) {
		this.x = x;
		updateBounds(true, false);
	}

	public float getY() {
		return y;
	}

	public void setY(float y) {
		this.y = y;
		updateBounds(false, true);
	}

	public float getWidth() {
		return width;
	}

	public void setWidth(float width) {
		this.width = width;
		updateBounds(true, false);
	}

	public float getHeight() {
		return height;
	}

	public void setHeight(float height) {
		this.height = height;
		updateBounds(false, true);
	}

	public float getCenterX() {
		return getLeft() + getWidth() / 2;
	}

	public float getCenterY() {
		return getTop() - getHeight() / 2;
	}

	private void updateBounds(boolean horizontal, boolean vertical) {
		if (horizontal) {
			Anchor anchor = this.anchor == Anchor.CENTER ? Anchor.NORTH : this.anchor;
			if (anchor == Anchor.NORTH_EAST || anchor == Anchor.EAST || anchor == Anchor.SOUTH_EAST) {
				right = x;
				left = x - width;
			} else if (anchor == Anchor.NORTH_WEST || anchor == Anchor.WEST || anchor == Anchor.SOUTH_WEST) {
				right = x + width;
				left = x;
			} else {
				left = x - width / 2;
				right = x + width / 2;
			}
		}
		if (vertical) {
			Anchor anchor = this.anchor == Anchor.CENTER ? Anchor.EAST : this.anchor;
			if (anchor == Anchor.NORTH_WEST || anchor == Anchor.NORTH || anchor == Anchor.NORTH_EAST) {
				top = y;
				bottom = y - height;
			} else if (anchor == Anchor.SOUTH_WEST || anchor == Anchor.SOUTH || anchor == Anchor.SOUTH_EAST) {
				top = y + height;
				bottom = y;
			} else {
				top = y + height / 2;
				bottom = y - height / 2;
			}
		}
	}

	public float getTop() {
		return top;
	}

	public float getRight() {
		return right;
	}

	public float getBottom() {
		return bottom;
	}

	public float getLeft() {
		return left;
	}

	@Override
	public String toString() {
		return String.format("x: %s y: %s width: %s height: %s", x, y, width, height);
	}

}

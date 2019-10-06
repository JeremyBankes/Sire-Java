package com.jeremy.sire.helpers.world.tile;

import java.awt.Color;
import java.awt.image.BufferedImage;

import com.jeremy.sire.graphics.Renderer;
import com.jeremy.sire.helpers.animation.Animation;
import com.jeremy.sire.helpers.tools.Anchor;
import com.jeremy.sire.helpers.tools.Bounds;

public class Tile {

	public static final int SIZE = 1;

	private final Chunk chunk;

	private final int x, y;
	private final Bounds collisionBox;
	private boolean solid;
	private byte data;

	private Animation animation;
	private BufferedImage texture;
	private Color color;

	public Tile(Chunk chunk, int x, int y, boolean solid) {
		this.chunk = chunk;
		this.x = x;
		this.y = y;
		this.solid = solid;
		collisionBox = new Bounds(Anchor.CENTER, getXInWorld(), getYInWorld(), SIZE, SIZE);
	}

	public void tick() {

	}

	public void render(Renderer renderer) {
		if (getAnimation() != null) {
			renderer.drawImage(getAnimation().getFrame(), //
					collisionBox.getLeft(), //
					collisionBox.getTop(), //
					collisionBox.getRight() - collisionBox.getLeft(), //
					collisionBox.getTop() - collisionBox.getBottom()//
			);
		}
		if (getTexture() != null) {
			renderer.drawImage(getTexture(), //
					collisionBox.getLeft(), //
					collisionBox.getTop(), //
					collisionBox.getRight() - collisionBox.getLeft(), //
					collisionBox.getTop() - collisionBox.getBottom()//
			);
		}
		if (getColor() != null) {
			renderer.setColor(getColor());
			renderer.fillRectangle( //
					collisionBox.getLeft(), //
					collisionBox.getTop(), //
					collisionBox.getRight() - collisionBox.getLeft(), //
					collisionBox.getTop() - collisionBox.getBottom()//
			);
		}
		if (isSolid() && chunk.getWorld().isShowCollisionBoxes()) {
			renderer.setColor(Color.WHITE);
			renderer.traceRectangle( //
					collisionBox.getLeft(), //
					collisionBox.getTop(), //
					collisionBox.getRight() - collisionBox.getLeft(), //
					collisionBox.getTop() - collisionBox.getBottom()//
			);
		}
	}

	public int getXInChunk() {
		return x;
	}

	public int getYInChunk() {
		return y;
	}

	public int getXInWorld() {
		return getChunk().getX() * Chunk.SIZE + getXInChunk();
	}

	public int getYInWorld() {
		return getChunk().getY() * Chunk.SIZE + getYInChunk();
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

	public Chunk getChunk() {
		return chunk;
	}

	public byte getData() {
		return data;
	}

	public void setData(byte data) {
		this.data = data;
	}

	public boolean isSolid() {
		return solid;
	}

	public Bounds getCollisionBox() {
		return collisionBox;
	}

}

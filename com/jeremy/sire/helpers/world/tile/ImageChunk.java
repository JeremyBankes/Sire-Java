package com.jeremy.sire.helpers.world.tile;

import java.awt.image.BufferedImage;

import com.jeremy.sire.graphics.Renderer;
import com.jeremy.sire.helpers.tools.Bounds;

public class ImageChunk extends Chunk {

	private BufferedImage image;

	public ImageChunk(TileWorld world, int x, int y, BufferedImage image) {
		super(world, x, y);
		this.image = image;
	}

	public void setCollision(int[][] collisionMap) {
		for (int i = 0; i < Chunk.SIZE; i++) {
			for (int j = 0; j < Chunk.SIZE; j++) {
				if (collisionMap[i][j] > 0) {
					setTile(new SolidTile(this, j, i));
				}
			}
		}
	}

	@Override
	public void render(Renderer renderer) {
		Bounds bounds = getTile(0, 15).getCollisionBox();
		renderer.drawImage(image, bounds.getLeft(), bounds.getTop(), bounds.getWidth() * Chunk.SIZE, bounds.getHeight() * Chunk.SIZE);
		super.render(renderer);
	}

	private static class SolidTile extends Tile {

		public SolidTile(Chunk chunk, int x, int y) {
			super(chunk, x, y, true);
		}

	}

}

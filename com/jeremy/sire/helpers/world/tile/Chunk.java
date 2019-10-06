package com.jeremy.sire.helpers.world.tile;

import com.jeremy.sire.graphics.Renderer;

public class Chunk {

	public static final int SIZE = 16;

	private Tile[][] tiles;

	private int x, y;
	private LocationKey key;
	private TileWorld world;

	public Chunk(TileWorld world, int x, int y) {
		this.x = x;
		this.y = y;
		this.world = world;
		key = new LocationKey(x, y);
		tiles = new Tile[SIZE][SIZE];
	}

	public void tick() {
		Tile tile;
		for (int i = 0; i < Chunk.SIZE; i++) {
			for (int j = 0; j < Chunk.SIZE; j++) {
				tile = tiles[i][j];
				if (tile == null) {
					continue;
				}
				tile.tick();
			}
		}
	}

	public void render(Renderer renderer) {
		Tile tile;
		for (int i = 0; i < Chunk.SIZE; i++) {
			for (int j = 0; j < Chunk.SIZE; j++) {
				tile = tiles[i][j];
				if (tile == null) {
					continue;
				}
				tile.render(renderer);
			}
		}
	}

	public void setTile(Tile tile) {
		if (tile.getChunk() != this) throw new IllegalStateException("attempted to set tile in chunk that doesn't contain it");
		tiles[tile.getYInChunk()][tile.getXInChunk()] = tile;
	}

	public Tile getTile(int tileX, int tileY) {
		if (tileX < 0 || tileX > Chunk.SIZE - 1 || tileY < 0 || tileY > Chunk.SIZE - 1) throw new IllegalArgumentException("attempted to get out of bounds tile in chunk");
		ensureTileExists(tileX, tileY);
		return tiles[tileY][tileX];
	}

	public LocationKey getKey() {
		return key;
	}

	public int getX() {
		return x;
	}

	public int getY() {
		return y;
	}

	public TileWorld getWorld() {
		return world;
	}

	private void ensureTileExists(int x, int y) {
		if (tiles[y][x] == null) {
			tiles[y][x] = new VoidTile(this, x, y);
		}
	}

}

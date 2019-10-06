package com.jeremy.sire.helpers.world.tile;

import java.util.Collection;
import java.util.HashMap;

import com.jeremy.sire.graphics.Camera;
import com.jeremy.sire.graphics.Renderer;
import com.jeremy.sire.helpers.world.World;

public class TileWorld extends World {

	private final HashMap<LocationKey, Chunk> chunks;

	public TileWorld(Camera camera) {
		super(camera);
		chunks = new HashMap<LocationKey, Chunk>();
	}

	@Override
	public void tick() {
		super.tick();
		getChunks().forEach(chunk -> chunk.tick());
	}

	@Override
	public void render(Renderer renderer) {
		renderer.setCamera(getCamera());
		getChunks().forEach(chunk -> chunk.render(renderer));
		renderDynamicObjects(renderer);
	}

	public void setTile(Tile tile) {
		tile.getChunk().setTile(tile);
	}

	public Tile getTile(int x, int y) {
		int chunkX = Math.floorDiv(x, Chunk.SIZE);
		int chunkY = Math.floorDiv(y, Chunk.SIZE);
		x = x < 0 ? Chunk.SIZE - 1 + x % Chunk.SIZE : x % Chunk.SIZE;
		y = y < 0 ? Chunk.SIZE - 1 + y % Chunk.SIZE : y % Chunk.SIZE;
		return getChunk(chunkX, chunkY).getTile(x, y);
	}

	public Tile getTile(float x, float y) {
		return getTile(Math.round(x), Math.round(y));
	}

	public void setChunk(Chunk chunk) {
		chunks.put(chunk.getKey(), chunk);
	}

	public Chunk getChunk(int x, int y) {
		ensureChunkExists(x, y);
		return chunks.get(new LocationKey(x, y));
	}

	public Collection<Chunk> getChunks() {
		return chunks.values();
	}

	private void ensureChunkExists(int x, int y) {
		if (!chunks.containsKey(new LocationKey(x, y))) {
			setChunk(new Chunk(this, x, y));
		}
	}

}

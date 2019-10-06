package com.jeremy.sire.helpers.inventory.itemStack;

import java.awt.image.BufferedImage;

public abstract class ItemType {

	private BufferedImage texture;
	private int maxStackSize;

	public ItemType(BufferedImage texture, int maxStackSize) {
		this.texture = texture;
		this.maxStackSize = maxStackSize;
	}

	public BufferedImage getTexture() {
		return texture;
	}

	public int getMaxStackSize() {
		return maxStackSize;
	}

}

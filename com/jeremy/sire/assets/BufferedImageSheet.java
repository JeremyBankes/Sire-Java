package com.jeremy.sire.assets;

import java.awt.image.BufferedImage;
import java.util.LinkedList;

public class BufferedImageSheet {

	private int spriteCount;
	private int spriteWidth;
	private int spriteHeight;

	private final LinkedList<BufferedImage> sprites;

	public BufferedImageSheet(BufferedImage image, int spriteWidth, int spriteHeight, int spriteCount) {
		this.spriteWidth = spriteWidth;
		this.spriteHeight = spriteHeight;
		this.spriteCount = spriteCount;

		sprites = new LinkedList<BufferedImage>();
		int spritesHorizontal = image.getWidth() / spriteWidth;
		int spritesVertical = image.getHeight() / spriteHeight;
		for (int i = 0; i < spritesVertical; i++) {
			for (int j = 0; j < spritesHorizontal; j++) {
				sprites.add(image.getSubimage(j * spriteWidth, i * spriteHeight, spriteWidth, spriteHeight));
			}
		}
	}

	public BufferedImageSheet(BufferedImageSheet sheet, int startIndex, int length) {
		spriteWidth = sheet.getSpriteWidth();
		spriteHeight = sheet.getSpriteHeight();
		spriteCount = length;
		sprites = new LinkedList<BufferedImage>(sheet.sprites.subList(startIndex, startIndex + length));
	}

	public BufferedImage getSprite(int index) {
		if (index < 0 || index >= spriteCount) {
			throw new IllegalArgumentException("sprite index out of range " + index);
		}
		return sprites.get(index);
	}

	public void setSprite(int index, BufferedImage sprite) {
		if (index < 0 || index >= spriteCount) {
			throw new IllegalArgumentException("sprite index out of range " + index);
		}
		sprites.set(index, sprite);
	}

	public int getSpriteCount() {
		return spriteCount;
	}

	public int getSpriteWidth() {
		return spriteWidth;
	}

	public int getSpriteHeight() {
		return spriteHeight;
	}

	public LinkedList<BufferedImage> getSprites() {
		return sprites;
	}

}

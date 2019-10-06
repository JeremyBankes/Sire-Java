package com.jeremy.sire.helpers.animation;

import java.awt.image.BufferedImage;

import com.jeremy.sire.assets.BufferedImageSheet;

public class Animation {

	private BufferedImageSheet spriteSheet;

	protected int framePointer;

	private final double frameTime;
	private double lastFrameUpdate;

	public Animation(BufferedImageSheet spriteSheet, int fps) {
		this.spriteSheet = spriteSheet;
		this.frameTime = 1.0 / fps;
		this.lastFrameUpdate = getTime();
	}

	public void tick() {
		double currentTime = getTime();
		if (currentTime - lastFrameUpdate > frameTime) {
			lastFrameUpdate = currentTime;
			incrementFramePointer();
		}
	}

	public int getFramePointer() {
		return framePointer;
	}

	public void setFramePointer(int framePointer) {
		this.framePointer = framePointer % getFrameCount();
	}

	public int getFrameCount() {
		return spriteSheet.getSpriteCount();
	}

	public BufferedImage getFrame() {
		return spriteSheet.getSprite(getFramePointer());
	}

	public BufferedImageSheet getSpriteSheet() {
		return spriteSheet;
	}

	public void setSpriteSheet(BufferedImageSheet spriteSheet) {
		this.spriteSheet = spriteSheet;
	}

	protected void incrementFramePointer() {
		framePointer++;
		if (framePointer >= getFrameCount()) {
			framePointer = 0;
		}
	}

	private double getTime() {
		return (double) System.nanoTime() / 1000000000;
	}

}

package com.jeremy.sire.helpers.animation;

import java.awt.Graphics2D;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;
import java.util.Collection;
import java.util.HashMap;

import com.jeremy.sire.assets.BufferedImageSheet;

public class ComplexAnimation extends Animation {

	private final HashMap<String, AnimationState> animationStates;
	private AnimationState currentAnimationState;

	public ComplexAnimation(BufferedImageSheet spriteSheet, int fps, AnimationState... animationStates) {
		super(spriteSheet, fps);
		this.animationStates = new HashMap<String, ComplexAnimation.AnimationState>();
		for (AnimationState animationState : animationStates) this.animationStates.put(animationState.name, animationState);
		currentAnimationState = animationStates[0];
	}

	@Override
	protected void incrementFramePointer() {
		if (getFramePointer() < currentAnimationState.startFrameIndex || getFramePointer() >= currentAnimationState.endFrameIndex) {
			framePointer = currentAnimationState.startFrameIndex;
		} else {
			super.incrementFramePointer();
		}
	}

	public void setAnimationState(String animationStateName) {
		if (!currentAnimationState.name.equals(animationStateName)) currentAnimationState = animationStates.get(animationStateName);
	}

	public Collection<AnimationState> getAnimationStates() {
		return animationStates.values();
	}

	public AnimationState getCurrentAnimationState() {
		return currentAnimationState;
	}

	private static BufferedImage createTransformed(BufferedImage image, AffineTransform transform) {
		BufferedImage newImage = new BufferedImage(image.getWidth(), image.getHeight(), BufferedImage.TYPE_INT_ARGB);
		Graphics2D g = newImage.createGraphics();
		g.transform(transform);
		g.drawImage(image, 0, 0, null);
		g.dispose();
		return newImage;
	}

	@Override
	public BufferedImage getFrame() {
		BufferedImage frame = super.getFrame();
		if (currentAnimationState.flip) {
			AffineTransform transform = AffineTransform.getScaleInstance(-1, 1);
			transform.translate(-frame.getWidth(), 0);
			return createTransformed(frame, transform);
		}
		return frame;
	}

	public static class AnimationState {

		private String name;
		private int startFrameIndex;
		private int endFrameIndex;
		private boolean flip;

		public AnimationState(String name, int startFrameIndex, int endFrameIndex, boolean flip) {
			this.name = name;
			this.startFrameIndex = startFrameIndex;
			this.endFrameIndex = endFrameIndex;
			this.flip = flip;
		}

		public AnimationState(String name, int startFrameIndex, int endFrameIndex) {
			this(name, startFrameIndex, endFrameIndex, false);
		}

		public int getStartFrameIndex() {
			return startFrameIndex;
		}

		public int getEndFrameIndex() {
			return endFrameIndex;
		}

	}

}

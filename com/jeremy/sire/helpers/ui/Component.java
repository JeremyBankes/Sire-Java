package com.jeremy.sire.helpers.ui;

import java.awt.Color;
import java.awt.image.BufferedImage;

import com.jeremy.sire.Game;
import com.jeremy.sire.graphics.Renderer;
import com.jeremy.sire.helpers.animation.Animation;
import com.jeremy.sire.helpers.tools.Anchor;
import com.jeremy.sire.helpers.tools.Bounds;
import com.jeremy.sire.input.KeyInput.KeyInputEvent;
import com.jeremy.sire.input.MouseInput.MouseInputEvent;

public class Component {

	private Game game;

	private Bounds bounds;
	private Animation animation;
	private BufferedImage texture;
	private Color foreground, background;

	private boolean hovering;
	private boolean focusable;

	public Component(Game game, Anchor anchor, float x, float y, float width, float height) {
		bounds = new Bounds(anchor, x, y, width, height);
		this.game = game;
		foreground = Color.WHITE;
		background = Color.DARK_GRAY;
	}

	public void keyEvent(KeyInputEvent event) {
	}

	public void mouseEvent(MouseInputEvent event) {
		float mx = (float) event.x / game.getRenderer().getWidth();
		float my = (float) event.y / game.getRenderer().getHeight();
		hovering = bounds.getLeft() <= mx && bounds.getRight() >= mx && bounds.getTop() >= my && bounds.getBottom() <= my;
		if (isHovering()) mouseEvent(event);
	}

	public void tick() {
	}

	public void render(Renderer renderer) {
		renderBackground(renderer);
		renderContent(renderer);
	}

	protected void renderContent(Renderer renderer) {
		renderer.setColor(foreground);
	}

	protected void renderBackground(Renderer renderer) {
		if (getAnimation() != null) {
			renderer.drawImage(getAnimation().getFrame(), //
					bounds.getLeft(), //
					bounds.getTop(), //
					bounds.getRight() - bounds.getLeft(), //
					bounds.getTop() - bounds.getBottom()//
			);
		}
		if (getTexture() != null) {
			renderer.drawImage(getTexture(), //
					bounds.getLeft(), //
					bounds.getTop(), //
					bounds.getRight() - bounds.getLeft(), //
					bounds.getTop() - bounds.getBottom()//
			);
		}
		if (getColor() != null) {
			renderer.setColor(getColor());
			renderer.fillRectangle( //
					bounds.getLeft(), //
					bounds.getTop(), //
					bounds.getRight() - bounds.getLeft(), //
					bounds.getTop() - bounds.getBottom()//
			);
		}
	}

	public Bounds getBounds() {
		return bounds;
	}

	public void setBounds(Bounds bounds) {
		this.bounds = bounds;
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
		return background;
	}

	public void setColor(Color color) {
		this.background = color;
	}

	public boolean isHovering() {
		return hovering;
	}

	public boolean isFocusable() {
		return focusable;
	}

	public void setFocusable(boolean focusable) {
		this.focusable = focusable;
	}

}

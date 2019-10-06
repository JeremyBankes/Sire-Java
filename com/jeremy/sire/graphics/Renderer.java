package com.jeremy.sire.graphics;

import java.awt.Canvas;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Rectangle;
import java.awt.image.BufferStrategy;
import java.util.HashMap;

import com.jeremy.sire.helpers.tools.Anchor;

public class Renderer {

	private int buffers = 2;

	private final float aspectRatio;

	private Canvas canvas;
	private BufferStrategy strategy;
	private Graphics2D graphics;

	private Camera camera;

	private final HashMap<FontKey, Font> fonts;

	public Renderer(int width, int height) {
		canvas = new Canvas();
		canvas.setPreferredSize(new Dimension(width, height));
		aspectRatio = (float) width / height;
		fonts = new HashMap<FontKey, Font>();
	}

	public Font getFont() {
		return graphics.getFont();
	}

	public void setColor(Color color) {
		graphics.setColor(color);
	}

	public void setFont(Font font) {
		graphics.setFont(font);
	}

	public void setFont(Font font, int height) {
		setFont(getFontOfHeight(font, height));
	}

	public void setFont(Font font, float height) {
		setFont(font, Math.round(getHeight() * height));
	}

	public void setFontHeight(int height) {
		setFont(getFont(), height);
	}

	public void setFontHeight(float height) {
		setFont(getFont(), height);
	}

	public Font getFontOfHeight(Font font, int height) {
		FontKey key = new FontKey(font, height);
		if (fonts.containsKey(key)) {
			return fonts.get(key);
		}
		float size = 0;
		font = font.deriveFont(size);
		while (graphics.getFontMetrics(font).getHeight() < height) {
			font = font.deriveFont(size++);
		}
		fonts.put(key, font);
		return font;
	}

	public Font getFontOfHeight(Font font, float heightAsScreenPercent) {
		return getFontOfHeight(font, Math.round(getHeight() * heightAsScreenPercent));
	}

	public int getTextHeight() {
		return graphics.getFontMetrics().getHeight();
	}

	public float getTextHeightAsScreenPercentage() {
		return (float) getTextHeight() / getHeight();
	}

	public int getTextWidth(String text) {
		return graphics.getFontMetrics().stringWidth(text);
	}

	public float getTextWidthAsScreenPercentage(String text) {
		return (float) getTextWidth(text) / getWidth();
	}

	public void fillRectangle(int x, int y, int width, int height, Anchor anchor) {
		graphics.fill(applyAnchor(x, y, width, height, anchor));
	}

	public void fillRectangle(int x, int y, int width, int height) {
		fillRectangle(x, y, width, height, Anchor.NORTH_WEST);
	}

	public void fillRectangle(float x, float y, float width, float height, Anchor anchor) {
		fillRectangle(camera.transformX(x), camera.transformY(y), camera.transformWidth(width), camera.transformHeight(height), anchor);
	}

	public void fillRectangle(float x, float y, float width, float height) {
		fillRectangle(x, y, width, height, Anchor.NORTH_WEST);
	}

	public void traceRectangle(int x, int y, int width, int height, Anchor anchor) {
		graphics.draw(applyAnchor(x, y, width, height, anchor));
	}

	public void traceRectangle(int x, int y, int width, int height) {
		traceRectangle(x, y, width, height, Anchor.NORTH_WEST);
	}

	public void traceRectangle(float x, float y, float width, float height, Anchor anchor) {
		traceRectangle(camera.transformX(x), camera.transformY(y), camera.transformWidth(width), camera.transformHeight(height), anchor);
	}

	public void traceRectangle(float x, float y, float width, float height) {
		traceRectangle(x, y, width, height, Anchor.NORTH_WEST);
	}

	public void drawImage(Image image, int x, int y, int width, int height, Anchor anchor) {
		Rectangle bounds = applyAnchor(x, y, width, height, anchor);
		graphics.drawImage(image, bounds.x, bounds.y, bounds.width, bounds.height, null);
	}

	public void drawImage(Image image, float x, float y, float width, float height, Anchor anchor) {
		drawImage(image, camera.transformX(x), camera.transformY(y), camera.transformWidth(width), camera.transformHeight(height), anchor);
	}

	public void drawImage(Image image, float x, float y, float width, float height) {
		drawImage(image, x, y, width, height, Anchor.NORTH_WEST);
	}

	public void drawText(int x, int y, String text, Anchor anchor) {
		Rectangle bounds = applyAnchor(x, y, getTextWidth(text), getTextHeight(), anchor);
		graphics.drawString(text, bounds.x, bounds.y + bounds.height);
	}

	public void drawText(String text, float x, float y, Anchor anchor) {
		drawText(camera.transformX(x), camera.transformY(y), text, anchor);
	}

	public void drawText(String text, float x, float y) {
		drawText(text, x, y, Anchor.SOUTH_WEST);
	}

	public void beginRender() {
		while ((strategy = canvas.getBufferStrategy()) == null)
			canvas.createBufferStrategy(buffers);
		graphics = (Graphics2D) strategy.getDrawGraphics();
	}

	public void endRender() {
		graphics.dispose();
		strategy.show();
	}

	public Canvas getCanvas() {
		return canvas;
	}

	public int getBuffers() {
		return buffers;
	}

	public void setBuffers(int buffers) {
		this.buffers = buffers;
	}

	public int getWidth() {
		return canvas.getWidth();
	}

	public int getHeight() {
		return canvas.getHeight();
	}

	public float getAspectRatio() {
		return aspectRatio;
	}

	public Camera getCamera() {
		return camera;
	}

	public void setCamera(Camera camera) {
		this.camera = camera;
	}

	private Rectangle applyAnchor(int x, int y, int width, int height, Anchor anchor) {
		Rectangle bounds = null;
		if (anchor == Anchor.CENTER) {
			bounds = new Rectangle(x - width / 2, y - height / 2, width, height);
		} else if (anchor == Anchor.NORTH) {
			bounds = new Rectangle(x - width / 2, y, width, height);
		} else if (anchor == Anchor.NORTH_EAST) {
			bounds = new Rectangle(x - width, y, width, height);
		} else if (anchor == Anchor.EAST) {
			bounds = new Rectangle(x - width, y - height / 2, width, height);
		} else if (anchor == Anchor.SOUTH_EAST) {
			bounds = new Rectangle(x - width, y - height, width, height);
		} else if (anchor == Anchor.SOUTH) {
			bounds = new Rectangle(x - width / 2, y - height, width, height);
		} else if (anchor == Anchor.SOUTH_WEST) {
			bounds = new Rectangle(x, y - height, width, height);
		} else if (anchor == Anchor.WEST) {
			bounds = new Rectangle(x, y - height / 2, width, height);
		} else if (anchor == Anchor.NORTH_WEST) {
			bounds = new Rectangle(x, y, width, height);
		}
		return bounds;
	}

	private class FontKey {

		private Font font;
		private float height;

		public FontKey(Font font, float height) {
			this.font = font;
			this.height = height;
		}

		@Override
		public boolean equals(Object obj) {
			if (!(obj instanceof FontKey))
				return false;
			FontKey key = (FontKey) obj;
			return key.font == font && key.height == height;
		}

		@Override
		public int hashCode() {
			return font.hashCode() * Float.hashCode(height);
		}

	}

}

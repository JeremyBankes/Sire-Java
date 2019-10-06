package com.jeremy.sire.helpers.ui.chat;

import java.awt.Color;
import java.util.Date;

import com.jeremy.sire.Game;
import com.jeremy.sire.graphics.Renderer;
import com.jeremy.sire.helpers.tools.Anchor;
import com.jeremy.sire.helpers.ui.chat.ChatInput.ChatInputCallback;
import com.jeremy.sire.input.MouseInput.MouseAction;

public class Chat {

	private Game game;

	private ChatHistory history;
	private ChatInput input;

	private Color backgroundColor = new Color(0f, 0f, 0f, 0.5f);
	private Color accentColor = new Color(0x66666666);

	private int lines = 20;
	private int scroll = 0;
	private float fontSize = 0.025f;

	private float marginX = 0.01f;
	private float marginY = 0.01f * 16 / 9 + fontSize * 2;

	private float paddingX = 0.01f;
	private float paddingY = 0.01f * 16 / 9;

	private boolean active;
	private boolean visible;

	public Chat(Game game) {
		this.game = game;
		history = new ChatHistory();
		input = new ChatInput(game);

		game.getMouseInput().addMouseCallback(event -> {
			if (event.action == MouseAction.SCROLL) {
				scroll = Math.min(Math.max(0, history.size() - lines), Math.max(0, scroll - event.y));
			}
		});
	}

	public void setInputCallback(ChatInputCallback callback) {
		input.setCallback(callback);
	}

	public ChatInputCallback getInputCallback() {
		return input.getCallback();
	}

	public void sendMessage(CommandSender sender, String message) {
		history.addFirst(new ChatHistoryEntry(new Date(), sender, message));
	}

	public void sendMessage(String message) {
		sendMessage(game.getConsoleSender(), message);
	}

	public void tick() {
		input.tick();
	}

	public void render(Renderer renderer) {
		if (!visible) return;
		renderer.setFontHeight(fontSize);

		float width = Math.max(getLongestLineLength(renderer), 0.5f) + paddingX * 2;
		float height = Math.min(history.size(), lines) * renderer.getTextHeightAsScreenPercentage() + paddingY * 2;
		float x = marginX;
		float y = 1f - height - marginY;

		renderBackground(renderer, x, y, width, height);

		x += paddingX;
		y = 1f - marginY - paddingY;

		renderHistory(renderer, x, y, width, height);

		renderInput(renderer, x, y, width, height);
	}

	protected void renderBackground(Renderer renderer, float x, float y, float width, float height) {
		if (!history.isEmpty()) {
			renderer.setColor(backgroundColor);
			renderer.fillRectangle(x, y, width, height);
		}

		if (history.size() > lines) {
			renderer.setColor(accentColor);
			float scrollPercent = (float) scroll / (history.size() - lines);
			renderer.fillRectangle(x, y + height - (height * 7 / 8) * scrollPercent - height / 8, 0.0025f, height / 8);
		}
	}

	protected void renderHistory(Renderer renderer, float x, float y, float width, float height) {
		for (int i = 0, len = Math.min(lines, history.size()); i < len; i++) {
			ChatHistoryEntry entry = history.get(i + scroll);
			ChatComponent[] components = getChatComponents(Format.translateColorCodes(entry.message));
			renderChatComponents(renderer, x, y - renderer.getTextHeightAsScreenPercentage() * i, components);
		}
	}

	protected void renderInput(Renderer renderer, float x, float y, float width, float height) {
		input.render(renderer, x, y, width, height);
	}

	public void clearInput() {
		input.clear();
	}

	private float getLongestLineLength(Renderer renderer) {
		float max = 0;
		for (int i = 0, len = Math.min(history.size(), lines); i < len; i++) {
			float width = renderer.getTextWidthAsScreenPercentage(Format.stripColor(history.get(i).message));
			if (width > max) max = width;
		}
		return max;
	}

	public boolean isVisible() {
		return visible;
	}

	public void setVisible(boolean visible) {
		this.visible = visible;
	}

	public boolean isActive() {
		return active;
	}

	public void setActive(boolean active) {
		input.setActive(this.active = active);
	}

	public Color getBackgroundColor() {
		return backgroundColor;
	}

	public void setBackgroundColor(Color backgroundColor) {
		this.backgroundColor = backgroundColor;
	}

	public float getMarginX() {
		return marginX;
	}

	public void setMarginX(float marginX) {
		this.marginX = marginX;
	}

	public float getMarginY() {
		return marginY;
	}

	public void setMarginY(float marginY) {
		this.marginY = marginY;
	}

	public float getPaddingX() {
		return paddingX;
	}

	public void setPaddingX(float paddingX) {
		this.paddingX = paddingX;
	}

	public float getPaddingY() {
		return paddingY;
	}

	public void setPaddingY(float paddingY) {
		this.paddingY = paddingY;
	}

	public int getScroll() {
		return scroll;
	}

	public void setScroll(int scroll) {
		this.scroll = scroll;
	}

	Game getGame() {
		return game;
	}

	public static ChatComponent[] getChatComponents(String colorCodedString) {
		String[] pieces = colorCodedString.split("(?<!\\\\|^)(?=§[A-z0-9])");
		ChatComponent[] components = new ChatComponent[pieces.length];
		for (int i = 0; i < pieces.length; i++) {
			String piece = pieces[i];
			Color color = ChatColor.getColor(piece.charAt(1));
			piece = piece.substring(2);
			components[i] = new ChatComponent(color, piece);
		}
		return components;
	}

	private static void renderChatComponents(Renderer renderer, float x, float y, ChatComponent... components) {
		for (ChatComponent component : components) {
			renderer.setColor(component.getColor());
			renderer.drawText(component.getText(), x, y, Anchor.SOUTH_WEST);
			x += renderer.getTextWidthAsScreenPercentage(component.getText());
		}
	}

}

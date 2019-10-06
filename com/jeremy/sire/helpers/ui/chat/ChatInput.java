package com.jeremy.sire.helpers.ui.chat;

import static com.jeremy.sire.input.KeyInput.*;
import static com.jeremy.sire.input.KeyInput.KeyAction.*;

import java.awt.Color;
import java.awt.HeadlessException;
import java.awt.Toolkit;
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.UnsupportedFlavorException;
import java.io.IOException;

import com.jeremy.sire.Game;
import com.jeremy.sire.graphics.Renderer;
import com.jeremy.sire.helpers.tools.Anchor;
import com.jeremy.sire.input.KeyInput;

public class ChatInput {

	private int cursor;
	private StringBuffer input;

	private ChatInputCallback callback;

	private Color backgroundColor = new Color(0f, 0f, 0f, 0.5f);
	private Color textColor = new Color(0xFFFFFF);
	private Color accentColor = new Color(0xFFFFFF);

	private boolean active;

	public ChatInput(Game game) {
		input = new StringBuffer();

		final KeyInput keyInput = game.getKeyInput();
		keyInput.addKeyEventCallback(event -> {
			if (!active) return;
			if (event.action == PRESS) {
				int last = input.length();
				boolean ctrl = event.modifiers.contains("ctrl");
				if (event.asciiCode == KEY_BACK_SPACE) {
					if (ctrl) {
						int start = getTravelIndex(false);
						input.delete(start, cursor);
						cursor = start;
					} else {
						if (cursor > 0) input.deleteCharAt(--cursor);
					}
				} else if (event.asciiCode == KEY_DELETE) {
					if (ctrl) {
						int end = getTravelIndex(true);
						input.delete(cursor, end);
					} else {
						if (cursor < last) input.deleteCharAt(++cursor);
					}
				} else if (event.asciiCode == KEY_RIGHT) {
					if (ctrl) {
						cursor = getTravelIndex(true);
					} else {
						if (cursor < last) cursor++;
					}
				} else if (event.asciiCode == KEY_LEFT) {
					if (ctrl) {
						cursor = getTravelIndex(false);
					} else {
						if (cursor > 0) cursor--;
					}
				} else if (event.asciiCode == KEY_V && ctrl) {
					try {
						String paste = Toolkit.getDefaultToolkit().getSystemClipboard().getData(DataFlavor.stringFlavor).toString();
						input.append(paste);
						cursor += paste.length();
					} catch (HeadlessException | UnsupportedFlavorException | IOException exception) {
						exception.printStackTrace();
					}
				} else if (event.asciiCode == KEY_ENTER) {
					if (callback != null) callback.onInput(input.toString());
					input.setLength(0);
					event.consume();
				}
			} else if (event.action == TYPE) {
				char character = (char) event.asciiCode;
				if (game.getRenderer().getFont().canDisplay(character)) {
					input.insert(cursor, character);
					cursor++;
				}
			}
			cursor = Math.min(input.length(), cursor);
		});
	}

	public void tick() {}

	public void render(Renderer renderer, float x, float y, float width, float height) {
		if (!active) return;
		float textHeight = renderer.getTextHeightAsScreenPercentage();

		renderer.setColor(backgroundColor);
		renderer.fillRectangle(x, y, width, height);

		renderer.setColor(textColor);
		renderer.drawText(input.toString(), x, y, Anchor.SOUTH_WEST);

		renderer.setColor(accentColor);
		float xOffset = renderer.getTextWidthAsScreenPercentage(input.substring(0, cursor));
		renderer.fillRectangle(x + xOffset, y - textHeight, 0.001f, textHeight);
	}

	public boolean isActive() {
		return active;
	}

	public void setActive(boolean active) {
		this.active = active;
	}

	public void setCallback(ChatInputCallback callback) {
		this.callback = callback;
	}

	public ChatInputCallback getCallback() {
		return callback;
	}

	private int getTravelIndex(boolean forward) {
		final String[] classifications = { "[A-z]", "[0-9]", " ", "[^A-z0-9 ]*" };

		if (forward) {
			int last = input.length();
			if (cursor == last) return last;
			int firstIndex = cursor;
			while (firstIndex < last && input.charAt(firstIndex) == ' ') firstIndex++;
			String firstChararacter = String.valueOf(input.charAt(firstIndex));
			int classification = 0;
			for (int i = 0; i < classifications.length; i++) {
				if (firstChararacter.matches(classifications[i])) {
					classification = i;
					break;
				}
			}
			int pointer = firstIndex;
			while (pointer < last) {
				if (!String.valueOf(input.charAt(pointer)).matches(classifications[classification])) {
					break;
				}
				pointer++;
			}
			return pointer;
		} else {
			if (cursor == 0) return 0;
			int firstIndex = cursor - 1;
			while (firstIndex > 0 && input.charAt(firstIndex) == ' ') firstIndex--;
			String firstChararacter = String.valueOf(input.charAt(firstIndex));
			int classification = 0;
			for (int i = 0; i < classifications.length; i++) {
				if (firstChararacter.matches(classifications[i])) {
					classification = i;
					break;
				}
			}
			int pointer = firstIndex;
			while (pointer > 0) {
				if (!String.valueOf(input.charAt(pointer)).matches(classifications[classification])) {
					pointer++;
					break;
				}
				pointer--;
			}
			return pointer;
		}
	}

	public void clear() {
		input.setLength(0);
		cursor = 0;
	}

	public static interface ChatInputCallback {

		public abstract void onInput(String input);

	}

}

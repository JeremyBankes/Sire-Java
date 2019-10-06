package com.jeremy.sire.input;

import java.util.ArrayList;

public class Keyboard {

	private int doubleTapThreshold = 250;
	private ArrayList<KeyEventCallback> keyEventCallbacks;

	public Keyboard() {
		keyEventCallbacks = new ArrayList<>();
	}

	public void addKeyEventCallback(KeyEventCallback callback) {
		keyEventCallbacks.add(callback);
	}

	public void fireKeyEvent(KeyEvent event) {
		for (KeyEventCallback callback : keyEventCallbacks) {
			if (event.isCancelled()) {
				return;
			}
			callback.invoke(event);
		}
	}

	public int getDoubleTapThreshold() {
		return doubleTapThreshold;
	}

	public void setDoubleTapThreshold(int doubleTapThreshold) {
		this.doubleTapThreshold = doubleTapThreshold;
	}

	public static enum KeyEventType {

		PRESS, RELEASE, TYPE, DOUBLE_TAP

	}

	public static class KeyEvent {

		private final int keyCode;
		private final KeyEventType type;
		private boolean cancelled;

		public KeyEvent(int keyCode, KeyEventType type) {
			this.keyCode = keyCode;
			this.type = type;
		}

		public int getKeyCode() {
			return keyCode;
		}

		public KeyEventType getType() {
			return type;
		}

		public boolean isCancelled() {
			return cancelled;
		}

		public void cancel() {
			cancelled = true;
		}

		@Override
		public String toString() {
			return String.format("%s %s [%s]", keyCode, type, cancelled ? "Cancelled" : "Not Cancelled");
		}

	}

	public static interface KeyEventCallback {

		public abstract void invoke(KeyEvent event);
	}

}

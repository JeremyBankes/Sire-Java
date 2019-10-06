package com.jeremy.sire.input;

import java.util.ArrayList;

public class Mouse {

	private int doubleClickThreshold = 250;
	private ArrayList<MouseEventCallback> mouseEventCallbacks;

	public Mouse() {
		mouseEventCallbacks = new ArrayList<>();
	}

	public void addMouseEventCallback(MouseEventCallback callback) {
		mouseEventCallbacks.add(callback);
	}

	public void fireMouseEvent(MouseEvent event) {
		for (MouseEventCallback callback : mouseEventCallbacks) {
			if (event.isCancelled()) {
				return;
			}
			callback.invoke(event);
		}
	}

	public int getDoubleClickThreshold() {
		return doubleClickThreshold;
	}

	public void setDoubleClickThreshold(int doubleClickThreshold) {
		this.doubleClickThreshold = doubleClickThreshold;
	}

	public static enum MouseEventType {

		MOVE, PRESS, RELEASE, DOUBLE_CLICK, WHEEL

	}

	public static class MouseEvent {

		private final int button;
		private final int x, y;
		private final MouseEventType type;
		private boolean cancelled;

		public MouseEvent(int button, int x, int y, MouseEventType type) {
			this.button = button;
			this.x = x;
			this.y = y;
			this.type = type;
		}

		public int getButton() {
			return button;
		}

		public int getX() {
			return x;
		}

		public int getY() {
			return y;
		}

		public MouseEventType getType() {
			return type;
		}

		public boolean isCancelled() {
			return cancelled;
		}

		public boolean isOfType(MouseEventType type) {
			return getType() == type;
		}

		public void cancel() {
			cancelled = true;
		}

		@Override
		public String toString() {
			return String.format("%s %s %s %s [%s]", button, x, y, type, cancelled ? "Cancelled" : "Not Cancelled");
		}

	}

	public static interface MouseEventCallback {

		public abstract void invoke(MouseEvent event);
	}
}

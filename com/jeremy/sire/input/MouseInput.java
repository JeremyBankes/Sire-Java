package com.jeremy.sire.input;

import java.awt.Canvas;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.event.MouseWheelEvent;
import java.awt.event.MouseWheelListener;
import java.util.AbstractMap.SimpleEntry;
import java.util.HashSet;

import com.jeremy.sire.graphics.GameWindow;

public class MouseInput {

	public static final int DOUBLE_CLICK_THRESHOLD = 300;

	private HashSet<MouseEventCallback> mouseCallbacks;
	private HashSet<Integer> pressed;

	private SimpleEntry<MouseAction, MouseInputEvent> doubleClick;

	private int x, y;
	int xOffset, yOffset;

	public MouseInput(GameWindow window) {
		mouseCallbacks = new HashSet<>();
		pressed = new HashSet<>();

		Canvas canvas = window.getRenderer().getCanvas();

		canvas.addMouseMotionListener(new MouseMotionListener() {
			@Override
			public void mouseMoved(MouseEvent event) {
				x = event.getX() - xOffset;
				y = event.getY() - yOffset;
				mouseCallbacks.forEach(callback -> callback.invoke(new MouseInputEvent(MouseAction.MOVE, x, y, event.getButton())));
			}

			@Override
			public void mouseDragged(MouseEvent event) {
				mouseMoved(event);
			}
		});

		canvas.addMouseListener(new MouseListener() {

			@Override
			public void mousePressed(MouseEvent event) {
				MouseInputEvent newEvent = new MouseInputEvent(MouseAction.PRESS, event.getX() - xOffset, event.getY() - yOffset, event.getButton());
				MouseInputEvent doubleClickEvent = null;
				if (doubleClick != null && System.currentTimeMillis() - doubleClick.getValue().time < DOUBLE_CLICK_THRESHOLD) {
					if (doubleClick.getValue().x == newEvent.x && doubleClick.getValue().y == newEvent.y) {
						doubleClickEvent = new MouseInputEvent(MouseAction.DOUBLE_CLICK, event.getX() - xOffset, event.getY() - yOffset, event.getButton());
					}
				}
				doubleClick = new SimpleEntry<MouseAction, MouseInputEvent>(MouseAction.PRESS, newEvent);
				for (MouseEventCallback callback : mouseCallbacks) {
					callback.invoke(newEvent);
					if (doubleClickEvent != null) callback.invoke(doubleClickEvent);
				}
				pressed.add(event.getButton());
			}

			@Override
			public void mouseReleased(MouseEvent event) {
				MouseInputEvent newEvent = new MouseInputEvent(MouseAction.RELEASE, event.getX() - xOffset, event.getY() - yOffset, event.getButton());
				mouseCallbacks.forEach(callback -> callback.invoke(newEvent));
				pressed.remove(event.getButton());
			}

			@Override
			public void mouseExited(MouseEvent event) {
				MouseInputEvent newEvent = new MouseInputEvent(MouseAction.EXIT, event.getX() - xOffset, event.getY() - yOffset, event.getButton());
				mouseCallbacks.forEach(callback -> callback.invoke(newEvent));
			}

			@Override
			public void mouseEntered(MouseEvent event) {
				MouseInputEvent newEvent = new MouseInputEvent(MouseAction.ENTER, event.getX() - xOffset, event.getY() - yOffset, event.getButton());
				mouseCallbacks.forEach(callback -> callback.invoke(newEvent));
			}

			@Override
			public void mouseClicked(MouseEvent event) {
				MouseInputEvent newEvent = new MouseInputEvent(MouseAction.CLICK, event.getX() - xOffset, event.getY() - yOffset, event.getButton());
				mouseCallbacks.forEach(callback -> callback.invoke(newEvent));
			}
		});

		canvas.addMouseWheelListener(new MouseWheelListener() {
			@Override
			public void mouseWheelMoved(MouseWheelEvent event) {
				mouseCallbacks.forEach(callback -> callback.invoke(new MouseInputEvent(MouseAction.SCROLL, 0, event.getWheelRotation(), event.getButton())));
			}
		});
	}

	public void addMouseCallback(MouseEventCallback callback) {
		mouseCallbacks.add(callback);
	}

	public void removeMouseCallback(MouseEventCallback callback) {
		mouseCallbacks.remove(callback);
	}

	public boolean isPressed(int button) {
		return pressed.contains(button);
	}

	public boolean isAnyPressed(int... button) {
		for (int asciiCode : button) {
			if (pressed.contains(asciiCode)) {
				return true;
			}
		}
		return false;
	}

	public boolean isAllPressed(int... button) {
		for (int asciiCode : button) {
			if (!pressed.contains(asciiCode)) {
				return false;
			}
		}
		return true;
	}

	public int getX() {
		return x;
	}

	public int getY() {
		return y;
	}

	public float getXPercent(int width) {
		return (float) getX() / width;
	}

	public float getYPercent(int height) {
		return (float) getY() / height;
	}

	public static enum MouseAction {
		MOVE, PRESS, CLICK, DOUBLE_CLICK, RELEASE, SCROLL, ENTER, EXIT
	}

	public static interface MouseEventCallback {

		public abstract void invoke(MouseInputEvent event);

	}

	public static class MouseInputEvent {

		public final MouseAction action;
		public final int x, y;
		public final int button;
		public final long time;

		private boolean consumed;

		public MouseInputEvent(MouseAction action, int x, int y, int button) {
			this.action = action;
			this.x = x;
			this.y = y;
			this.button = button;
			this.time = System.currentTimeMillis();
		}

		public boolean isConsumed() {
			return consumed;
		}

		public void consume() {
			consumed = true;
		}

		@Override
		public String toString() {
			return action + ", " + x + ", " + y + (consumed ? " consumed" : "");
		}

	}

	public static final int MOUSE_LEFT = 1, MOUSE_MIDDLE = 2, MOUSE_RIGHT = 3;

}

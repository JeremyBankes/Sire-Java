package com.jeremy.sire.helpers;

import java.util.HashMap;

import com.jeremy.sire.input.KeyInput;

public class Controls {

	private KeyInput keyInput;

	private final HashMap<String, Integer> controls;

	public Controls(KeyInput keyInput) {
		this.controls = new HashMap<String, Integer>();
		this.keyInput = keyInput;

		controls.put("move-up", KeyInput.KEY_W);
		controls.put("move-down", KeyInput.KEY_S);
		controls.put("move-right", KeyInput.KEY_D);
		controls.put("move-left", KeyInput.KEY_A);
		controls.put("sprint", KeyInput.KEY_G);
		controls.put("crouch", KeyInput.KEY_SHIFT);
	}

	public void on(String control, KeyInput.KeyAction action, KeyInput.KeyEventCallback callback) {
		keyInput.addKeyEventCallback(event -> {
			if (event.asciiCode == controls.get(control) && event.action == action) {
				callback.invoke(event);
				event.consume();
			}
		});
	}

	public boolean isMoveUp() {
		return keyInput.isPressed(controls.get("move-up"));
	}

	public boolean isMoveDown() {
		return keyInput.isPressed(controls.get("move-down"));
	}

	public boolean isMoveRight() {
		return keyInput.isPressed(controls.get("move-right"));
	}

	public boolean isMoveLeft() {
		return keyInput.isPressed(controls.get("move-left"));
	}

	public boolean isMovementDown() {
		return isMoveUp() || isMoveRight() || isMoveDown() || isMoveLeft();
	}

	public boolean isMovementKey(int asciiCode) {
		return controls.get("move-up") == asciiCode || controls.get("move-right") == asciiCode || controls.get("move-down") == asciiCode
				|| controls.get("move-left") == asciiCode;
	}

	public boolean isSprint() {
		return keyInput.isPressed(controls.get("sprint"));
	}

	public boolean isCrouch() {
		return keyInput.isPressed(controls.get("crouch"));
	}

}

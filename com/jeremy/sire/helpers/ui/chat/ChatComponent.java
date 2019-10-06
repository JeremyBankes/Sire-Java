package com.jeremy.sire.helpers.ui.chat;

import java.awt.Color;

public class ChatComponent {

	private Color color;
	private String text;

	public ChatComponent(Color color, String text) {
		this.color = color;
		this.text = text;
	}

	public Color getColor() {
		return color;
	}

	public void setColor(Color color) {
		this.color = color;
	}

	public String getText() {
		return text;
	}

	public void setText(String text) {
		this.text = text;
	}

}

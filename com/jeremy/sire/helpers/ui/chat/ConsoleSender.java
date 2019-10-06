package com.jeremy.sire.helpers.ui.chat;

public class ConsoleSender implements CommandSender {

	@Override
	public boolean hasPermission(Permission permission) {
		return true;
	}

	@Override
	public void sendMessage(String message) {
		System.out.println(Format.stripColor(message));
	}

}

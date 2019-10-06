package com.jeremy.sire.helpers.ui.chat;

public interface CommandSender {

	public boolean hasPermission(Permission permission);

	public default boolean hasPermission(String permission) {
		return hasPermission(new Permission(permission));
	}

	public void sendMessage(String message);

	public default void sendMessage(String message, Object... arguments) {
		sendMessage(Format.substitute(message, arguments));
	}

}

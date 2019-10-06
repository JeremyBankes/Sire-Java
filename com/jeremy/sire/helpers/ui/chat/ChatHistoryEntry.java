package com.jeremy.sire.helpers.ui.chat;

import java.util.Date;

public class ChatHistoryEntry {

	public final Date date;
	public final CommandSender sender;
	public final String message;

	public ChatHistoryEntry(Date date, CommandSender sender, String message) {
		this.date = date;
		this.sender = sender;
		this.message = message;
	}

}

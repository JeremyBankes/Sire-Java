package com.jeremy.sire.helpers.ui.chat;

public class Format {

	public static String substitute(String template, Object... arguments) {
		for (int i = 0; i < arguments.length; i++) template = template.replace(String.format("{%i}", i), arguments[i].toString());
		return template;
	}

	public static String stripColor(String coloredString) {
		return coloredString.replaceAll("(?<!\\\\)[&§][A-z0-9]", "");
	}

	public static String translateColorCodes(String colorCodedString) {
		colorCodedString = colorCodedString.replaceAll("(?<!\\\\)&(?=[A-z0-9])", "§");
		if (!colorCodedString.matches("^§[A-z0-9].*")) colorCodedString = "§a" + colorCodedString;
		return colorCodedString;
	}

}

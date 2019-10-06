package com.jeremy.sire.helpers.ui.chat;

import java.awt.Color;
import java.util.HashMap;

public class ChatColor {

	public static final HashMap<Character, Color> COLORS = new HashMap<Character, Color>();

	static {
		COLORS.put('0', new Color(0x000000));
		COLORS.put('1', new Color(0xb43232));
		COLORS.put('2', new Color(0x3232b4));
		COLORS.put('3', new Color(0xffff50));
		COLORS.put('4', new Color(0x8c00b4));
		COLORS.put('5', new Color(0x32b432));
		COLORS.put('6', new Color(0xfab400));
		COLORS.put('7', new Color(0xc800b4));
		COLORS.put('8', new Color(0x00c8b4));
		COLORS.put('9', new Color(0xc8b400));
		COLORS.put('a', new Color(0xffffff));
		COLORS.put('b', new Color(0xb4b4b4));
		COLORS.put('c', new Color(0x646464));
		COLORS.put('d', new Color(0x323232));
		COLORS.put('e', new Color(0x643c00));
		COLORS.put('f', new Color(0xfa64c8));
	}

	public static final String BLACK = "§0";
	public static final String RED = "§1";
	public static final String BLUE = "§2";
	public static final String YELLOW = "§3";
	public static final String PURPLE = "§4";
	public static final String GREEN = "§5";
	public static final String ORANGE = "§6";
	public static final String MAGENTA = "§7";
	public static final String CYAN = "§8";
	public static final String GOLD = "§9";
	public static final String WHITE = "§a";
	public static final String LIGHT_GRAY = "§b";
	public static final String MEDIUM_GRAY = "§c";
	public static final String DARK_GRAY = "§d";
	public static final String BROWN = "§e";
	public static final String PINK = "§f";

	public static Color getColor(char code) {
		return COLORS.containsKey(code) ? COLORS.get(code) : Color.WHITE;
	}

}

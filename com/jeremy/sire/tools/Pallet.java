package com.jeremy.sire.tools;

import java.awt.Color;

public class Pallet {

	public static final Color MAROON = new Color(0x800000);
	public static final Color RED = new Color(0xFF0000);
	public static final Color ORANGE = new Color(0xFFA500);
	public static final Color YELLOW = new Color(0xFFFF00);
	public static final Color OLIVE = new Color(0x808000);
	public static final Color GREEN = new Color(0x008000);
	public static final Color PURPLE = new Color(0x800080);
	public static final Color FUCHSIA = new Color(0xFF00FF);
	public static final Color LIME = new Color(0x00FF00);
	public static final Color TEAL = new Color(0x008080);
	public static final Color AQUA = new Color(0x00FFFF);
	public static final Color BLUE = new Color(0x0000FF);
	public static final Color NAVY = new Color(0x000080);
	public static final Color BLACK = new Color(0x000000);
	public static final Color GRAY = new Color(0x808080);
	public static final Color SILVER = new Color(0xC0C0C0);
	public static final Color WHITE = new Color(0xFFFFFF);

	public static final Color mix(Color a, Color b, float ratio) {
		float ar = (float) a.getRed() / 255;
		float ag = (float) a.getGreen() / 255;
		float ab = (float) a.getBlue() / 255;

		float br = (float) b.getRed() / 255;
		float bg = (float) b.getGreen() / 255;
		float bb = (float) b.getBlue() / 255;

		return new Color(

				Maths.clamp((ar * ratio) + (br * (1f - ratio)), 0, 1), //
				Maths.clamp((ag * ratio) + (bg * (1f - ratio)), 0, 1), //
				Maths.clamp((ab * ratio) + (bb * (1f - ratio)), 0, 1)

		);
	}

}

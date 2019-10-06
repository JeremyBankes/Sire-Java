package com.jeremy.sire.helpers;

import java.awt.Color;
import java.awt.image.BufferedImage;

public class Coloring {

	public static BufferedImage mask(BufferedImage image, int from, int to) {
		for (int i = 0; i < image.getHeight(); i++) {
			for (int j = 0; j < image.getWidth(); j++) {
				if (image.getRGB(j, i) == from) {
					image.setRGB(j, i, to);
				}
			}
		}
		return image;
	}

	public static BufferedImage mask(BufferedImage image, Color from, Color to) {
		return mask(image, from.getRGB(), to.getRGB());
	}

}

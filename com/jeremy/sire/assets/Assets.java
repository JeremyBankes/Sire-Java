package com.jeremy.sire.assets;

import java.awt.Image;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.HashMap;

import javax.imageio.ImageIO;

public class Assets {

	private static final HashMap<String, BufferedImage> IMAGES = new HashMap<>();

	public static BufferedImage getImage(String path) {
		String name = resolveName(path);
		if (IMAGES.containsKey(name)) {
			return IMAGES.get(name);
		}
		try {
			IMAGES.put(name, ImageIO.read(Assets.class.getResourceAsStream(path)));
		} catch (IOException e) {
			e.printStackTrace();
		}
		return getImage(path);
	}

	public static BufferedImage getImage(String path, int width, int height) {
		String name = resolveName(path);
		if (IMAGES.containsKey(name)) {
			BufferedImage image = IMAGES.get(name);
			if (image.getWidth() == width && image.getHeight() == height) {
				return image;
			}
		}
		try {
			BufferedImage image = ImageIO.read(Assets.class.getResourceAsStream(path));
			BufferedImage newImage = new BufferedImage(width, height, Image.SCALE_REPLICATE);
			newImage.getGraphics().drawImage(image.getScaledInstance(width, height, Image.SCALE_AREA_AVERAGING), 0, 0, null);
			IMAGES.put(name, newImage);
		} catch (IOException e) {
			e.printStackTrace();
		}
		return getImage(path);
	}

	private static String resolveName(String name) {
		return name.replace("^\\/", "").replaceAll("\\/|\\\\\\\\", ".").toLowerCase();
	}

}

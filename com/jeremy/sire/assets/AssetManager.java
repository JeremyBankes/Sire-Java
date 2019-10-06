package com.jeremy.sire.assets;

import java.awt.Font;
import java.awt.FontFormatException;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;

import javax.imageio.ImageIO;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.UnsupportedAudioFileException;

import com.jeremy.sire.sound.SoundData;

public class AssetManager {

	private final HashMap<String, BufferedImage> images;
	private final HashMap<String, BufferedImageSheet> imageSheets;
	private final HashMap<String, SoundData> soundData;

	public AssetManager() {
		images = new HashMap<String, BufferedImage>();
		imageSheets = new HashMap<String, BufferedImageSheet>();
		soundData = new HashMap<String, SoundData>();
	}

	public BufferedImage getBufferedImage(String path) {
		if (images.containsKey(path)) {
			return images.get(path);
		}
		return loadBufferedImage(path, true);
	}

	public BufferedImageSheet getBufferedImageSheet(String path, int spriteWidth, int spriteHeight, int spriteCount) {
		if (imageSheets.containsKey(path)) {
			return imageSheets.get(path);
		}
		return loadBufferedImageSheet(path, spriteWidth, spriteHeight, spriteCount, true);
	}

	public SoundData getSoundData(String path) {
		if (soundData.containsKey(path)) {
			return soundData.get(path);
		} else {
			return loadSoundData(path, true);
		}
	}

	public BufferedImage loadBufferedImage(String path, boolean cache) {
		try {
			BufferedImage image = ImageIO.read(AssetManager.class.getResourceAsStream(path));
			if (cache) {
				images.put(path, image);
			}
			return image;
		} catch (IOException | IllegalArgumentException exception) {
			System.out.printf("Failed to load image. (%s)%n", exception.getMessage());
			return null;
		}
	}

	public BufferedImageSheet loadBufferedImageSheet(String path, int spriteWidth, int spriteHeight, int spriteCount, boolean cache) {
		BufferedImageSheet imageSheet = new BufferedImageSheet(loadBufferedImage(path, false), spriteWidth, spriteHeight, spriteCount);
		if (cache) {
			imageSheets.put(path, imageSheet);
		}
		return imageSheet;
	}

	public SoundData loadSoundData(String path, boolean cache) {
		try {
			InputStream inputStream = AssetManager.class.getResourceAsStream(path);
			AudioInputStream audioInputStream = AudioSystem.getAudioInputStream(inputStream);
			ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
			byte[] buffer = new byte[1024];
			int read = 0;
			do {
				byteArrayOutputStream.write(buffer, 0, read);
				read = audioInputStream.read(buffer, 0, buffer.length);
			} while (read != -1);
			byte[] data = byteArrayOutputStream.toByteArray();
			SoundData soundData = new SoundData(audioInputStream.getFormat(), data);
			if (cache) {
				this.soundData.put(path, soundData);
			}
			return soundData;
		} catch (IOException | UnsupportedAudioFileException exception) {
			System.out.printf("Failed to load sound data. (%s)%n", exception.getMessage());
			return null;
		}
	}

	public Font loadFont(String path, int format, int size) {
		try {
			return Font.createFont(format, AssetManager.class.getResourceAsStream(path)).deriveFont((float) size);
		} catch (FontFormatException | IOException exception) {
			exception.printStackTrace();
			return null;
		}
	}

}

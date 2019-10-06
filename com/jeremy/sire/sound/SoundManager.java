package com.jeremy.sire.sound;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Optional;

import com.jeremy.sire.assets.AssetManager;

public class SoundManager {

	private final HashMap<String, HashSet<Sound>> sounds;
	private final AssetManager assetManager;

	public SoundManager(AssetManager assetManager) {
		this.sounds = new HashMap<String, HashSet<Sound>>();
		this.assetManager = assetManager;
	}

	public void playSound(String path) {
		if (sounds.containsKey(path)) {
			HashSet<Sound> soundPool = sounds.get(path);
			Optional<Sound> ghostSound = soundPool.stream().filter(sound -> !sound.isPlaying()).findFirst();
			if (ghostSound.isPresent()) {
				ghostSound.get().play();
			} else {
				Sound sound = new Sound(assetManager.getSoundData(path));
				soundPool.add(sound);
				sound.play();
			}
		} else {
			HashSet<Sound> soundPool = new HashSet<Sound>();
			Sound sound = new Sound(assetManager.getSoundData(path));
			soundPool.add(sound);
			sounds.put(path, soundPool);
			sound.play();
		}
	}

}

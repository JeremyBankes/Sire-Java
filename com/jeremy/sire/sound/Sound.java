package com.jeremy.sire.sound;

import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.LineListener;
import javax.sound.sampled.LineUnavailableException;

public class Sound {

	private SoundData soundData;
	private Clip clip;

	public Sound(SoundData soundData) {
		this.soundData = soundData;
		try {
			clip = AudioSystem.getClip();
		} catch (LineUnavailableException exception) {
			exception.printStackTrace();
		}
	}

	public void play() {
		if (!clip.isOpen()) {
			try {
				clip.open(soundData.getFormat(), soundData.getData(), 0, soundData.getSize());
			} catch (LineUnavailableException exception) {
				exception.printStackTrace();
			}
		}
		if (clip.isOpen()) {
			clip.setFramePosition(0);
			clip.start();
		}
	}

	public void addAudioEventListener(LineListener listener) {
		clip.addLineListener(listener);
	}

	public boolean isPlaying() {
		return clip.isRunning();
	}

}

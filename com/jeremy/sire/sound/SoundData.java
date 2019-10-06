package com.jeremy.sire.sound;

import javax.sound.sampled.AudioFormat;

public class SoundData {

	private AudioFormat format;
	private byte[] data;
	private int size;

	public SoundData(AudioFormat format, byte[] data) {
		this.format = format;
		this.data = data;
		this.size = data.length;
	}

	public AudioFormat getFormat() {
		return format;
	}

	public byte[] getData() {
		return data;
	}

	public int getSize() {
		return size;
	}

}

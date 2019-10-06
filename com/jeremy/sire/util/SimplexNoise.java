package com.jeremy.sire.util;

import java.util.Random;

public class SimplexNoise {

	private SimplexNoiseOctave[] octaves;
	private double[] frequencys;
	private double[] amplitudes;

	private int largestFeature;
	private double persistence;
	private int seed;

	public SimplexNoise(int largestFeature, double persistence, int seed) {
		this.largestFeature = largestFeature;
		this.persistence = persistence;
		this.seed = seed;

		int numberOfOctaves = (int) Math.ceil(Math.log10(this.largestFeature) / Math.log10(2));

		octaves = new SimplexNoiseOctave[numberOfOctaves];
		frequencys = new double[numberOfOctaves];
		amplitudes = new double[numberOfOctaves];

		Random rnd = new Random(this.seed);

		for (int i = 0; i < numberOfOctaves; i++) {
			octaves[i] = new SimplexNoiseOctave(rnd.nextInt());
			frequencys[i] = Math.pow(2, i);
			amplitudes[i] = Math.pow(this.persistence, octaves.length - i);
		}
	}

	public double getNoise2D(int x, int y) {
		double result = 0;
		for (int i = 0; i < octaves.length; i++) {
			result = result + octaves[i].noise(x / frequencys[i], y / frequencys[i]) * amplitudes[i];
		}
		return result;
	}

	public double getNoise3D(int x, int y, int z) {
		double result = 0;
		for (int i = 0; i < octaves.length; i++) {
			double frequency = Math.pow(2, i);
			double amplitude = Math.pow(persistence, octaves.length - i);

			result = result + octaves[i].noise(x / frequency, y / frequency, z / frequency) * amplitude;
		}

		return result;
	}
}

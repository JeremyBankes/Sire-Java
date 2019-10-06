package com.jeremy.sire.tools;

import static java.lang.Math.*;

public class Maths {

	public static final float clamp(float value, float min, float max) {
		return max(min, min(max, value));
	}

	public static final float distance(float x1, float y1, float x2, float y2) {
		double a = abs(x1 - x2);
		double b = abs(y1 - y2);
		return (float) sqrt(a * a + b * b);
	}

	public static final float sin(float angle) {
		return (float) Math.sin(toRadians(angle));
	}

	public static final float cos(float angle) {
		return (float) Math.cos(toRadians(angle));
	}

}

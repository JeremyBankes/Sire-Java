package com.jeremy.sire.serialization;

import java.nio.ByteBuffer;

public class ByteUtilities {

	public static final int write(byte[] destination, byte[] bytes, int pointer) {
		for (int i = 0; i < bytes.length; i++) {
			destination[pointer++] = bytes[i];
		}
		return pointer;
	}

	public static final int write(byte[] destination, byte bytes, int pointer) {
		destination[pointer++] = bytes;
		return pointer;
	}

	public static final byte[] byteToBytes(byte value) {
		return new byte[] { value };
	}

	public static final byte[] booleanToBytes(boolean value) {
		return new byte[] { value ? (byte) 1 : 0 };
	}

	public static final byte[] shortToBytes(short value) {
		return ByteBuffer.allocate(Short.BYTES).putShort(value).array();
	}

	public static final byte[] charToBytes(char value) {
		return ByteBuffer.allocate(Character.BYTES).putChar(value).array();
	}

	public static final byte[] intToBytes(int value) {
		return ByteBuffer.allocate(Integer.BYTES).putInt(value).array();
	}

	public static final byte[] longToBytes(long value) {
		return ByteBuffer.allocate(Long.BYTES).putLong(value).array();
	}

	public static final byte[] floatToBytes(float value) {
		return ByteBuffer.allocate(Float.BYTES).putFloat(value).array();
	}

	public static final byte[] doubleToBytes(double value) {
		return ByteBuffer.allocate(Double.BYTES).putDouble(value).array();
	}

	public static final byte byteFromBytes(byte[] source, int pointer) {
		return source[pointer];
	}

	public static final boolean booleanFromBytes(byte[] source, int pointer) {
		return source[pointer] == 1;
	}

	public static final short shortFromBytes(byte[] source, int pointer) {
		return ByteBuffer.wrap(source, pointer, 2).getShort();
	}

	public static final char charFromBytes(byte[] source, int pointer) {
		return ByteBuffer.wrap(source, pointer, 2).getChar();
	}

	public static final int intFromBytes(byte[] source, int pointer) {
		return ByteBuffer.wrap(source, pointer, 4).getInt();
	}

	public static final long longFromBytes(byte[] source, int pointer) {
		return ByteBuffer.wrap(source, pointer, 8).getLong();
	}

	public static final float floatFromBytes(byte[] source, int pointer) {
		return Float.intBitsToFloat(intFromBytes(source, pointer));
	}

	public static final double doubleFromBytes(byte[] source, int pointer) {
		return Double.longBitsToDouble(longFromBytes(source, pointer));
	}

	public static byte[] merge(byte[] a, byte[] b) {
		int length = a.length + b.length;
		byte[] result = new byte[length];
		System.arraycopy(a, 0, result, 0, a.length);
		System.arraycopy(b, 0, result, a.length, b.length);
		return result;
	}

}

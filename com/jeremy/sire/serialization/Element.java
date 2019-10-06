package com.jeremy.sire.serialization;

import static com.jeremy.sire.serialization.ByteUtilities.*;

import java.util.Arrays;

public class Element {

	public static enum Type {

		/* 0 */ BYTE(Byte.BYTES),
		/* 1 */ SHORT(Short.BYTES),
		/* 2 */ INT(Integer.BYTES),
		/* 3 */ LONG(Long.BYTES),
		/* 4 */ FLOAT(Float.BYTES),
		/* 5 */ DOUBLE(Double.BYTES),
		/* 6 */ BOOLEAN(1),
		/* 7 */ CHAR(Character.BYTES),
		/* 8 */ STRING(-1),
		/* 9 */ ARRAY(-1),
		/* 10 */ TERMINATION(1);

		public final byte id;
		public final int size;

		private Type(int size) {
			id = (byte) ordinal();
			this.size = size;
		}

		public static final Type getType(byte id) {
			return values()[(int) id];
		}

	}

	private Type type;
	private byte[] bytes;
	private String name;
	private Object value;

	public Element(byte[] bytes) {
		int pointer = 0;
		type = Type.getType(bytes[pointer++]);
		int stringByteLength = shortFromBytes(bytes, pointer);
		pointer += Type.SHORT.size;
		this.name = new String(Arrays.copyOfRange(bytes, pointer, pointer + stringByteLength));
		pointer += stringByteLength;
		value = determineValue(type, bytes, pointer);
		this.bytes = getValidBytes(bytes);
	}

	private byte[] getValidBytes(byte[] bytes) {
		int pointer = 1;
		short nameLength = shortFromBytes(bytes, pointer);
		pointer += Type.SHORT.size;
		pointer += nameLength;
		if (type == Type.ARRAY || type == Type.STRING) {
			Type arrayType = Type.getType(bytes[pointer++]);
			pointer += Type.SHORT.size + shortFromBytes(bytes, pointer) * arrayType.size;
		} else {
			pointer += type.size;
		}
		return Arrays.copyOfRange(bytes, 0, pointer);
	}

	public Element(String name, Object value) {
		this.name = name;
		this.value = value;
		type = determineType(value);
		bytes = determineBytes(type, name, value);
	}

	private static final Type determineType(Object value) {
		final Class<?> c = value.getClass();
		if (c == Byte.class) {
			return Type.BYTE;
		} else if (c == Short.class) {
			return Type.SHORT;
		} else if (c == Integer.class) {
			return Type.INT;
		} else if (c == Long.class) {
			return Type.LONG;
		} else if (c == Float.class) {
			return Type.FLOAT;
		} else if (c == Double.class) {
			return Type.DOUBLE;
		} else if (c == Boolean.class) {
			return Type.BOOLEAN;
		} else if (c == Character.class) {
			return Type.CHAR;
		} else if (c.isArray()) {
			return Type.ARRAY;
		} else {
			return Type.STRING;
		}
	}

	private static final byte[] determineBytes(Type type, String name, Object value) {
		byte[] nameBytes = name.getBytes();
		byte[] data = determineData(value);
		short nameLength = (short) nameBytes.length;

		byte[] bytes = new byte[Type.SHORT.size + nameLength + 1 + data.length];

		int pointer = 0;
		pointer = write(bytes, type.id, pointer);
		pointer = write(bytes, shortToBytes(nameLength), pointer);
		pointer = write(bytes, nameBytes, pointer);
		pointer = write(bytes, data, pointer);

		return bytes;
	}

	private static final byte[] determineData(Object value) {
		final Class<?> c = value.getClass();
		if (c == Byte.class) {
			return byteToBytes((Byte) value);
		} else if (c == Short.class) {
			return shortToBytes((Short) value);
		} else if (c == Integer.class) {
			return intToBytes((Integer) value);
		} else if (c == Long.class) {
			return longToBytes((Long) value);
		} else if (c == Float.class) {
			return floatToBytes((Float) value);
		} else if (c == Double.class) {
			return doubleToBytes((Double) value);
		} else if (c == Boolean.class) {
			return booleanToBytes((Boolean) value);
		} else if (c == Character.class) {
			return charToBytes((Character) value);
		} else if (c.isArray()) {
			return arrayToBytes(value);
		} else {
			return determineData(value.toString().toCharArray());
		}
	}

	private static final Object determineValue(Type type, byte[] bytes, int pointer) {
		switch (type) {
		case BYTE:
			return byteFromBytes(bytes, pointer);
		case SHORT:
			return shortFromBytes(bytes, pointer);
		case INT:
			return intFromBytes(bytes, pointer);
		case LONG:
			return longFromBytes(bytes, pointer);
		case FLOAT:
			return floatFromBytes(bytes, pointer);
		case DOUBLE:
			return doubleFromBytes(bytes, pointer);
		case BOOLEAN:
			return booleanFromBytes(bytes, pointer);
		case CHAR:
			return charFromBytes(bytes, pointer);
		case STRING:
			StringBuffer buffer = new StringBuffer();
			for (Object character : arrayFromBytes(bytes, pointer)) {
				buffer.append((Character) character);
			}
			return buffer.toString();
		case ARRAY:
			return arrayFromBytes(bytes, pointer);
		default:
			throw new IllegalStateException("Unrecognised type '" + type + "'.");
		}
	}

	private static byte[] arrayToBytes(Object array) {
		int pointer = 0;
		Class<?> c = array.getClass();
		Type type = null;
		short length = 0;
		byte[] rawData = null;
		if (c == byte[].class) {
			type = Type.BYTE;
			char[] primativeArray = (char[]) array;
			length = (short) primativeArray.length;
			rawData = new byte[length * type.size];
			for (int i = 0; i < length; i++) {
				write(rawData, determineData(primativeArray[i]), i * type.size);
			}
		} else if (c == short[].class) {
			type = Type.SHORT;
			short[] primativeArray = (short[]) array;
			length = (short) primativeArray.length;
			rawData = new byte[length * type.size];
			for (int i = 0; i < length; i++) {
				write(rawData, determineData(primativeArray[i]), i * type.size);
			}
		} else if (c == int[].class) {
			type = Type.INT;
			int[] primativeArray = (int[]) array;
			length = (short) primativeArray.length;
			rawData = new byte[length * type.size];
			for (int i = 0; i < length; i++) {
				write(rawData, determineData(primativeArray[i]), i * type.size);
			}
		} else if (c == long[].class) {
			type = Type.LONG;
			long[] primativeArray = (long[]) array;
			length = (short) primativeArray.length;
			rawData = new byte[length * type.size];
			for (int i = 0; i < length; i++) {
				write(rawData, determineData(primativeArray[i]), i * type.size);
			}
		} else if (c == float[].class) {
			type = Type.FLOAT;
			float[] primativeArray = (float[]) array;
			length = (short) primativeArray.length;
			rawData = new byte[length * type.size];
			for (int i = 0; i < length; i++) {
				write(rawData, determineData(primativeArray[i]), i * type.size);
			}
		} else if (c == double[].class) {
			type = Type.DOUBLE;
			double[] primativeArray = (double[]) array;
			length = (short) primativeArray.length;
			rawData = new byte[length * type.size];
			for (int i = 0; i < length; i++) {
				write(rawData, determineData(primativeArray[i]), i * type.size);
			}
		} else if (c == boolean[].class) {
			type = Type.BOOLEAN;
			boolean[] primativeArray = (boolean[]) array;
			length = (short) primativeArray.length;
			rawData = new byte[length * type.size];
			for (int i = 0; i < length; i++) {
				write(rawData, determineData(primativeArray[i]), i * type.size);
			}
		} else if (c == char[].class) {
			type = Type.CHAR;
			char[] primativeArray = (char[]) array;
			length = (short) primativeArray.length;
			rawData = new byte[length * type.size];
			for (int i = 0; i < length; i++) {
				write(rawData, determineData(primativeArray[i]), i * type.size);
			}
		} else {
			throw new IllegalStateException("Unrecognised array type '" + c.getCanonicalName() + "'.");
		}
		byte[] data = new byte[1 + 2 + length * type.size];
		data[pointer++] = type.id;
		pointer = write(data, shortToBytes(length), pointer);
		pointer = write(data, rawData, pointer);
		return data;
	}

	private static Object[] arrayFromBytes(byte[] bytes, int pointer) {
		Type type = Type.getType(bytes[pointer++]);
		short count = shortFromBytes(bytes, pointer);
		pointer += Type.SHORT.size;
		Object[] values = new Object[count];
		for (int i = 0; i < count; i++) {
			values[i] = determineValue(type, bytes, pointer);
			pointer += type.size;
		}
		return values;
	}

	public Type getType() {
		return type;
	}

	public byte[] getBytes() {
		return bytes;
	}

	public String getName() {
		return name;
	}

	public Object getValue() {
		return value;
	}

	public <T> T getValue(Class<T> clazz) {
		return clazz.cast(value);
	}

	@Override
	public String toString() {
		return name + ": [" + type.toString() + "] " + value;
	}

}

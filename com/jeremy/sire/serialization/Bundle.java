package com.jeremy.sire.serialization;

import static com.jeremy.sire.serialization.ByteUtilities.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.LinkedHashMap;

import com.jeremy.sire.serialization.Element.Type;

public class Bundle {

	private LinkedHashMap<String, Element> elements;
	private String name;

	public Bundle(String name) {
		this.elements = new LinkedHashMap<String, Element>();
		this.name = name;
	}

	// 2b - Bundle Name Length (Short)
	// ?b - Bundle Name Data (String)
	// ?b - Element Data (Element)
	// 1b - Termination Byte (byte)

	public Bundle(byte[] bytes) {
		this.elements = new LinkedHashMap<String, Element>();
		int pointer = 0;
		int stringByteLength = shortFromBytes(bytes, pointer);
		pointer += Type.SHORT.size;
		this.name = new String(Arrays.copyOfRange(bytes, pointer, pointer + stringByteLength));
		pointer += stringByteLength;

		while (bytes[pointer] != Type.TERMINATION.id) {
			pointer += put(new Element(Arrays.copyOfRange(bytes, pointer, bytes.length))).getBytes().length;
		}
	}

	public String getName() {
		return name;
	}

	public Collection<Element> getElements() {
		return elements.values();
	}

	public Bundle put(Element element) {
		elements.put(element.getName(), element);
		return this;
	}

	public Bundle put(String name, Object value) {
		put(new Element(name, value));
		return this;
	}

	public Element getElement(String name) {
		return elements.get(name);
	}

	public Object get(String name) {
		return getElement(name).getValue();
	}

	public <T> T get(String name, Class<T> elementClass) {
		return elementClass.cast(get(name));
	}

	public boolean has(String name) {
		return elements.containsKey(name);
	}

	public byte[] getBytes() {
		ArrayList<byte[]> data = new ArrayList<>();
		elements.values().forEach(element -> {
			data.add(element.getBytes());
		});
		byte[] nameBytes = name.getBytes();
		int length = Type.SHORT.size + nameBytes.length + (data.stream().mapToInt(e -> e.length).sum()) + 1;
		byte[] bytes = new byte[length];
		int pointer = 0;
		pointer = write(bytes, shortToBytes((short) nameBytes.length), pointer);
		pointer = write(bytes, nameBytes, pointer);
		for (byte[] elementBytes : data) {
			pointer = write(bytes, elementBytes, pointer);
		}
		pointer = write(bytes, Type.TERMINATION.id, pointer);
		return bytes;
	}

	@Override
	public String toString() {
		StringBuffer buffer = new StringBuffer();
		buffer.append(name);
		buffer.append(":\n");
		elements.values().forEach(element -> {
			buffer.append('\t');
			buffer.append(element.toString());
			buffer.append('\n');
		});
		return buffer.toString();
	}

}

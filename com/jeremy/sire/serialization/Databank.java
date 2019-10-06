package com.jeremy.sire.serialization;

import static com.jeremy.sire.serialization.ByteUtilities.*;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.LinkedHashMap;

import com.jeremy.sire.serialization.Element.Type;

public class Databank {

	private LinkedHashMap<String, Bundle> bundles;
	private String name;

	public Databank(String name) {
		this.bundles = new LinkedHashMap<String, Bundle>();
		this.name = name;
	}

	// 2b - Bundle Name Length (Short)
	// ?b - Bundle Name Data (String)
	// ?b - Bundle Data (Bundle)
	// 1b - Termination Byte (byte)

	public Databank(byte[] bytes) {
		this.bundles = new LinkedHashMap<String, Bundle>();
		int pointer = 0;
		int stringByteLength = shortFromBytes(bytes, pointer);
		pointer += Type.SHORT.size;
		this.name = new String(Arrays.copyOfRange(bytes, pointer, pointer + stringByteLength));
		pointer += stringByteLength;

		while (bytes[pointer] != Type.TERMINATION.id) {
			pointer += put(new Bundle(Arrays.copyOfRange(bytes, pointer, bytes.length))).getBytes().length;
		}
	}

	public Databank(InputStream inputStream) throws IOException {
		this(readBytesFromStream(inputStream));
	}

	public String getName() {
		return name;
	}

	public Collection<Bundle> getBundles() {
		return bundles.values();
	}

	public Databank put(Bundle bundle) {
		bundles.put(bundle.getName(), bundle);
		return this;
	}

	public Bundle get(String name) {
		return bundles.get(name);
	}

	public <T> T get(String name, Class<T> bundleClass) {
		return bundleClass.cast(get(name));
	}

	public boolean has(String name) {
		return bundles.containsKey(name);
	}

	public byte[] getBytes() {
		ArrayList<byte[]> data = new ArrayList<>();
		bundles.values().forEach(bundle -> {
			data.add(bundle.getBytes());
		});
		byte[] nameBytes = name.getBytes();
		int length = Type.SHORT.size + nameBytes.length + (data.stream().mapToInt(e -> e.length).sum()) + 1;
		byte[] bytes = new byte[length];
		int pointer = 0;
		pointer = write(bytes, shortToBytes((short) nameBytes.length), pointer);
		pointer = write(bytes, nameBytes, pointer);
		for (byte[] bundleBytes : data) {
			pointer = write(bytes, bundleBytes, pointer);
		}
		pointer = write(bytes, Type.TERMINATION.id, pointer);
		return bytes;
	}

	@Override
	public String toString() {
		StringBuffer buffer = new StringBuffer();
		buffer.append(name);
		buffer.append(":\n");
		bundles.values().forEach(bundle -> {
			buffer.append('\t');
			buffer.append(bundle.toString());
			buffer.append('\n');
		});
		return buffer.toString();
	}

	public void saveToFile(File file) throws IOException {
		if (!file.exists()) {
			if (!file.getParentFile().exists())
				file.getParentFile().mkdirs();
			file.createNewFile();
		}
		FileOutputStream output = new FileOutputStream(file);
		output.write(getBytes());
		output.close();
	}

	private static byte[] readBytesFromStream(InputStream inputStream) throws IOException {
		int size = 0;
		int i = 0;
		while (i < Integer.BYTES) {
			int byt = inputStream.read();
			if (byt == -1)
				continue;
			size = (size << 8) | byt & 0xFF;
			i++;
		}
		byte[] bytes = new byte[size];
		size -= Integer.BYTES;
		i = 0;
		while (i < size) {
			int byt = inputStream.read();
			if (byt == -1)
				continue;
			bytes[i + Integer.BYTES] = (byte) byt;
			i++;
		}
		return bytes;
	}

}

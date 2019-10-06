package com.jeremy.sire.helpers.networking;

import static com.jeremy.sire.serialization.ByteUtilities.*;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.net.SocketException;

import com.jeremy.sire.serialization.Bundle;
import com.jeremy.sire.serialization.Element.Type;

public class NetworkUtilities {

	public static Bundle readBundleFromStream(InputStream inputStream) throws SocketException {
		ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
		try {
			int nameLength = shortFromBytes(writeBytesFromInToOut(inputStream, outputStream, 2), 0);
			writeBytesFromInToOut(inputStream, outputStream, nameLength);
			Type type;
			do {
				type = Type.getType((byte) inputStream.read());
				outputStream.write(type.id);

				if (type == Type.TERMINATION)
					continue;

				writeBytesFromInToOut(inputStream, outputStream, shortFromBytes(writeBytesFromInToOut(inputStream, outputStream, 2), 0));
				if (type == Type.ARRAY || type == Type.STRING) {
					byte[] buffer = writeBytesFromInToOut(inputStream, outputStream, 3);
					writeBytesFromInToOut(inputStream, outputStream, Type.getType(buffer[0]).size * shortFromBytes(buffer, 1));
				} else {
					writeBytesFromInToOut(inputStream, outputStream, type.size);
				}
			} while (type != Type.TERMINATION);
		} catch (SocketException socketException) {
			throw socketException;
		} catch (IOException ioException) {
			ioException.printStackTrace();
		} catch (ArrayIndexOutOfBoundsException arrayOfBoundsException) {
			System.out.printf("No more data to read in stream%n");
		}
		return new Bundle(outputStream.toByteArray());
	}

	public static byte[] writeBytesFromInToOut(InputStream inputStream, OutputStream outputStream, int byteCount) throws IOException {
		byte[] buffer = new byte[byteCount];
		inputStream.read(buffer);
		outputStream.write(buffer);
		return buffer;
	}

	public static String key(Socket socket) {
		return String.format("%s:%s", socket.getInetAddress().getHostAddress(), socket.getPort());
	}

	public static void printBytes(byte[] bytes) {
		for (byte b : bytes) {
			System.out.print(b + " ");
		}
		System.out.println();
	}

}

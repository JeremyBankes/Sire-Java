package com.jeremy.sire.helpers.networking;

import static com.jeremy.sire.helpers.networking.NetworkUtilities.*;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.net.SocketException;
import java.util.LinkedList;

import com.jeremy.sire.serialization.Bundle;

public class TcpClient {

	private Socket socket;
	private final LinkedList<TCPClientReceiveCallback> callbacks;

	private boolean listening;
	private Thread thread;

	private InputStream inputStream;
	private OutputStream outputStream;

	public TcpClient() {
		thread = new Thread(this::run);
		callbacks = new LinkedList<TCPClientReceiveCallback>();
	}

	public void connect(String address, int port) throws IOException {
		socket = new Socket(address, port);
		inputStream = socket.getInputStream();
		outputStream = socket.getOutputStream();
	}

	public void disconnect() throws IOException {
		if (isListening()) {
			ignore();
		}
		if (socket != null) {
			socket.close();
		}
	}

	public void listen() {
		listening = true;
		thread.start();
	}

	public boolean isListening() {
		return listening;
	}

	public void ignore() {
		listening = false;
	}

	public void send(Bundle bundle) throws IOException {
		if (outputStream == null) {
			throw new IOException("Attempted to send bundle to server before successfully connecting");
		}
		outputStream.write(bundle.getBytes());
	}

	private void run() {
		if (!socket.isConnected()) {
			return;
		}

		try {
			while (listening) {
				if (socket.isClosed()) {
					listening = false;
				} else {
					Bundle bundle = readBundleFromStream(inputStream);
					getReceiveCallbacks().forEach(callback -> callback.receive(bundle));
				}
			}
		} catch (SocketException socketException) {
			System.out.println("Disconnected from server: " + socketException.getMessage());
		}
	}

	public boolean isConnected() {
		return socket != null && !socket.isClosed();
	}

	public LinkedList<TCPClientReceiveCallback> getReceiveCallbacks() {
		return callbacks;
	}

	public void addReceiveCallback(TCPClientReceiveCallback callback) {
		callbacks.add(callback);
	}

	public void removeReceiveCallback(TCPClientReceiveCallback callback) {
		callbacks.remove(callback);
	}

	public static interface TCPClientReceiveCallback {

		public abstract void receive(Bundle bundle);

	}

}

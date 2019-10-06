package com.jeremy.sire.helpers.networking;

import static com.jeremy.sire.helpers.networking.NetworkUtilities.*;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketException;
import java.util.HashMap;
import java.util.LinkedList;

import com.jeremy.sire.helpers.networking.TcpServer.NetworkEventCallback.NetworkEventType;
import com.jeremy.sire.serialization.Bundle;

public class TcpServer {

	private ServerSocket socket;
	private final LinkedList<TcpServerReceiveCallback> callbacks;
	private NetworkEventCallback networkEventCallback;

	private HashMap<String, Client> connectedClients;

	private boolean running;
	private Thread thread;

	public TcpServer() {
		connectedClients = new HashMap<String, Client>();
		callbacks = new LinkedList<TcpServerReceiveCallback>();
	}

	public void start(int port) throws IOException {
		running = true;
		thread = new Thread(this::run, "tcp-server");
		socket = new ServerSocket(port);
		thread.start();
	}

	public void stop() {
		running = false;
	}

	private void run() {
		while (running) {
			try {
				Socket clientSocket = socket.accept();
				final String key = key(clientSocket);
				new Thread(() -> {
					if (!clientSocket.isConnected()) {
						return;
					}
					try {
						Object disconnectInfo = "";
						final InputStream inputStream = clientSocket.getInputStream();
						final OutputStream outputStream = clientSocket.getOutputStream();
						connectedClients.put(key, new Client(clientSocket, outputStream));
						if (getNetworkEventCallback() != null) getNetworkEventCallback().onEvent(NetworkEventType.CONNECT, key);
						try {
							while (!clientSocket.isClosed()) {
								try {
									Bundle bundle = readBundleFromStream(inputStream);
									getReceiveCallbacks().forEach(callback -> callback.receive(key, bundle));
								} catch (ArrayIndexOutOfBoundsException outOfBoundsException) {
									disconnectInfo = outOfBoundsException.getMessage();
									clientSocket.close();
								}
							}
						} catch (SocketException socketException) {
							disconnectInfo = socketException.getMessage();
						}
						if (getNetworkEventCallback() != null) getNetworkEventCallback().onEvent(NetworkEventType.DISCONNECT, key, disconnectInfo);
					} catch (Exception exception) {
						exception.printStackTrace();
					} finally {
						connectedClients.remove(key);
					}
				}).start();
			} catch (IOException ioException) {
				ioException.printStackTrace();
			}
		}
	}

	public void send(String address, Bundle bundle) throws IOException {
		connectedClients.get(address).sendBundle(bundle);
	}

	public void disconnect(String address) throws IOException {
		connectedClients.get(address).socket.close();
	}

	public boolean isClient(String address) {
		return connectedClients.containsKey(address);
	}

	public boolean isRunning() {
		return running;
	}

	public LinkedList<TcpServerReceiveCallback> getReceiveCallbacks() {
		return callbacks;
	}

	public void addReceiveCallback(TcpServerReceiveCallback callback) {
		callbacks.add(callback);
	}

	public void removeReceiveCallback(TcpServerReceiveCallback callback) {
		callbacks.remove(callback);
	}

	public NetworkEventCallback getNetworkEventCallback() {
		return networkEventCallback;
	}

	public void setNetworkEventCallback(NetworkEventCallback networkEventCallback) {
		this.networkEventCallback = networkEventCallback;
	}

	public void broadcast(Bundle bundle) throws IOException {
		for (Client client : connectedClients.values()) client.sendBundle(bundle);
	}

	public int getLocalPort() {
		return socket.getLocalPort();
	}

	public static interface TcpServerReceiveCallback {

		public abstract void receive(String address, Bundle bundle);

	}

	public static interface NetworkEventCallback {

		public static enum NetworkEventType {
			CONNECT,
			DISCONNECT
		}

		public abstract void onEvent(NetworkEventType type, String address, Object... info);

	}

	private static class Client {

		private Socket socket;
		private OutputStream outputStream;

		public Client(Socket socket, OutputStream outputStream) {
			this.socket = socket;
			this.outputStream = outputStream;
		}

		public void sendBundle(Bundle bundle) throws IOException {
			outputStream.write(bundle.getBytes());
		}

	}

}

package com.jeremy.sire.helpers.networking;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;

import com.jeremy.sire.serialization.Bundle;

public class UdpServer {

	public static final int BUFFER_SIZE = 1024;

	private DatagramSocket socket;
	private UdpServerReceiveCallback callback;

	private boolean running;
	private Thread thread;

	public void start(int port) throws IOException {
		running = true;
		thread = new Thread(this::run, "udp-server");
		socket = new DatagramSocket(port);
		thread.start();
	}

	public void stop() {
		running = false;
	}

	private void run() {
		final byte[] buffer = new byte[BUFFER_SIZE];
		while (running) {
			try {
				DatagramPacket packet = new DatagramPacket(buffer, BUFFER_SIZE);
				socket.receive(packet);
				final String key = packet.getAddress().getHostAddress() + ":" + packet.getPort();
				Bundle bundle = new Bundle(packet.getData());
				if (getReceieveCallback() != null) {
					getReceieveCallback().receive(key, bundle);
				}
			} catch (IOException ioException) {
				ioException.printStackTrace();
			}
		}
	}

	public void send(String address, Bundle bundle) throws IOException {
		String[] socketAddress = address.split(":");
		byte[] buffer = bundle.getBytes();
		DatagramPacket packet = new DatagramPacket(buffer, buffer.length, InetAddress.getByName(socketAddress[0]),
				Integer.parseInt(socketAddress[1]));

		socket.send(packet);
	}

	public UdpServerReceiveCallback getReceieveCallback() {
		return callback;
	}

	public void setReceiveCallback(UdpServerReceiveCallback callback) {
		this.callback = callback;
	}

	public static interface UdpServerReceiveCallback {

		public abstract void receive(String address, Bundle bundle);

	}

}

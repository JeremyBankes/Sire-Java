package com.jeremy.sire.helpers.networking;

import java.security.GeneralSecurityException;
import java.security.Key;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.NoSuchAlgorithmException;

import javax.crypto.Cipher;

public class Encrypter {

	private Cipher cipher;

	public Encrypter(Key key) throws GeneralSecurityException {
		cipher = Cipher.getInstance("RSA");
		cipher.init(Cipher.ENCRYPT_MODE, key);
	}

	public byte[] encrypt(byte[] data) throws GeneralSecurityException {
		return cipher.doFinal(data);
	}

	public static KeyPair generateKeyPair() {
		try {
			KeyPairGenerator generator = KeyPairGenerator.getInstance("RSA");
			generator.initialize(1024);
			return generator.generateKeyPair();
		} catch (NoSuchAlgorithmException exception) {
			exception.printStackTrace();
		}
		return null;
	}

}

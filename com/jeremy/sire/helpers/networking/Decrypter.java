package com.jeremy.sire.helpers.networking;

import java.security.GeneralSecurityException;
import java.security.Key;
import java.security.KeyPair;

import javax.crypto.Cipher;

public class Decrypter {

	private Cipher cipher;

	public Decrypter(Key key) throws GeneralSecurityException {
		cipher = Cipher.getInstance("RSA");
		cipher.init(Cipher.DECRYPT_MODE, key);
	}

	public byte[] decrypt(byte[] data) throws GeneralSecurityException {
		return cipher.doFinal(data);
	}

	public static KeyPair generateKeyPair() {
		return Encrypter.generateKeyPair();
	}

}

package com.arny.arnylib.utils;

import android.os.Environment;
import android.util.Log;

import javax.crypto.*;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import java.io.*;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
public class EncryptUtils {

	private static final String ALGO_ENCRYPTOR = "AES/CBC/PKCS7Padding";
	private static String INITIALIZATIO_VECTOR = "AODVNUASDNVVAOVF";


	public static byte[] encryptStr(String plainText) throws Exception {
		Cipher cipher = Cipher.getInstance(ALGO_ENCRYPTOR);
		SecretKeySpec key = new SecretKeySpec(INITIALIZATIO_VECTOR.getBytes("UTF-8"), "AES");
		cipher.init(Cipher.ENCRYPT_MODE, key,new IvParameterSpec(INITIALIZATIO_VECTOR.getBytes("UTF-8")));
		return cipher.doFinal(plainText.getBytes("UTF-8"));
	}

	public static String decryptStr(byte[] cipherText) throws Exception{
		Cipher cipher = Cipher.getInstance(ALGO_ENCRYPTOR);
		SecretKeySpec key = new SecretKeySpec(INITIALIZATIO_VECTOR.getBytes("UTF-8"), "AES");
		cipher.init(Cipher.DECRYPT_MODE, key,new IvParameterSpec(INITIALIZATIO_VECTOR.getBytes("UTF-8")));
		return new String(cipher.doFinal(cipherText),"UTF-8");
	}

	public static byte[] generateKey(String password) throws Exception {
		byte[] keyStart = password.getBytes("UTF-8");
		KeyGenerator kgen = KeyGenerator.getInstance("AES");
		SecureRandom sr = SecureRandom.getInstance("SHA1PRNG");
		sr.setSeed(keyStart);
		kgen.init(128, sr);
		SecretKey skey = kgen.generateKey();
		return skey.getEncoded();
	}

	public static boolean encrypt( String path,String newpath,String fileName,byte[] pass) throws IOException, NoSuchAlgorithmException,
			NoSuchPaddingException, InvalidKeyException, InvalidAlgorithmParameterException {
		File extStore = Environment.getExternalStorageDirectory();
		long startTime = System.currentTimeMillis();
		Log.i("Encryption Started", extStore + path);
		FileInputStream fis = new FileInputStream(extStore + path);
		File folder = new File(extStore + newpath);
		boolean folderFilesExist = folder.exists() || folder.mkdir();
		if (!folderFilesExist) {
			return false;
		}
		FileOutputStream fos = new FileOutputStream(extStore + newpath + fileName, false);
		SecretKeySpec sks = new SecretKeySpec(pass,"AES");
		Cipher cipher = Cipher.getInstance(EncryptUtils.ALGO_ENCRYPTOR);
		cipher.init(Cipher.ENCRYPT_MODE, sks, new IvParameterSpec(new byte[16]));
		CipherOutputStream cos = new CipherOutputStream(fos, cipher);
		int b;
		byte[] d = new byte[1024 * 1024];
		while ((b = fis.read(d)) != -1) {
			cos.write(d, 0, b);
		}
		cos.flush();
		cos.close();
		fis.close();
		long stopTime = System.currentTimeMillis();
		Log.i("Encryption Ended", extStore + newpath + " length:" + new File(extStore + newpath).length());
		Log.i("Time Elapsed", ((stopTime - startTime) / 1000.0) + "");
		return true;
	}

	public static boolean decrypt( String path, String newpath,String fileName,byte[] pass) throws IOException, NoSuchAlgorithmException,
			NoSuchPaddingException, InvalidKeyException, InvalidAlgorithmParameterException {
		long startTime = System.currentTimeMillis();
		File extStore = Environment.getExternalStorageDirectory();
		Log.i("Decryption Started", extStore + path);
		File fil = new File(extStore+path);
		boolean  FileExist = fil.exists();
		if (!FileExist) {
			return false;
		}
		File folder = new File(extStore + newpath);
		boolean folderFilesExist = folder.exists() || folder.mkdir();
		if (!folderFilesExist) {
			return false;
		}
		FileInputStream fis = new FileInputStream(extStore + path);
		FileOutputStream fos = new FileOutputStream(extStore + newpath + fileName, false);
		SecretKeySpec sks = new SecretKeySpec(pass,"CBC");
		// Create cipher
		Cipher cipher = Cipher.getInstance(EncryptUtils.ALGO_ENCRYPTOR);
		cipher.init(Cipher.DECRYPT_MODE, sks, new IvParameterSpec(new byte[16]));
		CipherInputStream cis = new CipherInputStream(fis, cipher);

		int b;
		byte[] d = new byte[1024 * 1024];
		while ((b = cis.read(d)) != -1) {
			fos.write(d, 0, b);
		}

		long stopTime = System.currentTimeMillis();

		Log.i("Decryption Ended", extStore + newpath + fileName + " length:" + new File(extStore + newpath + fileName).length());
		Log.i("Time Elapsed", ((stopTime - startTime) / 1000.0) + "");

		fos.flush();
		fos.close();
		cis.close();
		return true;
	}
}

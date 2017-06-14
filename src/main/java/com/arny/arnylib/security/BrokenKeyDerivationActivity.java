package com.arny.arnylib.security;
import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import java.io.*;
import java.security.GeneralSecurityException;
import java.security.SecureRandom;
import java.security.spec.KeySpec;
import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.PBEKeySpec;
import javax.crypto.spec.SecretKeySpec;
public class BrokenKeyDerivationActivity extends Activity {
    /**
     * Method used to derive an <b>insecure</b> key by emulating the SHA1PRNG algorithm from the
     * deprecated Crypto provider.
     *
     * Do not use it to encrypt new data, just to decrypt encrypted data that would be unrecoverable
     * otherwise.
     */
    private static SecretKey deriveKeyInsecurely(String password, int keySizeInBytes) throws UnsupportedEncodingException {
        byte[] passwordBytes = password.getBytes("UTF-8");
        return new SecretKeySpec(
                InsecureSHA1PRNGKeyDerivator.deriveInsecureKey(passwordBytes, keySizeInBytes),
                "AES");
    }
    /**
     * Example use of a key derivation function, derivating a key securely from a password.
     */
    private  SecretKey deriveKeySecurely(String password, int keySizeInBytes) {
        // Use this to derive the key from the password:
        KeySpec keySpec = new PBEKeySpec(password.toCharArray(), retrieveSalt(),
                100 /* iterationCount */, keySizeInBytes * 8 /* key size in bits */);
        try {
            SecretKeyFactory keyFactory = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA1");
            byte[] keyBytes = keyFactory.generateSecret(keySpec).getEncoded();
            return new SecretKeySpec(keyBytes, "AES");
        } catch (Exception e) {
            throw new RuntimeException("Deal with exceptions properly!", e);
        }
    }
    /**
     * Retrieve encrypted data using a password. If data is stored with an insecure key, re-encrypt
     * with a secure key.
     */
    private String retrieveData(String password) throws UnsupportedEncodingException {
        String decryptedString;
        if (isDataStoredWithInsecureKey()) {
            SecretKey insecureKey = deriveKeyInsecurely(password, KEY_SIZE);
            byte[] decryptedData = decryptData(retrieveEncryptedData(), retrieveIv(), insecureKey);
            SecretKey secureKey = deriveKeySecurely(password, KEY_SIZE);
            storeDataEncryptedWithSecureKey(encryptData(decryptedData, retrieveIv(), secureKey));
            decryptedString = "Warning: data was encrypted with insecure key\n"
                    + new String(decryptedData, "UTF-8");
        } else {
            SecretKey secureKey = deriveKeySecurely(password, KEY_SIZE);
            byte[] decryptedData = decryptData(retrieveEncryptedData(), retrieveIv(), secureKey);
            decryptedString = "Great!: data was encrypted with secure key\n"
                    + new String(decryptedData, "UTF-8");
        }
        return decryptedString;
    }
    /*
     ***********************************************************************************************
     * The essential point of this example are the three methods above. Everything below this
     * comment just gives a concrete example of usage and defines mock methods.
     ***********************************************************************************************
     */
    /**
     * Retrieves encrypted data twice and displays the results.
     *
     * The mock data is encrypted with an insecure key (see {@link #cleanRoomStart()}) and so the
     * first time {@link #retrieveData(String)} reencrypts it and returns the plain text with a
     * warning message. The second time, as the data is properly encrypted, the plain text is
     * returned with a congratulations message.
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // Remove any files from previous executions of this app and initialize mock encrypted data.
        // Just so that the application has the same behaviour every time is run. You don't need to
        // do this in your app.
        try {
            cleanRoomStart();
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        // Find the text editor view inside the layout.
        String password = "unguessable";
        try {
            String firstResult = retrieveData(password);
            String secondResult = retrieveData(password);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    private static byte[] encryptOrDecrypt( byte[] data, SecretKey key, byte[] iv, boolean isEncrypt) {
        try {
            Cipher cipher = Cipher.getInstance("AES/CBC/PKCS7PADDING");
            cipher.init(isEncrypt ? Cipher.ENCRYPT_MODE : Cipher.DECRYPT_MODE, key,
                    new IvParameterSpec(iv));
            return cipher.doFinal(data);
        } catch (GeneralSecurityException e) {
            throw new RuntimeException("This is unconceivable!", e);
        }
    }
    private static byte[] encryptData(byte[] data, byte[] iv, SecretKey key) {
        return encryptOrDecrypt(data, key, iv, true);
    }
    private static byte[] decryptData(byte[] data, byte[] iv, SecretKey key) {
        return encryptOrDecrypt(data, key, iv, false);
    }
    /**
     * Remove any files from previous executions of this app and initialize mock encrypted data.
     *
     * <p>Just so that the application has the same behaviour every time is run. You don't need to
     * do this in your app.
     */
    private void cleanRoomStart() throws UnsupportedEncodingException {
        removeFile("salt");
        removeFile("iv");
        removeFile(SECURE_ENCRYPTION_INDICATOR_FILE_NAME);
        // Mock initial data
        encryptedData = encryptData(
                "I hope it helped!".getBytes(), retrieveIv(),
                deriveKeyInsecurely("unguessable", KEY_SIZE));
    }
    /*
     ***********************************************************************************************
     * Everything below this comment is a succession of mocks that would rarely interest someone on
     * Earth. They are merely intended to make the example self contained.
     ***********************************************************************************************
     */
    private boolean isDataStoredWithInsecureKey() {
        // Your app should have a way to tell whether the data has been re-encrypted in a secure
        // fashion, in this mock we use the existence of a file with a certain name to indicate
        // that.
        return !fileExists("encrypted_with_secure_key");
    }
    private byte[] retrieveIv() {
        byte[] iv = new byte[IV_SIZE];
        // Ideally your data should have been encrypted with a random iv. This creates a random iv
        // if not present, in order to encrypt our mock data.
        readFromFileOrCreateRandom("iv", iv);
        return iv;
    }
    private byte[] retrieveSalt() {
        // Salt must be at least the same size as the key.
        byte[] salt = new byte[KEY_SIZE];
        // Create a random salt if encrypting for the first time, and save it for future use.
        readFromFileOrCreateRandom("salt", salt);
        return salt;
    }
    private byte[] encryptedData = null;
    private byte[] retrieveEncryptedData() {
        return encryptedData;
    }
    private void storeDataEncryptedWithSecureKey(byte[] encryptedData) {
        // Mock implementation.
        this.encryptedData = encryptedData;
        writeToFile(SECURE_ENCRYPTION_INDICATOR_FILE_NAME, new byte[1]);
    }
    /**
     * Read from file or return random bytes in the given array.
     *
     * <p>Save to file if file didn't exist.
     */
    private void readFromFileOrCreateRandom(String fileName, byte[] bytes) {
        if (fileExists(fileName)) {
            readBytesFromFile(fileName, bytes);
            return;
        }
        SecureRandom sr = new SecureRandom();
        sr.nextBytes(bytes);
        writeToFile(fileName, bytes);
    }
    private boolean fileExists(String fileName) {
        File file = new File(getFilesDir(), fileName);
        return file.exists();
    }
    private void removeFile(String fileName) {
        File file = new File(getFilesDir(), fileName);
        file.delete();
    }
    private void writeToFile(String fileName, byte[] bytes) {
        try {
	        FileOutputStream fos = openFileOutput(fileName, Context.MODE_PRIVATE);
	        fos.write(bytes);
        } catch (IOException e) {
            throw new RuntimeException("Couldn't write to " + fileName, e);
        }
    }
    private void readBytesFromFile(String fileName, byte[] bytes) {
        try {
	        FileInputStream fis = openFileInput(fileName);
	        int numBytes = 0;
            while (numBytes < bytes.length) {
                int n = fis.read(bytes, numBytes, bytes.length - numBytes);
                if (n <= 0) {
                    throw new RuntimeException("Couldn't read from " + fileName);
                }
                numBytes += n;
            }
        } catch (IOException e) {
            throw new RuntimeException("Couldn't read from " + fileName, e);
        }
    }
    private static final int IV_SIZE = 16;
    private static final int KEY_SIZE = 32;
    private static final String SECURE_ENCRYPTION_INDICATOR_FILE_NAME =
            "encrypted_with_secure_key";
}
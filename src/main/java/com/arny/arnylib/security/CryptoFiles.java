package com.arny.arnylib.security;

import com.arny.arnylib.utils.DateTimeUtils;

import javax.crypto.Cipher;
import javax.crypto.CipherInputStream;
import javax.crypto.CipherOutputStream;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import java.io.*;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class CryptoFiles {

    static final String ALGO_ENCRYPTOR = "AES/CBC/PKCS5Padding";
    private static final int BUFF_SIZE = 1024 * 1024;
    private static final int BYTE_CNT = 8;
    static final String INITIALIZATIO_VECTOR = "AODVNUASDNVVAOVF";

    public static final String AES = "AES";

    public static String getFileChecksum(MessageDigest digest, File file) throws IOException {
        //Get file input stream for reading the file content
        FileInputStream fis = new FileInputStream(file);
        //Create byte array to read data in chunks
        byte[] byteArray = new byte[1024];
        int bytesCount = 0;
        //Read file data and update in message digest
        while ((bytesCount = fis.read(byteArray)) != -1) {
            digest.update(byteArray, 0, bytesCount);
        }
        //close the stream; We don't need it now.
        fis.close();
        //Get the hash's bytes
        byte[] bytes = digest.digest();
        //This bytes[] has bytes in decimal format;
        //Convert it to hexadecimal format
        StringBuilder sb = new StringBuilder();
        for (byte aByte : bytes) {
            sb.append(Integer.toString((aByte & 0xff) + 0x100, 16).substring(1));
        }
        //return complete hash
        return sb.toString();
    }

    public static boolean encrypt(byte[] password, String path, String newpath, String name) throws IOException, NoSuchAlgorithmException,
            NoSuchPaddingException, InvalidKeyException, InvalidAlgorithmParameterException {
        long startencrypt = System.currentTimeMillis();
        System.out.println("Encryption Started " + path + " size:" + new File(path).length());
        FileInputStream fis = new FileInputStream(path);
        File folder = new File(newpath);
        boolean folderFilesExist = folder.exists() || folder.mkdirs();
        System.out.println("encrypt: folderFilesExist = " + folderFilesExist);
        if (!folderFilesExist) {
            return false;
        }
        System.out.println("encrypt: newpath + name = " + (newpath + name));
        FileOutputStream fos = new FileOutputStream((newpath + name), false);
        SecretKeySpec sks = new SecretKeySpec(password, "AES");
        Cipher cipher = Cipher.getInstance(CryptoFiles.ALGO_ENCRYPTOR);
        cipher.init(Cipher.ENCRYPT_MODE, sks, new IvParameterSpec(new byte[16]));
        CipherOutputStream cos = new CipherOutputStream(fos, cipher);
        System.gc();
        Runtime.getRuntime().gc();
        int b;
        byte[] d = new byte[BUFF_SIZE * BYTE_CNT];
        while ((b = fis.read(d)) != -1) {
            cos.write(d, 0, b);
        }
        d = null;
        cos.flush();
        cos.close();
        fis.close();
        System.gc();
        Runtime.getRuntime().gc();
        System.out.println("Encryption Ended " + newpath + name + " length:" + new File(newpath + name).length());
        System.out.println("Time encrypt " + String.valueOf(DateTimeUtils.getTimeDiff(startencrypt)));
        return true;
    }

    public static boolean decrypt(byte[] password, String path, String newpath, String name) throws IOException, NoSuchAlgorithmException,
            NoSuchPaddingException, InvalidKeyException, InvalidAlgorithmParameterException {
        long startdecrypt = System.currentTimeMillis();
        System.out.println("Decryption Started " + path);
        FileInputStream fis = new FileInputStream(path);
        FileOutputStream fos = new FileOutputStream(newpath + name, false);
        SecretKeySpec sks = new SecretKeySpec(password, "AES");
        // Create cipher
        Cipher cipher = Cipher.getInstance(CryptoFiles.ALGO_ENCRYPTOR);
        cipher.init(Cipher.DECRYPT_MODE, sks, new IvParameterSpec(new byte[16]));
        CipherInputStream cis = new CipherInputStream(fis, cipher);
        System.gc();
        Runtime.getRuntime().gc();
        int b;
        byte[] d = new byte[BUFF_SIZE * BYTE_CNT];
        while ((b = cis.read(d)) != -1) {
            fos.write(d, 0, b);
        }
        d = null;
        fos.flush();
        fos.close();
        cis.close();
        System.gc();
        Runtime.getRuntime().gc();
        System.out.println("Decryption Ended " + newpath + name + " length:" + new File(newpath + name).length());
        System.out.println("Time decrypt " + String.valueOf(DateTimeUtils.getTimeDiff(startdecrypt)));

        return true;
    }

    public static boolean initDestFolder(String newpath, boolean createNomedia) {
        File fil = new File(newpath);
        boolean pathExists = fil.exists() || fil.mkdirs();
        System.out.println("decrypt: pathExists = " + pathExists);
        if (!pathExists) {
            return false;
        }
        if (createNomedia) {
            File NoMedia = new File(newpath + ".nomedia");
            if (!NoMedia.exists()) {
                try {
                    NoMedia.createNewFile();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
        return true;
    }

    public static byte[] decryptToByteArray(byte[] password, String path, String newpath, String name) throws IOException, NoSuchAlgorithmException,
            NoSuchPaddingException, InvalidKeyException, InvalidAlgorithmParameterException {
        long startTime = System.currentTimeMillis();
        System.out.println("Decryption Started " + path);
        File fil = new File(newpath);
        boolean pathExists = fil.exists() || fil.mkdirs();
        System.out.println("decrypt: pathExists = " + pathExists);
        if (!pathExists) {
            return null;
        }
        File NoMedia = new File(newpath + ".nomedia");
        if (!NoMedia.exists()) {
            try {
                NoMedia.createNewFile();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        FileInputStream fis = new FileInputStream(path);
        FileOutputStream fos = new FileOutputStream(newpath + name, false);
        SecretKeySpec sks = new SecretKeySpec(password, "CBC");
        // Create cipher
        Cipher cipher = Cipher.getInstance(CryptoFiles.ALGO_ENCRYPTOR);
        cipher.init(Cipher.DECRYPT_MODE, sks, new IvParameterSpec(new byte[16]));
        CipherInputStream cis = new CipherInputStream(fis, cipher);

        int b;
        byte[] d = new byte[1024 * 1024];
        while ((b = cis.read(d)) != -1) {
            fos.write(d, 0, b);
        }
        ByteArrayOutputStream baos = null;
        try {
            baos = new ByteArrayOutputStream();
            baos.writeTo(fos);
        } catch (IOException ioe) {
            // Handle exception here
            ioe.printStackTrace();
        } finally {
            fos.close();
        }
        long stopTime = System.currentTimeMillis();
        System.out.println("Decryption Ended " + newpath + name + " length:" + new File(newpath + name).length());
        System.out.println("Time Elapsed " + ((stopTime - startTime) / 1000.0) + "");
        fos.flush();
        fos.close();
        cis.close();
        return baos.toByteArray();
    }

}

package com.arny.arnylib.security;

import com.arny.arnylib.utils.DateTimeUtils;
import javax.crypto.*;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import java.io.*;
import java.security.*;

public class CryptoFiles {

    private static final String ALGO_ENCRYPTOR = "AES/CBC/PKCS5Padding";
    private static final int BUFF_SIZE = 1024 * 1024;
    private static final int BYTE_CNT = 8;
    private static String INITIALIZATIO_VECTOR = "AODVNUASDNVVAOVF";

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

    public static String convertToHex(File file) throws IOException {
        InputStream is = new FileInputStream(file);
        int bytesCounter = 0;
        int value = 0;
        StringBuilder sbHex = new StringBuilder();
        StringBuilder sbText = new StringBuilder();
        StringBuilder sbResult = new StringBuilder();
        while ((value = is.read()) != -1) {
            //convert to hex value with "X" formatter
            sbHex.append(String.format("%02X ", value));
            //If the chracater is not convertable, just print a dot symbol "."
            if (!Character.isISOControl(value)) {
                sbText.append((char) value);
            } else {
                sbText.append(".");
            }
            //if 16 bytes are read, reset the counter,
            //clear the StringBuilder for formatting purpose only.
            if (bytesCounter == 15) {
                sbResult.append(sbHex).append("      ").append(sbText).append("\n");
                sbHex.setLength(0);
                sbText.setLength(0);
                bytesCounter = 0;
            } else {
                bytesCounter++;
            }
        }
        //if still got content
        if (bytesCounter != 0) {
            //add spaces more formatting purpose only
            for (; bytesCounter < 16; bytesCounter++) {
                //1 character 3 spaces
                sbHex.append("   ");
            }
            sbResult.append(sbHex).append("      ").append(sbText).append("\n");
        }
        is.close();
        return sbResult.toString();
    }

    public static String byteArrayToHexString(byte[] b) {
        StringBuffer sb = new StringBuffer(b.length * 2);
        for (int i = 0; i < b.length; i++) {
            int v = b[i] & 0xff;
            if (v < 16) {
                sb.append('0');
            }
            sb.append(Integer.toHexString(v));
        }
        return sb.toString().toUpperCase();
    }

    public static byte[] hexStringToByteArray(String s) {
        byte[] b = new byte[s.length() / 2];
        for (int i = 0; i < b.length; i++) {
            int index = i * 2;
            int v = Integer.parseInt(s.substring(index, index + 2), 16);
            b[i] = (byte) v;
        }
        return b;
    }

    public static String stringToHex(String input) {
        if (input == null) throw new NullPointerException();
        return asHex(input.getBytes());
    }

    public static String hexToString(String txtInHex) {
        byte[] txtInByte = new byte[txtInHex.length() / 2];
        int j = 0;
        for (int i = 0; i < txtInHex.length(); i += 2) {
            txtInByte[j++] = Byte.parseByte(txtInHex.substring(i, i + 2), 16);
        }
        return new String(txtInByte);
    }

    private static String asHex(byte[] buf) {
        char[] hexChars = "0123456789abcdef".toCharArray();
        char[] chars = new char[2 * buf.length];
        for (int i = 0; i < buf.length; ++i) {
            chars[2 * i] = hexChars[(buf[i] & 0xF0) >>> 4];
            chars[2 * i + 1] = hexChars[buf[i] & 0x0F];
        }
        return new String(chars);
    }

    public static String generateHexKey(String password) {
        try {
            byte[] keyStart = password.getBytes("UTF-8");
            KeyGenerator keyGen = KeyGenerator.getInstance("AES");
            SecureRandom sr = SecureRandom.getInstance("SHA1PRNG");
            sr.setSeed(keyStart);
            keyGen.init(128, sr);
            SecretKey sk = keyGen.generateKey();
            return byteArrayToHexString(sk.getEncoded());
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "";
    }

    public static byte[] generateByteKey(String password) throws Exception {
        byte[] keyStart = password.getBytes("UTF-8");
        KeyGenerator kgen = KeyGenerator.getInstance("AES");
        SecureRandom sr = SecureRandom.getInstance("SHA1PRNG");
        sr.setSeed(keyStart);
        kgen.init(128, sr);
        SecretKey skey = kgen.generateKey();
        return skey.getEncoded();
    }

    public static String encryptString(String hexKey, String toEncrypt) {
        try {
            byte[] bytekey = CryptoFiles.hexStringToByteArray(hexKey);
            SecretKeySpec sks = new SecretKeySpec(bytekey, "AES");
            Cipher cipher = Cipher.getInstance("AES");
            cipher.init(Cipher.ENCRYPT_MODE, sks, cipher.getParameters());
            byte[] encrypted = cipher.doFinal(toEncrypt.getBytes());
            return CryptoFiles.byteArrayToHexString(encrypted);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "";
    }

    public static String decryptString(String hexKey, String encrypted) {
        try {
            byte[] bytekey2 = hexStringToByteArray(hexKey);
            SecretKeySpec sks2 = new SecretKeySpec(bytekey2, "AES");
            Cipher cipher2 = Cipher.getInstance("AES");
            cipher2.init(Cipher.DECRYPT_MODE, sks2);
            byte[] decrypted = cipher2.doFinal(hexStringToByteArray(encrypted));
            return new String(decrypted);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "";
    }

    public static byte[] encryptStr(String plainText) throws Exception {
        Cipher cipher = Cipher.getInstance(ALGO_ENCRYPTOR);
        SecretKeySpec key = new SecretKeySpec(INITIALIZATIO_VECTOR.getBytes("UTF-8"), "AES");
        cipher.init(Cipher.ENCRYPT_MODE, key, new IvParameterSpec(INITIALIZATIO_VECTOR.getBytes("UTF-8")));
        return cipher.doFinal(plainText.getBytes("UTF-8"));
    }

    public static String decryptStr(byte[] cipherText) throws Exception {
        Cipher cipher = Cipher.getInstance(ALGO_ENCRYPTOR);
        SecretKeySpec key = new SecretKeySpec(INITIALIZATIO_VECTOR.getBytes("UTF-8"), "AES");
        cipher.init(Cipher.DECRYPT_MODE, key, new IvParameterSpec(INITIALIZATIO_VECTOR.getBytes("UTF-8")));
        return new String(cipher.doFinal(cipherText), "UTF-8");
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

    public static String getMD5String(String text) {
        try {
            MessageDigest digest = MessageDigest.getInstance("MD5");
            digest.update(text.getBytes());
            byte messageDigest[] = digest.digest();

            StringBuilder hexString = new StringBuilder();
            for (byte aMessageDigest : messageDigest)
                hexString.append(Integer.toHexString(0xFF & aMessageDigest));

            return hexString.toString();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        return "";
    }
}

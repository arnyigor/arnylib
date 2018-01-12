package com.arny.arnylib.security;

import org.apache.commons.codec.binary.Base64;
import org.bouncycastle.jce.provider.BouncyCastleProvider;
import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.security.*;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;

public class CryptoStrings {
    private static final String HEX = "0123456789ABCDEF";

    public static String getSecurityProviders() {
        Security.addProvider(new BouncyCastleProvider());
        StringBuilder result = new StringBuilder();
        Provider[] providers = Security.getProviders();
        for (int i = 0; i < providers.length; i++) {
            // Получим все сервисы для выбранного провайдера
            Set<Object> ks = providers[i].keySet();
            Set<String> servicetypes = new TreeSet<>();
            for (Object k2 : ks) {
                String k = k2.toString();
                k = k.split(" ")[0];
                if (k.startsWith("Alg.Alias."))
                    k = k.substring(10);
                servicetypes.add(k.substring(0, k.indexOf('.')));
            }
            // Получим все алгоритмы для выбранного типа сервиса
            int s = 1;
            for (String stype : servicetypes) {
                Set<String> algorithms = new TreeSet<>();
                for (Object k1 : ks) {
                    String k = k1.toString();
                    k = k.split(" ")[0];
                    if (k.startsWith(stype + "."))
                        algorithms.add(k.substring(stype.length() + 1));
                    else if (k.startsWith("Alg.Alias." + stype + "."))
                        algorithms.add(k.substring(stype.length() + 11));
                }
                int a = 1;
                for (String algorithm : algorithms) {
                    result.append("[П#").append(i + 1).append(":").append(providers[i].getName()).append("]")
                            .append("[С#").append(s).append(":").append(stype).append("]").append("[А#").append(a)
                            .append(":").append(algorithm).append("]\n");
                    a++;
                }
                s++;
            }
        }
        return result.toString();
    }

    public static String toHex(String txt) {
        return toHex(txt.getBytes());
    }

    public static String fromHex(String hex) {
        return new String(toByte(hex));
    }

    public static byte[] toByte(String hexString) {
        int len = hexString.length() / 2;
        byte[] result = new byte[len];
        for (int i = 0; i < len; i++) {
            result[i] = Integer.valueOf(hexString.substring(i * 2, (i * 2) + 2), 16).byteValue();
        }
        return result;
    }

    public static String toHex(byte[] buf) {
        if (buf == null) {
            return "";
        }
        StringBuffer result = new StringBuffer(buf.length * 2);
        for (byte appendHex : buf) {
            appendHex(result, appendHex);
        }
        return result.toString();
    }

    private static byte[] getRawKey(byte[] seed, String algorithm) throws Exception {
        return new SecretKeySpec(MessageDigest.getInstance("MD5").digest(seed), algorithm).getEncoded();
    }

    private static void appendHex(StringBuffer sb, byte b) {
        sb.append(HEX.charAt((b >> 4) & 15)).append(HEX.charAt(b & 15));
    }

    public static Map<String, Object> getRSAKeys() throws Exception {
        KeyPairGenerator keyPairGenerator = KeyPairGenerator.getInstance("RSA");
        keyPairGenerator.initialize(2048);
        KeyPair keyPair = keyPairGenerator.generateKeyPair();
        PrivateKey privateKey = keyPair.getPrivate();
        PublicKey publicKey = keyPair.getPublic();
        Map<String, Object> keys = new HashMap<>();
        keys.put("private", privateKey);
        keys.put("public", publicKey);
        return keys;
    }

    public static String decryptMessage(String encryptedText, PublicKey publicKey) throws Exception {
        Cipher cipher = Cipher.getInstance("RSA");
        cipher.init(Cipher.DECRYPT_MODE, publicKey);
        return new String(cipher.doFinal(Base64.decodeBase64(encryptedText)));
    }

    public static String encryptMessage(String plainText, PrivateKey privateKey) throws Exception {
        Cipher cipher = Cipher.getInstance("RSA");
        cipher.init(Cipher.ENCRYPT_MODE, privateKey);
        return Base64.encodeBase64String(cipher.doFinal(plainText.getBytes()));
    }

    public static String getHexString(String text, String algorithm) {
        try {
            MessageDigest digest = MessageDigest.getInstance(algorithm);
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

    public static String generateHexKey(String password, String algorithm) {
        try {
            Security.addProvider(new BouncyCastleProvider());
            byte[] keyStart = password.getBytes("UTF-8");
            KeyGenerator keyGen = KeyGenerator.getInstance(algorithm);
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

    public static String encryptBase64(String cleartext) {
        return Base64.encodeBase64String(cleartext.getBytes());
    }

    public static String decryptBase64(String encrypted) {
        return new String(Base64.decodeBase64(encrypted));
    }

    public static String encrypt(String seed, String toEncrypt) {
        try {
            String algorithm = "AES";
            return toHex(encrypt(getRawKey(seed.getBytes(), algorithm), toEncrypt.getBytes(), algorithm));
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "";
    }

    public static String decrypt(String seed, String encrypted) {
        try {
            String algorithm = "AES";
            return new String(decrypt(getRawKey(seed.getBytes(), algorithm), toByte(encrypted), algorithm));
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "";
    }

    private static byte[] encrypt(byte[] raw, byte[] clear, String algorithm) throws Exception {
        Cipher cipher = Cipher.getInstance(algorithm);
        SecretKeySpec skeySpec = new SecretKeySpec(raw, algorithm);
        cipher.init(Cipher.DECRYPT_MODE, skeySpec);
        return cipher.doFinal(clear);
    }

    private static byte[] decrypt(byte[] raw, byte[] encrypted, String algorithm) throws Exception {
        SecretKeySpec skeySpec = new SecretKeySpec(raw, algorithm);
        Cipher cipher = Cipher.getInstance(algorithm);
        cipher.init(Cipher.DECRYPT_MODE, skeySpec);
        return cipher.doFinal(encrypted);
    }

    public static String encryptByHexKey(String pass, String toEncrypt) {
        try {
            String transformation = "AES/CBC/PKCS5Padding";
            byte[] bytes = hexStringToByteArray(generateHexKey(pass, "BLOWFISH"));
            Cipher cipher = Cipher.getInstance(transformation, BouncyCastleProvider.PROVIDER_NAME);
            SecretKeySpec spec = new SecretKeySpec(bytes, transformation);
            cipher.init(Cipher.ENCRYPT_MODE, spec,new IvParameterSpec(bytes));
            return byteArrayToHexString(cipher.doFinal(toEncrypt.getBytes()));
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "";
    }

    public static String decryptByHexKey(String pass, String encrypted) {
        try {
            String algorithm = "AES/CBC/PKCS5Padding";
            byte[] bytes = hexStringToByteArray(generateHexKey(pass, "BLOWFISH"));
            Cipher cipher = Cipher.getInstance(algorithm, BouncyCastleProvider.PROVIDER_NAME);
            SecretKeySpec spec = new SecretKeySpec(bytes, algorithm);
            cipher.init(Cipher.DECRYPT_MODE, spec, new IvParameterSpec(bytes));
            return new String(cipher.doFinal(hexStringToByteArray(encrypted)));
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "";
    }
}

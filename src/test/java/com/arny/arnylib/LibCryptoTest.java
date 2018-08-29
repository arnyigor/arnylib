package com.arny.arnylib;

import com.arny.arnylib.security.CryptoStrings;
import com.arny.arnylib.utils.Stopwatch;
import org.junit.Before;
import org.junit.Test;

import java.security.PrivateKey;
import java.security.PublicKey;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
public class LibCryptoTest {

    private String input;

    @Before
    public void init() throws Exception {
        input = "123456789";
        System.out.println(input);
    }

    //    @Test
    public void base64() throws Exception {
        String encode = CryptoStrings.encryptBase64(input);
        System.out.println(encode);
        assertThat(encode).isNotNull();
        String decode = CryptoStrings.decryptBase64(encode);
        System.out.println(decode);
        assertThat(decode).isNotNull();
    }

    @Test
    public void criptoStrings() throws Exception {
        Stopwatch stopwatch = new Stopwatch();
        stopwatch.start();
        String encode = CryptoStrings.encrypt(input, input);
        System.out.println("criptoStrings  time to encode: " + stopwatch.getElapsedTimeMili());
        assertThat(encode).isNotNull();
        stopwatch.restart();
        String decode = CryptoStrings.decrypt(input, encode);
        System.out.println("criptoStrings decode time:" + stopwatch.getElapsedTimeMili());
        assertThat(decode).isNotNull();
    }

    @Test
    public void providers() throws Exception {
        String s = input = CryptoStrings.getSecurityProviders();
        System.out.println(s);
    }

    @Test
    public void criptoByHex() throws Exception {
        Stopwatch stopwatch = new Stopwatch();
        stopwatch.start();
        String res = null;
        String encode = CryptoStrings.encryptByHexKey(input, input);
        System.out.println("criptoByHex encode:" + encode + " time to encode: " + stopwatch.getElapsedTimeMili());
        stopwatch.restart();
        String decode = CryptoStrings.decryptByHexKey(input, encode);
        System.out.println("criptoByHex decode:" + decode + " decode time:" + stopwatch.getElapsedTimeMili());
        res = "ok";
        assertThat(res.equals("ok"));
    }


    @Test
    public void criptoRSA() throws Exception {
        Stopwatch stopwatch = new Stopwatch();
        stopwatch.start();
        // Generate public and private keys using RSA
        Map<String, Object> keys = CryptoStrings.getRSAKeys();
        PrivateKey privateKey = (PrivateKey) keys.get("private");
        PublicKey publicKey = (PublicKey) keys.get("public");
        String encryptedText = CryptoStrings.encryptMessage(input, privateKey);
        String descryptedText = CryptoStrings.decryptMessage(encryptedText, publicKey);
        System.out.println("input:" + input);
        System.out.println("encrypted:" + encryptedText);
        System.out.println("decrypted:" + descryptedText);
        System.out.println(" time:" + stopwatch.getElapsedTimeMili());

    }

}
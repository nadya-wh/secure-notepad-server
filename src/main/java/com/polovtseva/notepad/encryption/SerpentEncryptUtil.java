package com.polovtseva.notepad.encryption;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.bouncycastle.jce.provider.BouncyCastleProvider;
import org.apache.commons.codec.binary.Base64;


import javax.crypto.*;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import java.io.UnsupportedEncodingException;
import java.math.BigInteger;
import java.security.*;
import java.security.spec.AlgorithmParameterSpec;

/**
 * Created by nadez on 9/24/2016.
 */
public class SerpentEncryptUtil {

    private static final Logger logger = LogManager.getLogger(SerpentEncryptUtil.class);


    //    public static final String SERPENT_CIPHER_MODE = "Serpent/CTR/NOPADDING";
    public static final String SERPENT_CIPHER_MODE = "AES";
    //public static final String SERPENT_CIPHER_NAME = "Serpent";
    public static final String SERPENT_CIPHER_NAME = "AES";
    public static final String ENCODING = "UTF-8";

    public SerpentEncryptUtil() {
        Security.insertProviderAt(new BouncyCastleProvider(), 1);
    }

    public static String encrypt(Key key, String initVector, String value) {
        try {
            IvParameterSpec iv = new IvParameterSpec(initVector.getBytes(ENCODING));
            //SecretKeySpec skeySpec = new SecretKeySpec(key.getBytes(ENCODING), cipherName);

            Cipher cipher = Cipher.getInstance(SERPENT_CIPHER_MODE);
            cipher.init(Cipher.ENCRYPT_MODE, key, iv);

            byte[] encrypted = cipher.doFinal(value.getBytes());
            return Base64.encodeBase64String(encrypted);
        } catch (Exception ex) {
            logger.error(ex);
        }
        return null;
    }

    public static String decrypt(Key secretKey, String initVector, String encrypted) {
        try {
            IvParameterSpec iv = new IvParameterSpec(initVector.getBytes(ENCODING));
            //SecretKey secretKey = new SecretKeySpec(key.getBytes(ENCODING), cipherName);
            Cipher cipher = Cipher.getInstance(SERPENT_CIPHER_MODE);
            cipher.init(Cipher.DECRYPT_MODE, secretKey, iv);
            byte[] original = cipher.doFinal(Base64.decodeBase64(encrypted));
            return new String(original);
        } catch (Exception ex) {
            logger.error(ex);
        }
        return null;
    }

    public static SecretKeySpec buildSpecKey(String key, String cipherName) {
        SecretKeySpec skeySpec = null;
        try {
            skeySpec = new SecretKeySpec(key.getBytes(ENCODING), cipherName);
        } catch (UnsupportedEncodingException e) {
            logger.error(e);
        }
        return skeySpec;
    }

}

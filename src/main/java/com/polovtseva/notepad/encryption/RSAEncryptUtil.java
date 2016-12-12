package com.polovtseva.notepad.encryption;

import com.polovtseva.notepad.utils.BytesToString;
import org.apache.commons.codec.binary.Base64;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.crypto.Cipher;
import java.io.*;
import java.security.*;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.X509EncodedKeySpec;

/**
 * Created by nadez on 9/24/2016.
 */
public class RSAEncryptUtil {

    private static final Logger logger = LogManager.getLogger(RSAEncryptUtil.class);

    public static final String ALGORITHM = "RSA";
    public static final String ALGORITHM_MODE = "RSA/ECB/PKCS1Padding";
    public static final String ENCODING = "UTF8";

    public static final String PUBLIC_KEY_FILENAME = "publick";
    public static final String PRIVATE_KEY_FILENAME = "privatek";

    private PrivateKey privateKey;

    public PrivateKey getPrivateKey() {
        return privateKey;
    }

    public PublicKey generateKey() {
        try {
            KeyPairGenerator keyGen = KeyPairGenerator.getInstance(ALGORITHM, "BC");
            keyGen.initialize(1024);
            KeyPair key = keyGen.generateKeyPair();
            privateKey = key.getPrivate();
            return key.getPublic();
        } catch (NoSuchAlgorithmException e) {
            logger.error(e);
        } catch (NoSuchProviderException e) {
            logger.error(e);
        }
        return null;
    }

    public static String takePublicKey(PublicKey publicKey) {
        return Base64.encodeBase64String(publicKey.getEncoded());
    }

    public String takePrivateKey() {
        return Base64.encodeBase64String(privateKey.getEncoded());
    }

    public static PublicKey countPublicKeyFromString(String key) {
        byte[] publicBytes = Base64.decodeBase64(key.getBytes());
        X509EncodedKeySpec keySpec = new X509EncodedKeySpec(publicBytes);
        KeyFactory keyFactory;
        try {
            keyFactory = KeyFactory.getInstance("RSA");
            PublicKey pubKey = keyFactory.generatePublic(keySpec);
            return pubKey;
        } catch (NoSuchAlgorithmException e) {
            logger.error(e);
        } catch (InvalidKeySpecException e) {
            logger.error(e);
        }
        return null;
    }

    public static byte[] encrypt(byte[] text, PublicKey key) throws Exception {
        byte[] cipherText;
        Cipher cipher = Cipher.getInstance(ALGORITHM_MODE);
        cipher.init(Cipher.ENCRYPT_MODE, key);
        cipherText = cipher.doFinal(text);
        return cipherText;
    }

    public static String encrypt(String text, PublicKey key) {
        String encryptedText;
        byte[] cipherText;
        try {
            cipherText = encrypt(text.getBytes(ENCODING), key);
        } catch (Exception e) {
            logger.error(e);
            return null;
        }
        encryptedText = BytesToString.encodeBASE64(cipherText);
        return encryptedText;
    }

    public static byte[] decrypt(byte[] text, PrivateKey key) throws Exception {
        byte[] decryptedText;
        Cipher cipher = Cipher.getInstance(ALGORITHM_MODE);
        cipher.init(Cipher.DECRYPT_MODE, key);
        decryptedText = cipher.doFinal(text);
        return decryptedText;

    }

    public static String decrypt(String text, PrivateKey key) {
        try {
            byte[] decryptedText = decrypt(BytesToString.decodeBASE64(text), key);
            return new String(decryptedText, ENCODING);

        } catch (Exception e) {
            logger.error(e);
        }
        return null;
    }


    public static void saveKey(Key key, String filename) {
        try (FileOutputStream fileOut =
                     new FileOutputStream(filename);
             ObjectOutputStream out = new ObjectOutputStream(fileOut)) {
            out.writeObject(key);
        } catch (IOException i) {
            logger.error(i);
        }
    }

    /*public PrivateKey restoreKey() {
        try (FileInputStream fileIn = new FileInputStream(PUBLIC_KEY_FILENAME);
             ObjectInputStream in = new ObjectInputStream(fileIn);) {
            privateKey = (PrivateKey) in.readObject();
            System.out.println("Key restored!");
            return privateKey;
        } catch (IOException i) {
            logger.error(i);
        } catch (ClassNotFoundException e) {
            logger.error(e);
        }
        return null;
    }*/

    public static Key restoreKey(String filename) {
        try (FileInputStream fileIn = new FileInputStream(filename);
             ObjectInputStream in = new ObjectInputStream(fileIn);) {
            Key key = (Key) in.readObject();
            System.out.println("Key restored!");
            return key;
        } catch (IOException i) {
            logger.error(i);
        } catch (ClassNotFoundException e) {
            logger.error(e);
        }
        return null;
    }

}

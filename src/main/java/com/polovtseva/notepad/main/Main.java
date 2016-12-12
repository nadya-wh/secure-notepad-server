package com.polovtseva.notepad.main;


import com.polovtseva.notepad.encryption.RSAEncryptUtil;
import com.polovtseva.notepad.signature.SignatureUtil;
import com.polovtseva.notepad.utils.BytesToString;

import java.security.*;

/**
 * Created by nadez on 9/21/2016.
 */
public class Main {
    public static void main(String[] args) {
//        SerpentEncryptUtil encryptor = new SerpentEncryptUtil();
//        String initVector = "RandomInitVector"; // 16 bytes IV
//        String sessionKey = KeyBuilder.generateSessionKey();
//        SecretKey secretKey = SerpentEncryptUtil.buildSpecKey(sessionKey, SerpentEncryptUtil.SERPENT_CIPHER_NAME);
//
//
//        String encryptedString = encryptor.encrypt(secretKey, initVector, "Hello World");
//        System.out.println(encryptor.decrypt(secretKey, initVector, encryptedString));
//        RSAEncryptUtil rsa = new RSAEncryptUtil();
//        rsa.generateKey();
//        rsa.saveKey(rsa.getKeyPair().getPrivate());
//
//        System.out.println(rsa.restoreKey());
//        System.out.println(rsa.takePublicKey());
//        System.out.println(rsa.takePrivateKey());
//        String rsaEncrypted = null;
//        try {
//            rsaEncrypted = RSAEncryptUtil.encrypt("heeeeeeeeeee", rsa.getKeyPair().getPublic());
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//        System.out.println("RSAEncryptUtil: " + rsaEncrypted);
//        try {
//            String rsaDecrypted = RSAEncryptUtil.decrypt(rsaEncrypted, rsa.getKeyPair().getPrivate());
//            System.out.println("RSAEncryptUtil: " + rsaDecrypted);
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//
//        String publicKeyAsString = rsa.takePublicKey();
//        PublicKey publicKey = rsa.countPublicKeyFromString(publicKeyAsString);
//        System.out.println(publicKey.equals(rsa.getKeyPair().getPublic()));
//        try {
//            System.out.println(FileDAOImpl.getInstance().read("1"));
//            System.out.println(UserDAOImpl.getInstance().find("1", "1", "1"));
//        } catch (DAOException e) {
//            e.printStackTrace();
//        }
        Security.addProvider(new org.bouncycastle.jce.provider.BouncyCastleProvider());
        /*RSAEncryptUtil rsaEncryptUtil = new RSAEncryptUtil();
        PublicKey publicKey = rsaEncryptUtil.generateKey();
        PrivateKey privateKey = rsaEncryptUtil.getPrivateKey();
        String data = "1111";
        byte[] signature = SignatureUtil.sign(data, privateKey);
        String s = BytesToString.encodeBASE64(signature);
        boolean res = SignatureUtil.verify(data, BytesToString.decodeBASE64(s), publicKey);
        System.out.println("Signature: " + signature + "\nVerification: " + res);
        */
        /*try {

            KeyPairGenerator keyGen = KeyPairGenerator.getInstance("RSA", "BC");

            keyGen.initialize(512, new SecureRandom());

            KeyPair keyPair = keyGen.generateKeyPair();
            Signature signature = Signature.getInstance("SHA1withRSA", "BC");

            signature.initSign(keyPair.getPrivate(), new SecureRandom());

            byte[] message = "abc".getBytes();
            signature.update(message);

            byte[] sigBytes = signature.sign();
            signature.initVerify(keyPair.getPublic());
            signature.update(message);
            System.out.println(signature.verify(sigBytes));
        } catch (Exception e) {
            e.printStackTrace();
        }*/
    }
}

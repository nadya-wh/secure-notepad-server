package com.polovtseva.notepad.encryption;

import java.io.*;
import java.security.*;
import java.security.cert.*;

/**
 * Created by Nadzeya_Polautsava on 11/19/2016.
 */
public class KeyStoreUtil {
    private static String keyStore = "KeyStore.jks";
    private static ClassLoader loader = ClassLoader.getSystemClassLoader();

    private static File file = new File(keyStore);

    public static void writeEntry(String alias, PrivateKey key, String password)
            throws KeyStoreException,
            CertificateException,
            UnrecoverableEntryException {
        KeyStore ks = loadKs();
        KeyStore.PrivateKeyEntry prEntry =
                new KeyStore.PrivateKeyEntry(key, new java.security.cert.Certificate[]{ks.getCertificate("root")});
        ks.setEntry(alias, prEntry, new KeyStore.PasswordProtection(password.toCharArray()));
        saveKs(ks);
    }

    public static PrivateKey readEntry(String alias, String password)
            throws KeyStoreException,
            CertificateException,
            NoSuchAlgorithmException,
            UnrecoverableEntryException {
        KeyStore ks = loadKs();
        KeyStore.PrivateKeyEntry pkEntry = (KeyStore.PrivateKeyEntry)
                ks.getEntry(alias, new KeyStore.PasswordProtection(password.toCharArray()));
        return pkEntry.getPrivateKey();
    }

    private static KeyStore loadKs() throws KeyStoreException, CertificateException {
        KeyStore ks = KeyStore.getInstance(KeyStore.getDefaultType());

        try (FileInputStream fis = new FileInputStream(file)) {

            ks.load(fis, "mydomain".toCharArray());
        } catch (IOException ignored) {
            ignored.printStackTrace();
        } catch (NoSuchAlgorithmException ignored) {
            ignored.printStackTrace();
        }
        return ks;
    }

    private static void saveKs(KeyStore ks) throws CertificateException, KeyStoreException {
        FileOutputStream fos = null;
        try {
            fos = new FileOutputStream(file);
            ks.store(fos, "mydomain".toCharArray());
        } catch (FileNotFoundException ignored) {
        } catch (IOException ignored) {
        } catch (NoSuchAlgorithmException ignored) {

        } finally {
            try {
                fos.close();
            } catch (IOException ignored) {
            }
        }
    }
}

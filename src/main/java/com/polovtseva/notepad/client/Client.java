package com.polovtseva.notepad.client;

import com.polovtseva.notepad.encryption.RSAEncryptUtil;
import com.polovtseva.notepad.encryption.SerpentEncryptUtil;
import com.polovtseva.notepad.protocol.Protocol;
import org.bouncycastle.jce.provider.BouncyCastleProvider;

import javax.crypto.SecretKey;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.UnknownHostException;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.Security;

/**
 * Created by nadez on 9/27/2016.
 */
public class Client {

    private static final String INIT_VECTOR = "initVector";

    public static final String MENU = "1) Send public key;\n" +
            "2) Receive file;\n" +
            "3) Stop";

    public static void main(String[] args) {

        Security.addProvider(new BouncyCastleProvider());
        RSAEncryptUtil rsaEncryptUtil = new RSAEncryptUtil();


        //PublicKey publicKey = rsaEncryptUtil.generateKey();
        PrivateKey privateKey = (PrivateKey) rsaEncryptUtil.restoreKey(RSAEncryptUtil.PRIVATE_KEY_FILENAME);

        rsaEncryptUtil.saveKey(rsaEncryptUtil.getPrivateKey(), RSAEncryptUtil.PRIVATE_KEY_FILENAME);

        System.out.println(privateKey);

        try (Socket socket = new Socket(Protocol.HOSTNAME, Protocol.PORT);
             PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
             BufferedReader in = new BufferedReader(
                     new InputStreamReader(socket.getInputStream()));
             BufferedReader standardInput = new BufferedReader(new InputStreamReader(System.in))) {
            if (rsaEncryptUtil.getPrivateKey() == null) {
                PublicKey publicKey = rsaEncryptUtil.generateKey();
                rsaEncryptUtil.saveKey(rsaEncryptUtil.getPrivateKey(), RSAEncryptUtil.PRIVATE_KEY_FILENAME);
                System.out.println("Generated private: " + rsaEncryptUtil.getPrivateKey());
                out.println(Protocol.CHANGE_PUBLIC_KEY + " " + RSAEncryptUtil.takePublicKey(publicKey));
            }
            out.println(Protocol.GENERATE_SESSION_KEY);
            String encryptedKey = in.readLine();
            System.out.println("Encrypted key: " + encryptedKey);
            String sessionKey = RSAEncryptUtil.decrypt(encryptedKey, rsaEncryptUtil.getPrivateKey());
            System.out.println("Connected, session key received: " + sessionKey);
            SecretKey serpentSecretKey = SerpentEncryptUtil.buildSpecKey(sessionKey, SerpentEncryptUtil.SERPENT_CIPHER_NAME);

            boolean stop = false;
            while (!stop) {
                System.out.println(MENU);

                Integer choice = Integer.parseInt(standardInput.readLine());
                switch (choice) {
                    case 1:
                        PublicKey publicKey = rsaEncryptUtil.generateKey();
                        rsaEncryptUtil.saveKey(rsaEncryptUtil.getPrivateKey(), RSAEncryptUtil.PRIVATE_KEY_FILENAME);
                        out.println(Protocol.CHANGE_PUBLIC_KEY + " " + RSAEncryptUtil.takePublicKey(publicKey));
                        break;
                    case 2:
                        System.out.println("Enter filename: ");
                        String filename = standardInput.readLine();
                        out.println(Protocol.GET_FILE + " " + filename + " " + sessionKey);
                        String encryptedFile = in.readLine();
                        String file = SerpentEncryptUtil.decrypt(serpentSecretKey, INIT_VECTOR, encryptedFile);
                        System.out.println(file);
                        break;
                    case 3:
                        stop = true;
                        break;
                }
            }
        } catch (UnknownHostException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}

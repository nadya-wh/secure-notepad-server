package com.polovtseva.notepad.server;

import com.polovtseva.notepad.dao.exception.DAOException;
import com.polovtseva.notepad.dao.impl.FileDAOImpl;
import com.polovtseva.notepad.encryption.KeyBuilder;
import com.polovtseva.notepad.encryption.RSAEncryptUtil;
import com.polovtseva.notepad.encryption.SerpentEncryptUtil;
import com.polovtseva.notepad.model.File;
import com.polovtseva.notepad.protocol.Protocol;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.crypto.SecretKey;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.security.PublicKey;

/**
 * Created by nadez on 9/24/2016.
 */
public class Server {

    private static final Logger logger = LogManager.getLogger(Server.class);
    private static final String INIT_VECTOR = "initVector";


    public static final String PUBLIC_KEY_FILENAME = "publicKey.txt";


    private PublicKey publicKey;

    public Server() {
        publicKey = (PublicKey) RSAEncryptUtil.restoreKey(RSAEncryptUtil.PUBLIC_KEY_FILENAME);
    }

    public void run() {
        try (
                ServerSocket serverSocket = new ServerSocket(Protocol.PORT);
                Socket clientSocket = serverSocket.accept();
                PrintWriter out =
                        new PrintWriter(clientSocket.getOutputStream(), true);
                BufferedReader in = new BufferedReader(
                        new InputStreamReader(clientSocket.getInputStream()));
        ) {
            String input;
            while ((input = in.readLine()) != null) {
                String[] inputs = input.split(" ");
                String choice = inputs[0];
                switch (choice) {
                    case Protocol.CHANGE_PUBLIC_KEY:
                        savePublicKey(inputs[1]);
                        break;
                    case Protocol.GET_FILE:
                        sendEncryptedFile(inputs[1], inputs[2], out);
                        break;
                    case Protocol.SAVE_FILE:
                        break;
                    case Protocol.GENERATE_SESSION_KEY:
                        sendSessionKey(out);
                        break;

                }
            }
        } catch (IOException e) {
            logger.error(e);
        }
    }

    public void savePublicKey(String key) {
        System.out.println("Saving key!!!");
        publicKey = RSAEncryptUtil.countPublicKeyFromString(key);
        RSAEncryptUtil.saveKey(publicKey, RSAEncryptUtil.PUBLIC_KEY_FILENAME);
    }

    public void saveFile(File file) {

    }

    public void sendEncryptedFile(String filename, String sessionKey, PrintWriter out) {
        String data;
        try {
            File file = FileDAOImpl.getInstance().read(filename);
            data = file.getValue();
        } catch (DAOException e) {
            data = "Internal server error.";
            logger.error(e);
        }
        SecretKey secretKey = SerpentEncryptUtil.buildSpecKey(sessionKey, SerpentEncryptUtil.SERPENT_CIPHER_NAME);
        SerpentEncryptUtil encryptor = new SerpentEncryptUtil();
        String fileEncrypted = encryptor.encrypt(secretKey, INIT_VECTOR, data);
        out.println(fileEncrypted);
    }

    public void sendSessionKey(PrintWriter out) {
        String sessionKey = KeyBuilder.generateSessionKey();
        String encryptedSessionKey = RSAEncryptUtil.encrypt(sessionKey, publicKey);
        System.out.println("SessionKeyWrapper key: " + sessionKey);
        System.out.println("Encrypted key: " + encryptedSessionKey);
        out.println(encryptedSessionKey);
    }
}

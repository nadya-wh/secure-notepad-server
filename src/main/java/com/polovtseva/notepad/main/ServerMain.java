package com.polovtseva.notepad.main;

import com.polovtseva.notepad.server.Server;
import org.bouncycastle.jce.provider.BouncyCastleProvider;

import java.security.Security;

/**
 * Created by nadez on 9/27/2016.
 */
public class ServerMain {
    public static void main(String[] args) {
        Security.addProvider(new BouncyCastleProvider());
        Server server = new Server();
        server.run();
    }
}

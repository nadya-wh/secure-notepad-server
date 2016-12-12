package com.polovtseva.notepad.encryption;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.crypto.spec.SecretKeySpec;
import java.io.UnsupportedEncodingException;
import java.math.BigInteger;
import java.security.SecureRandom;

/**
 * Created by nadez on 9/25/2016.
 */
public class KeyBuilder {

    private static final SecureRandom random = new SecureRandom();

    public static String generateSessionKey() {
        return new BigInteger(130, random).toString(32).substring(0, 16);
    }
}

package com.polovtseva.notepad.signature;


import com.polovtseva.notepad.utils.BytesToString;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;

import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.SecureRandom;
import java.security.Signature;

/**
 * Created by Nadzeya_Polautsava on 10/22/2016.
 */

public class SignatureUtil {

    private static final Logger log = LogManager.getLogger(SignatureUtil.class);
    public static final String SIGNATURE_UTIL_TAG = "SignatureUtil";

    public static String sign(String toSign, PrivateKey privateKey) {
        try {
            Signature signature = Signature.getInstance("SHA1withRSA", "BC");

            signature.initSign(privateKey, new SecureRandom());

            byte[] message = toSign.getBytes();
            signature.update(message);
            byte[] sigBytes = signature.sign();
            return BytesToString.encodeBASE64(sigBytes);
        } catch (Exception e) {
            log.error(e);
        }
        return "";
    }

    public static boolean verify(String message, String signature, PublicKey publicKey) {
        try {
            Signature sig = Signature.getInstance("SHA1withRSA", "BC");
            sig.initVerify(publicKey);
            sig.update(message.getBytes());
            return sig.verify(BytesToString.decodeBASE64(signature));
        } catch (Exception e) {
            log.error(e);
        }
        return false;
    }
}

package com.polovtseva.notepad.utils;

import org.apache.commons.codec.binary.Base64;

/**
 * Created by Nadzeya_Polautsava on 10/23/2016.
 */
public class BytesToString {
    public static String encodeBASE64(byte[] bytes) {
        return Base64.encodeBase64String(bytes);
    }

    public static byte[] decodeBASE64(String text) {
        return Base64.decodeBase64(text);
    }
}

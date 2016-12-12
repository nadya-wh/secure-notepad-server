package com.polovtseva.notepad.service;

import com.polovtseva.notepad.model.FileKey;

/**
 * Created by Nadzeya_Polautsava on 11/19/2016.
 */
public interface FileKeyService {

    FileKey readKey(String filename, Long userId);
    void saveKey(String filename, Long userId, String key);
}

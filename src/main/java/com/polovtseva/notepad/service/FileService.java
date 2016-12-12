package com.polovtseva.notepad.service;

import com.polovtseva.notepad.model.File;
import com.polovtseva.notepad.service.exception.ServiceException;

/**
 * Created by nadez on 10/22/2016.
 */
public interface FileService {
    File find(String filename) throws ServiceException;

    void save(File file) throws ServiceException;
}

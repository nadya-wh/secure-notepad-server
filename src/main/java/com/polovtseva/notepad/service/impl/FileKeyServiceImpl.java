package com.polovtseva.notepad.service.impl;

import com.polovtseva.notepad.dao.FileKeyDAO;
import com.polovtseva.notepad.dao.exception.DAOException;
import com.polovtseva.notepad.dao.impl.FileKeyDAOImpl;
import com.polovtseva.notepad.model.File;
import com.polovtseva.notepad.model.FileKey;
import com.polovtseva.notepad.model.FileKeyPK;
import com.polovtseva.notepad.service.FileKeyService;
import com.polovtseva.notepad.service.FileService;
import com.polovtseva.notepad.service.exception.ServiceException;

/**
 * Created by Nadzeya_Polautsava on 11/19/2016.
 */
public class FileKeyServiceImpl implements FileKeyService {

    private FileKeyDAO fileKeyDAO;
    private FileService fileService;
    private static FileKeyServiceImpl instance = new FileKeyServiceImpl();

    public static FileKeyServiceImpl getInstance() {
        return instance;
    }

    public FileKeyServiceImpl(FileKeyDAO fileKeyDAO) {
        this.fileKeyDAO = fileKeyDAO;
    }


    public FileKeyServiceImpl() {
        this.fileKeyDAO = FileKeyDAOImpl.getInstance();
        this.fileService = FileServiceImpl.getInstance();
    }

    @Override
    public FileKey readKey(String filename, Long userId) {
        File file = fileService.find(filename);
        if (file != null) {
            FileKeyPK fileKeyPK = new FileKeyPK(userId, file.getFileId());
            try {
                return fileKeyDAO.read(fileKeyPK);
            } catch (DAOException e) {
                throw new ServiceException(e);
            }
        }
        return null;
    }

    @Override
    public void saveKey(String filename, Long userId, String key) {
        Long fileId = takeFileId(filename);
        try {
            fileKeyDAO.save(new FileKey(new FileKeyPK(userId, fileId), key));
        } catch (DAOException e) {
            throw new ServiceException(e);
        }
    }

    private Long takeFileId(String filename) {
        File file = fileService.find(filename);
        return file.getFileId();
    }
}

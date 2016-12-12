package com.polovtseva.notepad.service.impl;

import com.polovtseva.notepad.dao.FileDAO;
import com.polovtseva.notepad.dao.exception.DAOException;
import com.polovtseva.notepad.dao.impl.FileDAOImpl;
import com.polovtseva.notepad.encryption.KeyStoreUtil;
import com.polovtseva.notepad.encryption.RSAEncryptUtil;
import com.polovtseva.notepad.model.File;
import com.polovtseva.notepad.service.FileService;
import com.polovtseva.notepad.service.exception.ServiceException;

import java.security.KeyPair;
import java.security.KeyStore;
import java.security.PrivateKey;
import java.security.PublicKey;

/**
 * Created by nadez on 10/22/2016.
 */
public class FileServiceImpl implements FileService {

    private FileDAO fileDAO;
    private static FileServiceImpl instance = new FileServiceImpl();

    public static FileServiceImpl getInstance() {
        return instance;
    }

    public FileServiceImpl(FileDAO fileDAO) {
        this.fileDAO = fileDAO;
    }


    public FileServiceImpl() {
        this.fileDAO = FileDAOImpl.getInstance();
    }



    @Override
    public File find(String filename) throws ServiceException {
        try {
            File file =  fileDAO.read(filename);
            if (file.getEncrypted() != null && file.getEncrypted()) {
                PrivateKey privateKey = KeyStoreUtil.readEntry(filename, "mydomain");
                String decryptedText = RSAEncryptUtil.decrypt(file.getValue(), privateKey);
                file.setValue(decryptedText);
                file.setEncrypted(false);
            }
            return file;
        } catch (Exception e) {
            throw new ServiceException(e);
        }
    }

    @Override
    public void save(File file) throws ServiceException {
        try {
            File alreadyExists = find(file.getFilename());
            if (alreadyExists!= null) {
                file.setFileId(alreadyExists.getFileId());
            }
            RSAEncryptUtil rsaEncryptUtil = new RSAEncryptUtil();
            PublicKey publicKey = rsaEncryptUtil.generateKey();
            file.setValue(RSAEncryptUtil.encrypt(file.getValue(), publicKey));
            file.setPublicKey(RSAEncryptUtil.takePublicKey(publicKey));
            file.setEncrypted(true);
            KeyStoreUtil.writeEntry(file.getFilename(), rsaEncryptUtil.getPrivateKey(), "mydomain");
            fileDAO.save(file);
        } catch (Exception e) {
            throw new ServiceException(e);
        }
    }
}

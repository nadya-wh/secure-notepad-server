package com.polovtseva.notepad.dao.impl;

import com.polovtseva.notepad.dao.FileKeyDAO;
import com.polovtseva.notepad.dao.SessionWrapper;
import com.polovtseva.notepad.dao.exception.DAOException;
import com.polovtseva.notepad.model.FileKey;
import com.polovtseva.notepad.model.FileKeyPK;
import org.hibernate.Hibernate;
import org.hibernate.Session;

/**
 * Created by Nadzeya_Polautsava on 11/19/2016.
 */
public class FileKeyDAOImpl implements FileKeyDAO {

    private static final FileKeyDAOImpl instance = new FileKeyDAOImpl();


    public static FileKeyDAOImpl getInstance() {
        return instance;
    }

    @Override
    public FileKey read(FileKeyPK fileKeyPK) throws DAOException {
        Session session = null;
        FileKey  fileKey = null;
        try {
            session = SessionWrapper.getSessionFactory().openSession();
            fileKey = session.get(FileKey.class, fileKeyPK);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (session != null && session.isOpen()) {
                session.close();
            }
        }
        return fileKey;
    }

    @Override
    public FileKey read(String filename, Long userId) throws DAOException {
        return null;
    }
}

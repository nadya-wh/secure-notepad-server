package com.polovtseva.notepad.dao.impl;

import com.polovtseva.notepad.dao.FileDAO;
import com.polovtseva.notepad.dao.SessionWrapper;
import com.polovtseva.notepad.dao.exception.DAOException;
import com.polovtseva.notepad.model.File;
import com.polovtseva.notepad.model.User;
import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.query.Query;

import java.util.List;

/**
 * Created by nadez on 9/25/2016.
 */
public class FileDAOImpl implements FileDAO {
    private static final FileDAOImpl instance = new FileDAOImpl();

    private static final String FIND_FILE_BY_NAME = "from File where filename = :filename";

    public static FileDAOImpl getInstance() {
        return instance;
    }

    @Override
    public File read(String filename) throws DAOException {
        Session session = null;
        try {
            session = SessionWrapper.getSessionFactory().openSession();
            Query query = session.createQuery(FIND_FILE_BY_NAME);
            query.setParameter("filename", filename);
            List<File> files = query.list();
            if (files.size() == 1) {
                return files.get(0);
            }
        } catch (HibernateException e) {
            throw new DAOException(e);
        } finally {
            if (session != null) {
                session.close();
            }
        }
        return null;
    }
}

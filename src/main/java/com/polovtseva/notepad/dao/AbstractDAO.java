package com.polovtseva.notepad.dao;

import com.polovtseva.notepad.dao.exception.DAOException;
import org.hibernate.HibernateException;
import org.hibernate.Session;

/**
 * Created by nadez on 10/22/2016.
 */
public interface AbstractDAO<T> {

    default void save(T entity) throws DAOException {
        Session session = null;
        try  {
            session = SessionWrapper.getSessionFactory().openSession();
            session.beginTransaction();
            session.saveOrUpdate(entity);
            session.getTransaction().commit();
        }  catch (HibernateException e) {
            throw new DAOException(e);
        } finally {
            if (session != null) {
                session.close();
            }
        }
    }
}

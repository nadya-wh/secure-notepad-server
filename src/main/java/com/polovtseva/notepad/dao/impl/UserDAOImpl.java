package com.polovtseva.notepad.dao.impl;

import com.polovtseva.notepad.dao.SessionWrapper;
import com.polovtseva.notepad.dao.UserDAO;
import com.polovtseva.notepad.dao.exception.DAOException;
import com.polovtseva.notepad.model.User;
import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.query.Query;

import java.util.List;

/**
 * Created by nadez on 10/22/2016.
 */
public class UserDAOImpl implements UserDAO {

    private static final UserDAOImpl instance = new UserDAOImpl();

    public static UserDAOImpl getInstance() {
        return instance;
    }

    private static String FIND_USER = "from User where login = :login AND password = :password AND secretToken = :secretToken";

    private static String FIND_USER_LOGIN_PASSWORD = "from User where login = :login AND password = :password";

    private static String FIND_USER_LOGIN = "from User where login = :login";


    @Override
    public User find(String login, String password, String secretToken) throws DAOException {
        Session session = null;
        try {
            session = SessionWrapper.getSessionFactory().openSession();
            Query query = session.createQuery(FIND_USER);
            query.setParameter("login", login);
            query.setParameter("password", password);
            query.setParameter("secretToken", secretToken);
            List<User> userList = query.list();
            if (userList.size() == 1)
                return userList.get(0);
        } catch (HibernateException e) {
            throw new DAOException(e);
        } finally {
            if (session != null) {
                session.close();
            }
        }
        return null;
    }

    @Override
    public User find(String login, String password) throws DAOException {
        Session session = null;
        try {
            session = SessionWrapper.getSessionFactory().openSession();
            Query query = session.createQuery(FIND_USER_LOGIN_PASSWORD);
            query.setParameter("login", login);
            query.setParameter("password", password);
            List<User> userList = query.list();
            if (userList.size() == 1) {
                return userList.get(0);
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

    @Override
    public Boolean find(String login) throws DAOException {
        Session session = null;
        try {
            session = SessionWrapper.getSessionFactory().openSession();
            Query query = session.createQuery(FIND_USER_LOGIN);
            query.setParameter("login", login);
            List<User> userList = query.list();
            return userList.size() == 1;
        } catch (HibernateException e) {
            throw new DAOException(e);
        } finally {
            if (session != null) {
                session.close();
            }
        }
    }
}

package com.polovtseva.notepad.dao;

import com.polovtseva.notepad.dao.exception.DAOException;
import com.polovtseva.notepad.model.User;

/**
 * Created by nadez on 10/22/2016.
 */
public interface UserDAO extends AbstractDAO<User> {
    User find(String login, String password, String secretToken) throws DAOException;

    User find(String login, String password) throws DAOException;

    User find(String login) throws DAOException;
}

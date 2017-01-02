package com.polovtseva.notepad.service.impl;

import com.polovtseva.notepad.dao.UserDAO;
import com.polovtseva.notepad.dao.exception.DAOException;
import com.polovtseva.notepad.dao.impl.UserDAOImpl;
import com.polovtseva.notepad.model.User;
import com.polovtseva.notepad.service.UserService;
import com.polovtseva.notepad.service.exception.ServiceException;

/**
 * Created by nadez on 10/22/2016.
 */
public class UserServiceImpl implements UserService {

    private static final UserServiceImpl instance = new UserServiceImpl();
    private UserDAO userDAO;

    public UserServiceImpl(UserDAO userDAO) {
        this.userDAO = userDAO;
    }

    public UserServiceImpl() {
        this.userDAO = UserDAOImpl.getInstance();
    }

    public static UserServiceImpl getInstance() {
        return instance;
    }


    @Override
    public void save(User user) throws ServiceException {
        try {
            userDAO.save(user);
        } catch (DAOException e) {
            throw new ServiceException(e);
        }
    }

//    @Override
//    public User find(String login, String password, String secretToken) throws ServiceException {
//        try {
//            return userDAO.find(login, password, secretToken);
//        } catch (DAOException e) {
//            throw new ServiceException(e);
//        }
//    }

    @Override
    public User find(String login, String password) throws ServiceException {
        try {
            return userDAO.find(login, password);
        } catch (DAOException e) {
            throw new ServiceException(e);
        }
    }

    @Override
    public User find(String login) throws ServiceException {
        try {
            return userDAO.find(login);
        } catch (DAOException e) {
            throw new ServiceException(e);
        }
    }

}

package com.polovtseva.notepad.service;

import com.polovtseva.notepad.model.User;
import com.polovtseva.notepad.service.exception.ServiceException;
import com.sun.org.apache.xpath.internal.operations.Bool;

/**
 * Created by nadez on 10/22/2016.
 */
public interface UserService {

    void save(User user) throws ServiceException;

    User find(String login, String password, String secretToken) throws ServiceException;

    User find(String login, String password) throws ServiceException;

    Boolean find(String login) throws ServiceException;
}

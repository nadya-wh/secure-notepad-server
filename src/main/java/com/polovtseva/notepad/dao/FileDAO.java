package com.polovtseva.notepad.dao;

import com.polovtseva.notepad.dao.exception.DAOException;
import com.polovtseva.notepad.model.File;

/**
 * Created by nadez on 9/25/2016.
 */
public interface FileDAO extends AbstractDAO<File> {

    File read(String filename) throws DAOException;
}

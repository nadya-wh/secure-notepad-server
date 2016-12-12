package com.polovtseva.notepad.dao;

import com.polovtseva.notepad.dao.exception.DAOException;
import com.polovtseva.notepad.model.FileKey;
import com.polovtseva.notepad.model.FileKeyPK;

/**
 * Created by Nadzeya_Polautsava on 11/19/2016.
 */
public interface FileKeyDAO extends AbstractDAO<FileKey> {

    FileKey read(FileKeyPK fileKeyPK)throws DAOException;

    FileKey read(String filename, Long userId) throws DAOException;

}

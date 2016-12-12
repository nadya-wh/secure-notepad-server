package com.polovtseva.notepad.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import javax.persistence.*;

/**
 * Created by Nadzeya_Polautsava on 11/19/2016.
 */
@Entity
@Table(name = "files_users")
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
public class FileKey {


    @EmbeddedId
    private FileKeyPK fileKeyPK;
    @Column(name = "cipher_key")
    private String key;

    public FileKey() {
    }

    public FileKey(FileKeyPK fileKeyPK, String key) {
        this.fileKeyPK = fileKeyPK;
        this.key = key;
    }

    public FileKeyPK getFileKeyPK() {
        return fileKeyPK;
    }

    public void setFileKeyPK(FileKeyPK fileKeyPK) {
        this.fileKeyPK = fileKeyPK;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }
}

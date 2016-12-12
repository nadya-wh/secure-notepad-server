package com.polovtseva.notepad.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import javax.persistence.*;
import java.io.Serializable;

/**
 * Created by nadez on 9/27/2016.
 */

@Entity
@Table(name = "files")
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
public class File implements Serializable {

    @Id
    @Column(name = "file_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long fileId;

    @Column(name = "filename")
    private String filename;

    @Column(name = "value")
    private String value;

    @Column(name = "is_encrypted")
    private Boolean encrypted;

    @Column(name = "public_key")
    private String publicKey;

    public File() {
    }

    public File(String filename, String value) {
        this.filename = filename;
        this.value = value;
    }

    public File(Long fileId, String filename, String value) {
        this.fileId = fileId;
        this.filename = filename;
        this.value = value;
    }

    public String getFilename() {
        return filename;
    }

    public void setFilename(String filename) {
        this.filename = filename;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public Long getFileId() {
        return fileId;
    }

    public Boolean getEncrypted() {
        return encrypted;
    }

    public void setEncrypted(Boolean encrypted) {
        this.encrypted = encrypted;
    }

    public void setFileId(Long fileId) {
        this.fileId = fileId;
    }

    public String getPublicKey() {
        return publicKey;
    }

    public void setPublicKey(String publicKey) {
        this.publicKey = publicKey;
    }

    @Override
    public String toString() {
        return "File{" +
                "filename='" + filename + '\'' +
                ", value='" + value + '\'' +
                '}';
    }
}

package com.polovtseva.notepad.model;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import java.io.Serializable;

/**
 * Created by Nadzeya_Polautsava on 11/19/2016.
 */
@Embeddable
public class FileKeyPK implements Serializable{

    @Column(name = "user_id")
    private Long userId;
    @Column(name = "file_id")
    private Long fileId;

    public FileKeyPK() {
    }

    public FileKeyPK(Long userId, Long fileId) {
        this.userId = userId;
        this.fileId = fileId;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public Long getFileId() {
        return fileId;
    }

    public void setFileId(Long fileId) {
        this.fileId = fileId;
    }
}

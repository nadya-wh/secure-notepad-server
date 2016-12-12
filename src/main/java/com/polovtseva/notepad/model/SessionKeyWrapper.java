package com.polovtseva.notepad.model;

import java.io.Serializable;

/**
 * Created by nadez on 10/22/2016.
 */
public class SessionKeyWrapper implements Serializable {
    private String sessionKey;
    private String errorMessage;

    public SessionKeyWrapper() {

    }

    public String getSessionKey() {
        return sessionKey;
    }

    public void setSessionKey(String sessionKey) {
        this.sessionKey = sessionKey;
    }

    public String getErrorMessage() {
        return errorMessage;
    }

    public void setErrorMessage(String errorMessage) {
        this.errorMessage = errorMessage;
    }
}

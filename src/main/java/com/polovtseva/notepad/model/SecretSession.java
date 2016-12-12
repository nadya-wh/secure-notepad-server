package com.polovtseva.notepad.model;

import java.security.PublicKey;
import java.time.LocalDateTime;

/**
 * Created by Nadzeya_Polautsava on 10/27/2016.
 */
public class SecretSession {
    private User user;
    private LocalDateTime sessionKeyGivenTime;
    private PublicKey publicKey;

    public SecretSession(User user, PublicKey publicKey) {
        this.user = user;
        this.publicKey = publicKey;
        sessionKeyGivenTime = LocalDateTime.now();
    }

    public User getUser() {
        return user;
    }

    public PublicKey getPublicKey() {
        return publicKey;
    }

    public void setPublicKey(PublicKey publicKey) {
        this.publicKey = publicKey;
    }
}

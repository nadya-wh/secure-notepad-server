package com.polovtseva.notepad.controller;

import com.polovtseva.notepad.encryption.KeyBuilder;
import com.polovtseva.notepad.encryption.RSAEncryptUtil;
import com.polovtseva.notepad.model.SecretSession;
import com.polovtseva.notepad.model.SessionKeyWrapper;
import com.polovtseva.notepad.model.User;
import com.polovtseva.notepad.service.UserService;
import com.polovtseva.notepad.service.impl.UserServiceImpl;
import com.polovtseva.notepad.signature.SignatureUtil;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.security.PublicKey;
import java.security.interfaces.RSAPublicKey;
import java.util.HashMap;
import java.util.Map;
import java.util.function.BooleanSupplier;

/**
 * Created by nadez on 10/22/2016.
 */
@RestController
public class UserController {

    private UserService userService;

    private static Map<String, SecretSession> currentUsers = new HashMap<>();



    public UserController() {
        userService = UserServiceImpl.getInstance();
    }
//
//    @RequestMapping(method = RequestMethod.POST, value = "/signin2")
//    public ResponseEntity<SessionKeyWrapper> signIn(@RequestParam(value = "login") String login,
//                                                    @RequestParam(value = "password") String password,
//                                                    @RequestParam(value = "secretToken") String secretToken) {
//        SessionKeyWrapper sessionKeyWrapper = new SessionKeyWrapper();
//        User user = userService.find(login, password, secretToken);
//        if (user != null) {
//            String sessionKey = KeyBuilder.generateSessionKey();
//
//            sessionKeyWrapper.setSessionKey(sessionKey);
//            currentUsers.put(sessionKey, new SecretSession(user));
//            return new ResponseEntity<>(sessionKeyWrapper, HttpStatus.OK);
//        }
//        return new ResponseEntity<>(sessionKeyWrapper, HttpStatus.UNAUTHORIZED);
//    }

    @RequestMapping(method = RequestMethod.POST, value = "/signin")
    public ResponseEntity<SessionKeyWrapper> signIn(@RequestParam(value = "login") String login,
                                                    @RequestParam(value = "password") String password,
                                                    @RequestParam(value = "publicKey", defaultValue = "") String publicKeyStr) {
        PublicKey publicKey = null;
        SessionKeyWrapper sessionKeyWrapper = new SessionKeyWrapper();
        if (publicKeyStr != null && !publicKeyStr.isEmpty()) {
            publicKey = RSAEncryptUtil.countPublicKeyFromString(publicKeyStr);
        } else {
            publicKey = (PublicKey) RSAEncryptUtil.restoreKey(RSAEncryptUtil.PUBLIC_KEY_FILENAME);
        }
        if (publicKey == null) {
            sessionKeyWrapper.setErrorMessage("No public key.");
            return new ResponseEntity<>(sessionKeyWrapper, HttpStatus.EXPECTATION_FAILED);
        }

        User user = userService.find(login, password);
        if (user != null) {
            String sessionKey = KeyBuilder.generateSessionKey();
            String encryptedSessionKey = RSAEncryptUtil.encrypt(sessionKey, publicKey);
            currentUsers.put(sessionKey, new SecretSession(user, publicKey));
            sessionKeyWrapper.setSessionKey(encryptedSessionKey);
            return new ResponseEntity<>(sessionKeyWrapper, HttpStatus.OK);
        }
        sessionKeyWrapper.setErrorMessage("Invalid login and/or password.");
        return new ResponseEntity<>(sessionKeyWrapper, HttpStatus.UNAUTHORIZED);
    }

    @RequestMapping(method = RequestMethod.POST, value = "/signup")
    public ResponseEntity<SessionKeyWrapper> signUp(@RequestParam(value = "login") String login,
                                                    @RequestParam(value = "password") String password,
                                                    @RequestParam(value = "signature") String signature,
                                                    @RequestParam(value = "publicKey") String publicKey) {
        SessionKeyWrapper sessionKeyWrapper = new SessionKeyWrapper();
        PublicKey rsaKey = RSAEncryptUtil.countPublicKeyFromString(publicKey);
        if (rsaKey == null) {
            sessionKeyWrapper.setErrorMessage("Key is not valid.");
            return new ResponseEntity<>(sessionKeyWrapper, HttpStatus.BAD_REQUEST);
        }
        if (SignatureUtil.verify(password, signature, rsaKey)) {

            if (userService.find(login)) {
                sessionKeyWrapper.setErrorMessage("Such user already exists.");
                return new ResponseEntity<>(sessionKeyWrapper, HttpStatus.CONFLICT);
            }
            userService.save(new User(login, password));
            User user = userService.find(login, password);
            if (user != null) {
                String sessionKey = KeyBuilder.generateSessionKey();
                String encryptedSessionKey = RSAEncryptUtil.encrypt(sessionKey, rsaKey);
                sessionKeyWrapper.setSessionKey(encryptedSessionKey);
                currentUsers.put(sessionKey, new SecretSession(user, rsaKey));
                return new ResponseEntity<>(sessionKeyWrapper, HttpStatus.OK);
            }
            sessionKeyWrapper.setErrorMessage("Could not save user.");
            return new ResponseEntity<>(sessionKeyWrapper, HttpStatus.UNAUTHORIZED);
        }
        sessionKeyWrapper.setErrorMessage("Signature was not verified.");
        return new ResponseEntity<>(sessionKeyWrapper, HttpStatus.FORBIDDEN);
    }

    public static Boolean userExists(String sessionKey) {
        return currentUsers.get(sessionKey) != null;
    }

    public static SecretSession takeSession(String sessionKey) {
        return currentUsers.get(sessionKey);
    }
}

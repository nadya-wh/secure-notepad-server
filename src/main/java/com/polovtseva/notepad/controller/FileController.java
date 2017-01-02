package com.polovtseva.notepad.controller;

import com.polovtseva.notepad.encryption.SerpentEncryptUtil;
import com.polovtseva.notepad.model.File;
import com.polovtseva.notepad.model.FileKey;
import com.polovtseva.notepad.model.SecretSession;
import com.polovtseva.notepad.model.User;
import com.polovtseva.notepad.service.FileService;
import com.polovtseva.notepad.service.exception.ServiceException;
import com.polovtseva.notepad.service.impl.FileKeyServiceImpl;
import com.polovtseva.notepad.service.impl.FileServiceImpl;
import com.polovtseva.notepad.signature.SignatureUtil;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.crypto.SecretKey;
import java.security.PublicKey;

/**
 * Created by nadez on 10/22/2016.
 */
@RestController
public class FileController {

    private static final String INIT_VECTOR = "initVector";

    //@Autowired
    private FileService fileService;

    public FileController() {
        this.fileService = FileServiceImpl.getInstance();
    }

    public FileController(FileService fileService) {
        this.fileService = fileService;
    }

    public void setFileService(FileServiceImpl fileService) {
        this.fileService = fileService;
    }


    @RequestMapping(method = RequestMethod.POST, value = "/files")
    public ResponseEntity<File> getFile(@RequestParam(value = "name") String filename,
                                        @RequestParam(value = "sessionKey") String sessionKey,
                                        @RequestParam(value = "signature") String signature) {
        File file = null;
        SecretSession secretSession = UserController.takeSession(sessionKey);
        PublicKey publicKey = secretSession.getPublicKey();
        if (publicKey != null && SignatureUtil.verify(filename, signature, publicKey)) {
            if (!UserController.userExists(sessionKey)) {
                return new ResponseEntity<>(file, HttpStatus.UNAUTHORIZED);
            }
            try {
                file = fileService.find(filename);
                if (file != null && file.getValue() != null) {
                    SecretKey secretKey = SerpentEncryptUtil.buildSpecKey(sessionKey, SerpentEncryptUtil.SERPENT_CIPHER_NAME);
                    SerpentEncryptUtil encryptor = new SerpentEncryptUtil();
                    String fileEncrypted = encryptor.encrypt(secretKey, INIT_VECTOR, file.getValue());
                    file.setValue(fileEncrypted);
                } else {
                    return new ResponseEntity<>(file, HttpStatus.NOT_FOUND);
                }
            } catch (ServiceException e) {
                // TODO: 10/22/2016 d
                e.printStackTrace();
            }
            return new ResponseEntity<>(file, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(file, HttpStatus.FORBIDDEN);
        }
    }

    @RequestMapping(method = RequestMethod.POST, value = "/file")
    public ResponseEntity<File> saveFile(@RequestParam(value = "name") String filename,
                                         @RequestParam(value = "sessionKey") String sessionKey,
                                         @RequestParam(value = "signature") String signature,
                                         @RequestParam(value = "value") String value) {
        File file = new File(filename, value);
        SecretSession secretSession = UserController.takeSession(sessionKey);
        PublicKey publicKey = secretSession.getPublicKey();
        if (publicKey != null && SignatureUtil.verify(filename, signature, publicKey)) {
            if (!UserController.userExists(sessionKey)) {
                return new ResponseEntity<>(file, HttpStatus.UNAUTHORIZED);
            }
            try {
                fileService.save(file);
                return new ResponseEntity<>(file, HttpStatus.OK);
            } catch (ServiceException e) {
                // TODO: 10/22/2016 logs
                e.printStackTrace();
            }
            return new ResponseEntity<>(file, HttpStatus.FORBIDDEN);
        } else {
            return new ResponseEntity<>(file, HttpStatus.FORBIDDEN);
        }
    }

    @RequestMapping(method = RequestMethod.POST, value = "/key")
    public ResponseEntity<FileKey> getKey(@RequestParam(value = "name") String filename,
                                          @RequestParam(value = "sessionKey") String sessionKey,
                                          @RequestParam(value = "signature") String signature) {
        FileKey keyForFile = null;
        SecretSession secretSession = UserController.takeSession(sessionKey);
        PublicKey publicKey = secretSession.getPublicKey();
        if (publicKey != null && SignatureUtil.verify(filename, signature, publicKey)) {
            if (!UserController.userExists(sessionKey)) {
                return new ResponseEntity<>(keyForFile, HttpStatus.UNAUTHORIZED);
            } else {
                User user = UserController.takeSession(sessionKey).getUser();
                FileKey fileKey = FileKeyServiceImpl.getInstance().readKey(filename, user.getUserId());
                keyForFile = fileKey;
                return new ResponseEntity<>(keyForFile, HttpStatus.OK);
            }
        }
        return new ResponseEntity<>(keyForFile, HttpStatus.FORBIDDEN);
    }


    @RequestMapping(method = RequestMethod.POST, value = "/keys")
    public ResponseEntity<FileKey> saveKey(@RequestParam(value = "name") String filename,
                                           @RequestParam(value = "sessionKey") String sessionKey,
                                           @RequestParam(value = "signature") String signature) {
        FileKey keyForFile = null;
        SecretSession secretSession = UserController.takeSession(sessionKey);
        PublicKey publicKey = secretSession.getPublicKey();
        if (publicKey != null && SignatureUtil.verify(filename, signature, publicKey)) {
            if (!UserController.userExists(sessionKey)) {
                return new ResponseEntity<>(keyForFile, HttpStatus.UNAUTHORIZED);
            } else {
                User user = UserController.takeSession(sessionKey).getUser();
                FileKeyServiceImpl.getInstance().saveKey(filename, user.getUserId(),
                        sessionKey);
                return new ResponseEntity<>(new FileKey(null, sessionKey), HttpStatus.OK);
            }
        }
        return new ResponseEntity<>(keyForFile, HttpStatus.FORBIDDEN);
    }
}

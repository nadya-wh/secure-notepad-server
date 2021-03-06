package com.polovtseva.notepad.main;

import com.polovtseva.notepad.controller.FileController;
import com.polovtseva.notepad.dao.impl.UserDAOImpl;
import com.polovtseva.notepad.encryption.KeyStoreUtil;
import com.polovtseva.notepad.model.File;
import com.polovtseva.notepad.service.impl.FileServiceImpl;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import java.security.*;
import java.security.cert.CertificateException;

/**
 * Created by nadez on 10/22/2016.
 */
@SpringBootApplication
@ComponentScan(basePackageClasses= FileController.class)
public class RestApplication {
    public static void main(String[] args) {
        /*ConfigurableApplicationContext ctx = new ClassPathXmlApplicationContext(
                new String []{"applicationContext.xml"});
        System.out.println(ctx.getBean(UserDAOImpl.class));
        // add a shutdown hook for the above context...
        ctx.registerShutdownHook();*/
        Security.addProvider(new org.bouncycastle.jce.provider.BouncyCastleProvider());
        SpringApplication.run(RestApplication.class, args);
    }
}

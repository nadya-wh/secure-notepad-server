package com.polovtseva.notepad.dao;

import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;

/**
 * Created by User on 28.02.2016.
 */
public class SessionWrapper {
    private static SessionFactory sessionFactory;

   public static SessionFactory getSessionFactory() {
        if (sessionFactory == null) {
            sessionFactory =  new Configuration().configure().buildSessionFactory();
        }
       return sessionFactory;
//       return null;
    }
}

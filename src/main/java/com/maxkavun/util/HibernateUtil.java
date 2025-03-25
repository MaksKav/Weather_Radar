package com.maxkavun.util;

import com.maxkavun.entity.Location;
import com.maxkavun.entity.Session;
import com.maxkavun.entity.User;
import com.maxkavun.exception.HibernateInitializationException;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;

@Slf4j
public class HibernateUtil {
    @Getter
    private static final SessionFactory sessionFactory = buildSessionFactory();

    private HibernateUtil() {
    }

    private static SessionFactory buildSessionFactory() {
        try {
            log.info("Building Hibernate SessionFactory");
            return new Configuration()
                    .addAnnotatedClass(User.class)
                    .addAnnotatedClass(Location.class)
                    .addAnnotatedClass(Session.class)
                    .configure("hibernate.cfg.xml")
                    .buildSessionFactory();
        } catch (Exception e) {
            log.error("Failed to build Hibernate SessionFactory", e);
            throw new HibernateInitializationException("Could not initialize Hibernate SessionFactory", e);
        }
    }
}

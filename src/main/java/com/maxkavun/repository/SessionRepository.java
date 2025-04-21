package com.maxkavun.repository;

import com.maxkavun.entity.Session;
import com.maxkavun.entity.User;
import org.hibernate.SessionFactory;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public class SessionRepository extends AbstractHibernateRepository<Session , UUID> {

    public SessionRepository(SessionFactory sessionFactory) {
        super(Session.class , sessionFactory);
    }

    public Optional<User> getUserBySessionId(UUID sessionId) {
        return findById(sessionId).map(Session::getUser);
    }
}

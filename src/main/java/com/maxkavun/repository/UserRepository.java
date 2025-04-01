package com.maxkavun.repository;

import com.maxkavun.entity.User;
import com.maxkavun.exception.UserPersistenceException;
import lombok.extern.slf4j.Slf4j;
import org.hibernate.HibernateException;
import org.hibernate.SessionFactory;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Slf4j
@Repository
public class UserRepository extends AbstractHibernateRepository<User, Long> {

    protected UserRepository(SessionFactory sessionFactory) {
        super(User.class, sessionFactory);
    }

    @Transactional(readOnly = true)
    public Optional<User> findByUsername(String username) {
        try {
            String usernameForQuery = username.toLowerCase();
            return Optional.ofNullable(getSession()
                    .createQuery(" select u from User u  where LOWER(u.login)=:username", User.class)
                    .setParameter("username", usernameForQuery)
                    .getSingleResultOrNull());
        } catch (HibernateException exception) {
            log.error("Error while find user with username: {}", username, exception);
            throw new UserPersistenceException("Could not find user with username: " + username, exception);
        }
    }
}


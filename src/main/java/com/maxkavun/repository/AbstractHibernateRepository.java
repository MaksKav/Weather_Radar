package com.maxkavun.repository;

import com.maxkavun.entity.AbstractEntity;
import com.maxkavun.exception.RepositoryException;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.query.Query;
import org.springframework.transaction.annotation.Transactional;
import java.io.Serializable;
import java.util.List;
import java.util.Optional;

public abstract class AbstractHibernateRepository<T extends AbstractEntity<ID>, ID extends Serializable> {

    private final Class<T> entityClass;
    private final SessionFactory sessionFactory;

    protected AbstractHibernateRepository(Class<T> entityClass, SessionFactory sessionFactory) {
        this.entityClass = entityClass;
        this.sessionFactory = sessionFactory;
    }

    protected Session getSession() {
        return sessionFactory.getCurrentSession();
    }

    @Transactional(readOnly = true)
    public Optional<T> findById(ID id) {
        T entity = getSession().get(entityClass, id);
        return Optional.ofNullable(entity);
    }

    @Transactional
    public void save(T entity) {
        try {
            if (entity.getId() == null) {
                getSession().persist(entity);
            } else {
                getSession().merge(entity);
            }
        } catch (Exception e) {
            throw new RepositoryException("Could not save entity: " + entity, e);
        }
    }

    @Transactional
    public void delete(T entity) {
        getSession().remove(entity);
    }

    @Transactional(readOnly = true)
    public List<T> findAll() {
        var session = getSession();
        String hql = "FROM " + entityClass.getSimpleName();
        Query<T> query = session.createQuery(hql, entityClass);
        return query.getResultList();
    }
}

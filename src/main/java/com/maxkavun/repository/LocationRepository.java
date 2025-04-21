package com.maxkavun.repository;

import com.maxkavun.entity.Location;
import com.maxkavun.entity.User;
import com.maxkavun.exception.RepositoryException;
import org.hibernate.HibernateException;
import org.hibernate.SessionFactory;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

@Repository
public class LocationRepository extends AbstractHibernateRepository<Location ,Long> {

    protected LocationRepository(SessionFactory sessionFactory) {
        super(Location.class, sessionFactory);
    }


    public Optional<Location> findByUserAndLatitudeAndLongitude(User user, BigDecimal lat, BigDecimal lon) {
        try {
            return sessionFactory.getCurrentSession()
                    .createQuery("FROM Location WHERE user = :user AND latitude = :lat AND longitude = :lon", Location.class)
                    .setParameter("user", user)
                    .setParameter("lat", lat)
                    .setParameter("lon", lon)
                    .uniqueResultOptional();
        }catch (HibernateException | DataAccessException exception){
            throw new RepositoryException("Error while find location by user , latitude and longitude ",exception);
        }
    }


    public List<Location> findAllUserLocations(User user) {
        try {
            return sessionFactory.getCurrentSession()
                    .createQuery("FROM Location WHERE user = :user", Location.class)
                    .setParameter("user", user)
                    .getResultList();
        }catch (HibernateException | DataAccessException exception){
            throw new RepositoryException("Error while find location by user ",exception);
        }
    }

}

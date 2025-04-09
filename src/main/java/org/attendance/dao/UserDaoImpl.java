package org.attendance.dao;

import jakarta.persistence.NoResultException;
import org.attendance.entity.User;
import org.hibernate.Session;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public class UserDaoImpl extends GenericDAOImpl<User> implements UserDAO {
    public UserDaoImpl() {
        super(User.class);
    }


    @Override
    public Optional<User> findByEmail(String email) {
        try (Session session = getSessionFactory().openSession()) {
            User user = session.createQuery("SELECT u FROM User u WHERE u.email = :email", User.class)
                    .setParameter("email", email)
                    .getSingleResult();
            return Optional.of(user);
        } catch (NoResultException e) {
            return Optional.empty();
        }
    }
}

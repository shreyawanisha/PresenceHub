package org.attendance.dao;

import jakarta.persistence.NoResultException;
import org.attendance.entity.Faculty;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public class FacultyDAOImpl extends GenericDAOImpl<Faculty> implements FacultyDAO {

    public FacultyDAOImpl() {
        super(Faculty.class);
    }

    @Override
    public Optional<Faculty> findByUserId(long userId) {
        try {
            Faculty faculty = getSession().createQuery("SELECT f FROM Faculty f WHERE f.user.id = :userId", Faculty.class)
                    .setParameter("userId", userId)
                    .getSingleResult();
            return Optional.of(faculty);
        } catch (NoResultException e) {
            return Optional.empty();
        }
    }
}
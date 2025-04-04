package org.attendance.dao;

import org.attendance.entity.Faculty;
import org.springframework.stereotype.Repository;

@Repository
public class FacultyDAOImpl extends GenericDAOImpl<Faculty> implements FacultyDAO {

    public FacultyDAOImpl() {
        super(Faculty.class);
    }

    @Override
    public Faculty findByUserId(long userId) {
        try {
            return em.createQuery("SELECT f FROM Faculty f WHERE f.user.id = :userId", Faculty.class)
                    .setParameter("userId", userId)
                    .getSingleResult();
        } catch (Exception e) {
            return null;
        }
    }
}

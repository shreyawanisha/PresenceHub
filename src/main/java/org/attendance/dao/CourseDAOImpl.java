package org.attendance.dao;

import jakarta.persistence.NoResultException;
import org.attendance.entity.Course;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public class CourseDAOImpl extends GenericDAOImpl<Course> implements CourseDAO {

    public CourseDAOImpl() {
        super(Course.class);
    }

    @Override
    public Optional<Course> findByCRN(String crn) {
        try {
            Course course = getSession().createQuery("FROM Course c WHERE c.crn = :crn", Course.class)
                    .setParameter("crn", crn)
                    .getSingleResult();
            return Optional.of(course);
        } catch (NoResultException e) {
            return Optional.empty();
        }
    }
}
package org.attendance.dao;

import jakarta.persistence.NoResultException;
import jakarta.transaction.Transactional;
import org.attendance.entity.Course;
import org.hibernate.Session;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class CourseDAOImpl extends GenericDAOImpl<Course> implements CourseDAO {

    public CourseDAOImpl() {
        super(Course.class);
    }

    @Override
    @Transactional
    public void save(Course course) {
        final Session session = getSession();
        session.persist(course);
    }

    @Override
    public Course findById(Long id) {
        return getSession().find(Course.class, id);
    }

    @Override
    public List<Course> findAll() {
        return getSession().createQuery("FROM Course", Course.class).getResultList();
    }

    @Override
    public Course findByCRN(String crn) {
        try {
            return getSession().createQuery("FROM Course c WHERE c.crn = :crn", Course.class)
                    .setParameter("crn", crn)
                    .getSingleResult();
        } catch (NoResultException e) {
            return null;
        }
    }
}
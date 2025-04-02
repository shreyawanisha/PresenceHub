package org.attendance.dao;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.transaction.Transactional;
import org.attendance.entity.Course;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class CourseDAOImpl extends GenericDAOImpl<Course> implements CourseDAO {

    @PersistenceContext
    private EntityManager em;


    public CourseDAOImpl() {
        super(Course.class);
    }

    @Transactional
    @Override
    public void save(Course course) {
        em.persist(course);
    }

    @Override
    public Course findById(Long id) {
        return em.find(Course.class, id);
    }

    @Override
    public List<Course> findAll() {
        return em.createQuery("SELECT c FROM Course c", Course.class).getResultList();
    }
}

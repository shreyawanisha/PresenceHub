package org.attendance.dao;

import jakarta.persistence.NoResultException;
import org.attendance.entity.Enrollment;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public class EnrollmentDAOImpl extends GenericDAOImpl<Enrollment> implements EnrollmentDAO {

    public EnrollmentDAOImpl() {
        super(Enrollment.class);
    }

    @Override
    public Optional<Enrollment> findByStudentAndCourse(long studentId, long courseId) {
        try {
            final Enrollment enrollment = getSession().createQuery("FROM Enrollment e WHERE e.student.id = :studentId AND e.course.id = :courseId", Enrollment.class)
                    .setParameter("studentId", studentId)
                    .setParameter("courseId", courseId)
                    .getSingleResult();
            return Optional.of(enrollment);
        } catch (NoResultException e) {
            return Optional.empty();
        }
    }

    @Override
    public List<Enrollment> findByCourseId(long courseId) {
        return getSession().createQuery("FROM Enrollment e where e.course.id = :courseId", Enrollment.class)
                .setParameter("courseId", courseId).getResultList();
    }

    @Override
    public List<Enrollment> findByStudentId(long studentId) {
        return getSession().createQuery("FROM Enrollment e where e.student.id = :studentId", Enrollment.class).setParameter("studentId", studentId).getResultList();
    }

    @Override
    public List<Enrollment> findByCourseIdIs(List<Long> courseIds) {
        return getSession().createQuery("FROM Enrollment e where e.course.id IN :courseIds", Enrollment.class)
                .setParameter("courseIds", courseIds)
                .getResultList();
    }
}

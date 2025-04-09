package org.attendance.dao;

import org.attendance.entity.Enrollment;

import java.util.List;
import java.util.Optional;

public interface EnrollmentDAO extends GenericDAO<Enrollment> {
    Optional<Enrollment> findByStudentAndCourse(long studentId, long courseId);
    List<Enrollment> findByCourseId(long courseId);
    List<Enrollment> findByStudentId(long studentId);

    List<Enrollment> findByCourseIdIs(List<Long> courseIds);
}

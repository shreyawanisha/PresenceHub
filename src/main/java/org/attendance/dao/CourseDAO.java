package org.attendance.dao;

import org.attendance.entity.Course;

import java.util.Optional;

public interface CourseDAO extends GenericDAO<Course> {
    Optional<Course> findByCRN(String crn);
}

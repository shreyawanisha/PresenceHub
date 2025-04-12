package org.attendance.dao;

import org.attendance.entity.Course;
import org.attendance.entity.Faculty;

import java.util.List;
import java.util.Optional;

public interface CourseDAO extends GenericDAO<Course> {
    Optional<Course> findByCRN(String crn);
    List<Course> findByFacultiesContaining(Faculty faculty);
}

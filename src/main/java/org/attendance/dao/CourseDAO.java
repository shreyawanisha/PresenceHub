package org.attendance.dao;

import org.attendance.entity.Course;

import java.util.List;

public interface CourseDAO extends GenericDAO<Course> {
    void save(Course course);
    Course findById(Long id);
    List<Course> findAll();
}

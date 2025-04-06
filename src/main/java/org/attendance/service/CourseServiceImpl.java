package org.attendance.service;

import jakarta.transaction.Transactional;
import org.attendance.dao.CourseDAO;
import org.attendance.entity.Course;
import org.attendance.service.interfaces.CourseService;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class CourseServiceImpl implements CourseService {

    private final CourseDAO courseDAO;

    public CourseServiceImpl(CourseDAO courseDAO) {
        this.courseDAO = courseDAO;
    }

    @Transactional
    @Override
    public void saveCourse(Course course) {
        courseDAO.save(course);
    }

    @Override
    public List<Course> getAllCourses() {
        return courseDAO.findAll();
    }

    @Override
    public boolean existsByCrn(String crn) {
        return courseDAO.findByCRN(crn).isPresent();
    }

    @Override
    public Optional<Course> getById(Long id) {
        return courseDAO.findById(id);
    }
}

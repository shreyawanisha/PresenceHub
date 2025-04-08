package org.attendance.service;

import org.attendance.dao.FacultyDAO;
import org.attendance.dto.AssignCourseToFacultyRequestDTO;
import org.attendance.entity.Faculty;
import org.springframework.http.HttpStatus;
import org.springframework.transaction.annotation.Transactional;
import org.attendance.dao.CourseDAO;
import org.attendance.entity.Course;
import org.attendance.service.interfaces.CourseService;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.*;
import java.util.stream.Collectors;

@Transactional
@Service
public class CourseServiceImpl implements CourseService {

    private final CourseDAO courseDAO;
    private final FacultyDAO facultyDAO;

    public CourseServiceImpl(CourseDAO courseDAO, FacultyDAO facultyDAO) {
        this.courseDAO = courseDAO;
        this.facultyDAO = facultyDAO;
    }

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

    @Override
    public void assignFacultyToCourse(AssignCourseToFacultyRequestDTO requestDTO) {
        Course course = courseDAO.findById(requestDTO.getCourseId())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Course not found with ID: " + requestDTO.getCourseId()));
        final Set<Long> alreadyAssignedFacultyIds = course.getFaculties().stream().map(faculty -> faculty.getUser().getId()).collect(Collectors.toSet());

        List<Long> alreadyAssigned = new ArrayList<>();
        Set<Faculty> newFacultyToAssign = new HashSet<>();

        for (Long facultyId : requestDTO.getFacultyIds()) {
            Faculty faculty = facultyDAO.findByUserId(facultyId)
                    .orElseThrow(() -> new ResponseStatusException(HttpStatus.BAD_REQUEST, "Faculty not found with user ID: " + facultyId));

            if (alreadyAssignedFacultyIds.contains(facultyId)) {
                alreadyAssigned.add(facultyId);
            } else {
                newFacultyToAssign.add(faculty);
            }
        }

        if (!alreadyAssigned.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Course already has faculties with user IDs: " + alreadyAssigned);
        }

        course.getFaculties().addAll(newFacultyToAssign);
        courseDAO.update(course);
    }
}

package org.attendance.service;

import org.attendance.dao.FacultyDAO;
import org.attendance.dto.response.AssignCourseToFacultyRequestDTO;
import org.attendance.dto.response.CourseResponseDTO;
import org.attendance.dto.response.FacultyResponseDTO;
import org.attendance.entity.Faculty;
import org.attendance.entity.User;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.context.SecurityContextHolder;
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
    public void assignFacultyToCourse(AssignCourseToFacultyRequestDTO requestDTO) {
        Course course = courseDAO.findById(requestDTO.getCourseId())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Course not found with ID: " + requestDTO.getCourseId()));

        Set<Long> existingFacultyIds = course.getFaculties().stream()
                .map(Faculty::getId)
                .collect(Collectors.toSet());

        List<String> alreadyAssignedNames = new ArrayList<>();
        Set<Faculty> newFacultyToAssign = new HashSet<>();

        for (Long facultyId : requestDTO.getFacultyIds()) {
            Faculty faculty = facultyDAO.findById(facultyId)
                    .orElseThrow(() -> new ResponseStatusException(HttpStatus.BAD_REQUEST, "Faculty not found with ID: " + facultyId));

            if (existingFacultyIds.contains(facultyId)) {
                alreadyAssignedNames.add(faculty.getUser().getUsername());
            } else {
                newFacultyToAssign.add(faculty);
            }
        }

        if (!alreadyAssignedNames.isEmpty()) {
            String msg = "Course already has these assigned faculties: " + String.join(", ", alreadyAssignedNames);
            throw new ResponseStatusException(HttpStatus.CONFLICT, msg);
        }

        course.getFaculties().addAll(newFacultyToAssign);
        courseDAO.update(course);
    }

    @Override
    public List<CourseResponseDTO> getCoursesAssignedToCurrentFaculty() {
        User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        if (user == null) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "User not authenticated, Please login");
        }
        Faculty faculty = facultyDAO.findByUserId(user.getId())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Faculty not found with user ID: " + user.getId()));
        List<Course> assignedCourses = courseDAO.findByFacultiesContaining(faculty);
        return mapToCourseResponseDTO(assignedCourses);
    }

    @Override
    public Optional<Course> findById(Long courseId) {
        return courseDAO.findById(courseId)
                .or(() -> {
                    throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Course not found with ID: " + courseId);
                });
    }

    private List<CourseResponseDTO> mapToCourseResponseDTO(List<Course> assignedCourses) {
        return assignedCourses.stream()
                .map(course -> new CourseResponseDTO(
                        course.getId(),
                        course.getCrn(),
                        course.getCourseName(),
                        course.getDepartment(),
                        course.getSemester(),
                        course.getFaculties().stream()
                                .map(faculty -> new FacultyResponseDTO(
                                        faculty.getUser().getId(),
                                        faculty.getDepartment(),
                                        faculty.getUser().getEmail(),
                                        faculty.getUser().getUsername()))
                                .collect(Collectors.toList())
                ))
                .collect(Collectors.toList());
    }
}

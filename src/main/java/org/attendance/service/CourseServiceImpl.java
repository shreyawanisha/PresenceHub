package org.attendance.service;

import org.attendance.dao.FacultyDAO;
import org.attendance.dao.UserDAO;
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

    public CourseServiceImpl(CourseDAO courseDAO, FacultyDAO facultyDAO, UserDAO userDao) {
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

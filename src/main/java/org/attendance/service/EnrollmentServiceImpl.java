package org.attendance.service;

import org.attendance.dao.CourseDAO;
import org.attendance.dao.EnrollmentDAO;
import org.attendance.dao.StudentDAO;
import org.attendance.dto.response.CourseResponseDTO;
import org.attendance.dto.response.StudentResponseDTO;
import org.attendance.entity.Course;
import org.attendance.entity.Enrollment;
import org.attendance.entity.Student;
import org.attendance.service.interfaces.EnrollmentService;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.stream.Collectors;

@Transactional
@Service
public class EnrollmentServiceImpl implements EnrollmentService {
    private final EnrollmentDAO enrollmentDAO;
    private final StudentDAO studentDAO;
    private final CourseDAO courseDAO;

    public EnrollmentServiceImpl(EnrollmentDAO enrollmentDAO, StudentDAO studentDAO, CourseDAO courseDAO) {
        this.enrollmentDAO = enrollmentDAO;
        this.studentDAO = studentDAO;
        this.courseDAO = courseDAO;
    }

    @Override
    public void enrollStudentToCourse(long studentId, long courseId) {
        Student student = studentDAO.findById(studentId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Student not found with ID: " + studentId));

        Course course = courseDAO.findById(courseId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Course not found with ID: " + courseId));

        boolean alreadyEnrolled = enrollmentDAO.findByStudentId(studentId).stream()
                .anyMatch(e -> e.getCourse().getId() == courseId);

        if (alreadyEnrolled) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Student is already enrolled in this course.");
        }

        Enrollment enrollment = new Enrollment(student, course);
        enrollmentDAO.save(enrollment);

    }

    @Override
    public List<StudentResponseDTO> getStudentsByCourse(long courseId) {
        Course course = courseDAO.findById(courseId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Course not found with ID: " + courseId));

        List<Enrollment> enrollments = enrollmentDAO.findByCourseId(courseId);
        return enrollments.stream()
                .map(e -> mapToStudentDto(e.getStudent()))
                .collect(Collectors.toList());
    }

    @Override
    public List<CourseResponseDTO> getCoursesByStudent(long studentId) {
        Student student = studentDAO.findById(studentId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Student not found with ID: " + studentId));

        List<Enrollment> enrollments = enrollmentDAO.findByStudentId(studentId);
        return enrollments.stream()
                .map(e -> mapToCourseDto(e.getCourse()))
                .collect(Collectors.toList());
    }

    private StudentResponseDTO mapToStudentDto(Student student) {
        StudentResponseDTO dto = new StudentResponseDTO();
        dto.setId(student.getId());
        dto.setUsername(student.getUser().getUsername());
        dto.setEmail(student.getUser().getEmail());
        dto.setRollNumber(student.getRollNumber());
        dto.setDepartment(student.getDepartment());
        dto.setSemester(student.getSemester());
        return dto;
    }

    private CourseResponseDTO mapToCourseDto(Course course) {
        CourseResponseDTO dto = new CourseResponseDTO();
        dto.setId(course.getId());
        dto.setCrn(course.getCrn());
        dto.setCourseName(course.getCourseName());
        dto.setDepartment(course.getDepartment());
        dto.setSemester(course.getSemester());
        return dto;
    }
}

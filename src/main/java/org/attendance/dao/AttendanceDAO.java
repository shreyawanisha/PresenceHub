package org.attendance.dao;

import org.attendance.entity.Attendance;
import org.attendance.entity.Course;
import org.attendance.entity.Student;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface AttendanceDAO extends GenericDAO<Attendance> {
    boolean existsByStudentAndCourseAndAttendanceDate(Student student, Course course, LocalDate date);
    Optional<Attendance> findByStudentAndCourseAndDate(Long studentId, Long courseId, LocalDate date);

    boolean existsByCourseAndDate(Long courseId, LocalDate date);

    List<Attendance> findByCourseAndDate(Long courseId, LocalDate date);
}

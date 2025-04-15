package org.attendance.dao;

import jakarta.persistence.TypedQuery;
import org.attendance.entity.Attendance;
import org.attendance.entity.Course;
import org.attendance.entity.Student;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Repository
public class AttendanceDAOImpl extends GenericDAOImpl<Attendance> implements AttendanceDAO {

    public AttendanceDAOImpl() {
        super(Attendance.class);
    }

    @Override
    public boolean existsByStudentAndCourseAndAttendanceDate(Student student, Course course, LocalDate date) {
        String hql = "SELECT COUNT(a) FROM Attendance a WHERE a.student = :student AND a.course = :course AND a.attendanceDate = :date";
        TypedQuery<Long> query = getSession().createQuery(hql, Long.class);
        query.setParameter("student", student);
        query.setParameter("course", course);
        query.setParameter("date", date);
        return query.getSingleResult() > 0;
    }
    @Override
    public Optional<Attendance> findByStudentAndCourseAndDate(Long studentId, Long courseId, LocalDate date) {
        return getSession().createQuery("FROM Attendance a WHERE a.student.id = :studentId AND a.course.id = :courseId AND a.attendanceDate = :date", Attendance.class)
                .setParameter("studentId", studentId)
                .setParameter("courseId", courseId)
                .setParameter("date", date)
                .uniqueResultOptional();
    }

    @Override
    public boolean existsByCourseAndDate(Long courseId, LocalDate date) {
        return getSession().createQuery("FROM Attendance a WHERE a.course.id = :courseId AND a.attendanceDate = :date", Attendance.class)
                .setParameter("courseId", courseId)
                .setParameter("date", date)
                .uniqueResultOptional()
                .isPresent();
    }

    @Override
    public List<Attendance> findByCourseAndDate(Long courseId, LocalDate date) {
        return getSession().createQuery("FROM Attendance a WHERE a.course.id = :courseId AND a.attendanceDate = :date", Attendance.class)
                .setParameter("courseId", courseId)
                .setParameter("date", date)
                .getResultList();
    }

    @Override
    public List<Attendance> findByStudentId(long studentId) {
       return getSession().createQuery("FROM Attendance a WHERE a.student.id = :studentId", Attendance.class)
                .setParameter("studentId", studentId)
                .getResultList();
    }
}

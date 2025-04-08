package org.attendance.entity;

import jakarta.persistence.*;
import org.attendance.enums.AttendanceStatus;

import java.time.LocalDate;

@Entity
@Table(name = "attendances", uniqueConstraints = {@UniqueConstraint(columnNames = {"student_id", "course_id", "attendance_date"})})
public class Attendance extends BaseEntity{

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "student_id", nullable = false)
    private Student student;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "course_id", nullable = false)
    private Course course;

    @Column(nullable = false, name = "attendance_date")
    private LocalDate attendanceDate;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false , name = "status")
    private AttendanceStatus status;

    public Attendance() {}

    public Attendance(Student student, Course course, LocalDate attendanceDate, AttendanceStatus status) {
        this.student = student;
        this.course = course;
        this.attendanceDate = attendanceDate;
        this.status = status;
    }

    public Student getStudent() {
        return student;
    }

    public void setStudent(Student student) {
        this.student = student;
    }

    public Course getCourse() {
        return course;
    }

    public void setCourse(Course course) {
        this.course = course;
    }

    public LocalDate getAttendanceDate() {
        return attendanceDate;
    }

    public void setAttendanceDate(LocalDate attendanceDate) {
        this.attendanceDate = attendanceDate;
    }

    public AttendanceStatus getStatus() {
        return status;
    }

    public void setStatus(AttendanceStatus status) {
        this.status = status;
    }
}
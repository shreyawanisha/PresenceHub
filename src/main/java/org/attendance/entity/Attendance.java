package org.attendance.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.attendance.enums.AttendanceStatus;

import java.time.LocalDate;

@Setter
@Getter
@Entity
@Table(name = "attendances", uniqueConstraints = {@UniqueConstraint(columnNames = {"student_id", "course_id", "attendance_date"})})
public class Attendance extends BaseEntity{

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "student_id", nullable = false)
    private Student student;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "course_id", nullable = false)
    private Course course;

    @Column(nullable = false)
    private LocalDate attendanceDate;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private AttendanceStatus status;

    public Attendance() {}

    public Attendance(Student student, Course course, LocalDate attendanceDate, AttendanceStatus status) {
        this.student = student;
        this.course = course;
        this.attendanceDate = attendanceDate;
        this.status = status;
    }

}

package org.attendance.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
@Entity
@Table(name = "enrollments", uniqueConstraints = {@UniqueConstraint(columnNames = {"student_id", "course_id"})})
public class Enrollment extends BaseEntity{

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "student_id", nullable = false)
    private Student student;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "course_id", nullable = false)
    private Course course;

    @Column(nullable = false)
    private LocalDate enrollmentStartDate;

    @Column(nullable = true)
    private LocalDate enrollmentEndDate;

    public Enrollment() {
        this.enrollmentStartDate = LocalDate.now();
    }

    public Enrollment(Student student, Course course) {
        this.student = student;
        this.course = course;
        this.enrollmentStartDate = LocalDate.now();
    }
}

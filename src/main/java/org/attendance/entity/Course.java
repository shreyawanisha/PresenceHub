package org.attendance.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.HashSet;
import java.util.Set;

@Getter
@Setter
@Entity
@Table(name = "courses")
public class Course extends BaseEntity{

    @Column(nullable = false, unique = true)
    private String crn;

    @Column(name = "course_name", nullable = false)
    private String courseName;

    @Column(nullable = false)
    private String department;

    @Column(nullable = false)
    private Integer semester;

    @ManyToMany
    @JoinTable(name = "course_faculty",
            joinColumns = @JoinColumn(name = "course_id"),
            inverseJoinColumns = @JoinColumn(name = "faculty_id"))
    private Set<Faculty> faculties = new HashSet<>();

    public Course() {
    }

    public Course(String crn, String courseName, String department, int semester) {
        this.crn = crn;
        this.courseName = courseName;
        this.department = department;
        this.semester = semester;
    }
}
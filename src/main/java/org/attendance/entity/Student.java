package org.attendance.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.attendance.enums.Semester;

@Getter
@Setter
@Entity
@Table(name = "students")
public class Student extends BaseEntity{

    @OneToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "user_id", nullable = false, unique = true)
    private User user;

    @Column(nullable = false, unique = true)
    private String rollNumber;

    @Column(nullable = false)
    private String department;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Semester semester;

    public Student() {
    }

    public Student(User user, String rollNumber, String department, Semester semester) {
        this.user = user;
        this.rollNumber = rollNumber;
        this.department = department;
        this.semester = semester;
    }

}

package org.attendance.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.HashSet;
import java.util.Set;

@Getter
@Setter
@Entity
@Table(name = "faculties")
public class Faculty extends BaseEntity{

    @OneToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "user_id", nullable = false, unique = true)
    private User user;

    @Column(nullable = false)
    private String department;

    @ManyToMany(mappedBy = "faculties")
    private Set<Course> course = new HashSet<>();


    public Faculty() {
    }

    public Faculty(User user, String department) {
        this.user = user;
        this.department = department;
    }
}

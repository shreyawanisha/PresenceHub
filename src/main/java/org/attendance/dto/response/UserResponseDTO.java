package org.attendance.dto.response;

import org.attendance.entity.User;
import org.attendance.enums.Semester;

public class UserResponseDTO {
    private Long id;
    private String email;
    private String username;


    public String getRollNumber() {
        return rollNumber;
    }

    public void setRollNumber(String rollNumber) {
        this.rollNumber = rollNumber;
    }

    public String getDepartment() {
        return department;
    }

    public void setDepartment(String department) {
        this.department = department;
    }

    public Semester getSemester() {
        return semester;
    }

    public void setSemester(Semester semester) {
        this.semester = semester;
    }

    // Add these:
    private String rollNumber;
    private String department;
    private Semester semester;



    // constructor
    public UserResponseDTO(User user) {
        this.id = user.getId();
        this.email = user.getEmail();
        this.username = user.getUsername();
        this.rollNumber = user.getRollNumber();
        this.department = user.getDepartment();
        this.semester = user.getSemester();
    }

    public UserResponseDTO(Long id, String email, String username) {
        this.id = id;
        this.email = email;
        this.username = username;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }
}

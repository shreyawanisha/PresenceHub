package org.attendance.dto.response;

import org.attendance.enums.Semester;

public class StudentResponseDTO {
    private Long id;
    private String username;
    private String rollNumber;
    private String department;
    private String email;
    private Semester semester;

    public StudentResponseDTO() {
    }

    public StudentResponseDTO(Long id, String username, String rollNumber, String department, String email, Semester semester) {
        this.id = id;
        this.username = username;
        this.rollNumber = rollNumber;
        this.department = department;
        this.email = email;
        this.semester = semester;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

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

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public Semester getSemester() {
        return semester;
    }

    public void setSemester(Semester semester) {
        this.semester = semester;
    }
}

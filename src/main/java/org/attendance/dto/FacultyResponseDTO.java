package org.attendance.dto;

public class FacultyResponseDTO {
    private Long id;
    private String department;
    private String email;
    private String username;

    public FacultyResponseDTO() {
    }

    public FacultyResponseDTO(Long id, String department, String email, String username) {
        this.id = id;
        this.department = department;
        this.email = email;
        this.username = username;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
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

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }
}
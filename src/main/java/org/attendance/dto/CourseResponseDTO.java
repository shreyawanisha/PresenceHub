package org.attendance.dto;

import java.util.List;

public class CourseResponseDTO {
    private Long id;
    private String crn;
    private String courseName;
    private String department;
    private Integer semester;
    private List<FacultyResponseDTO> faculties;

    public CourseResponseDTO() {
    }

    public CourseResponseDTO(Long id, String crn, String courseName, String department, Integer semester, List<FacultyResponseDTO> faculties) {
        this.id = id;
        this.crn = crn;
        this.courseName = courseName;
        this.department = department;
        this.semester = semester;
        this.faculties = faculties;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getCrn() {
        return crn;
    }

    public void setCrn(String crn) {
        this.crn = crn;
    }

    public String getCourseName() {
        return courseName;
    }

    public void setCourseName(String courseName) {
        this.courseName = courseName;
    }

    public String getDepartment() {
        return department;
    }

    public void setDepartment(String department) {
        this.department = department;
    }

    public Integer getSemester() {
        return semester;
    }

    public void setSemester(Integer semester) {
        this.semester = semester;
    }

    public List<FacultyResponseDTO> getFaculties() {
        return faculties;
    }

    public void setFaculties(List<FacultyResponseDTO> faculties) {
        this.faculties = faculties;
    }
}
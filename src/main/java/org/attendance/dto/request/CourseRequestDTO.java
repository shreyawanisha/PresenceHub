package org.attendance.dto.request;
import jakarta.validation.constraints.NotBlank;
import org.attendance.enums.Semester;

public class CourseRequestDTO {

    @NotBlank(message = "CRN is required")
    private String crn;

    @NotBlank(message = "Course name is required")
    private String courseName;

    @NotBlank(message = "Department is required")
    private String department;

    private Semester semester;

    public CourseRequestDTO() {
    }

    public CourseRequestDTO(String crn, String courseName, String department, Semester semester) {
        this.crn = crn;
        this.courseName = courseName;
        this.department = department;
        this.semester = semester;
    }

    public String getCrn() {
        return crn;
    }

    public String getCourseName() {
        return courseName;
    }

    public String getDepartment() {
        return department;
    }

    public Semester getSemester() {
        return semester;
    }

    public void setCrn(String crn) {
        this.crn = crn;
    }

    public void setCourseName(String courseName) {
        this.courseName = courseName;
    }

    public void setDepartment(String department) {
        this.department = department;
    }

    public void setSemester(Semester semester) {
        this.semester = semester;
    }
}
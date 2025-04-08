package org.attendance.dto.response;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;

import java.util.List;

public class AssignCourseToFacultyRequestDTO {

    @NotNull(message = "Course ID is required")
    @Min(value = 1, message = "Course ID must be greater than 0")
    private Long courseId;

    @NotEmpty(message = "Faculty IDs list must not be empty")
    private List<Long> facultyIds;

    public AssignCourseToFacultyRequestDTO() {
    }

    public AssignCourseToFacultyRequestDTO(Long courseId, List<Long> facultyIds) {
        this.courseId = courseId;
        this.facultyIds = facultyIds;
    }

    public Long getCourseId() {
        return courseId;
    }

    public void setCourseId(Long courseId) {
        this.courseId = courseId;
    }

    public List<Long> getFacultyIds() {
        return facultyIds;
    }

    public void setFacultyIds(List<Long> facultyIds) {
        this.facultyIds = facultyIds;
    }
}

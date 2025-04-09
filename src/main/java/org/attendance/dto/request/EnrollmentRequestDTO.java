package org.attendance.dto.request;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;

public class EnrollmentRequestDTO {
    @NotNull(message = "Student ID cannot be blank")
    @Min(value = 1, message = "Student ID must be greater than 0")
    private long studentId;

    @NotNull(message = "Course ID cannot be blank")
    @Min(value = 1, message = "Course ID must be greater than 0")
    private long courseId;

    public EnrollmentRequestDTO() {
    }

    public EnrollmentRequestDTO(long studentId, long courseId) {
        this.studentId = studentId;
        this.courseId = courseId;
    }

    public long getStudentId() {
        return studentId;
    }

    public void setStudentId(long studentId) {
        this.studentId = studentId;
    }

    public long getCourseId() {
        return courseId;
    }

    public void setCourseId(long courseId) {
        this.courseId = courseId;
    }
}

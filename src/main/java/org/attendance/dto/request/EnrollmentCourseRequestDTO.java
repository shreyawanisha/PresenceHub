package org.attendance.dto.request;

import jakarta.validation.constraints.NotNull;

public class EnrollmentCourseRequestDTO {

    @NotNull(message = "Course ID is required")
    private long courseId;

    public long getCourseId() {
        return courseId;
    }

    public void setCourseId(long courseId) {
        this.courseId = courseId;
    }
}

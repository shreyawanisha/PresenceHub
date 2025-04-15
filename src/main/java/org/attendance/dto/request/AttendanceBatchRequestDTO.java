package org.attendance.dto.request;

import java.time.LocalDate;
import java.util.List;

public class AttendanceBatchRequestDTO {
    private Long courseId;
    private LocalDate date;
    private List<Long> presentStudentIds;

    // Getters & Setters
    public Long getCourseId() {
        return courseId;
    }

    public void setCourseId(Long courseId) {
        this.courseId = courseId;
    }

    public LocalDate getDate() {
        return date;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }

    public List<Long> getPresentStudentIds() {
        return presentStudentIds;
    }

    public void setPresentStudentIds(List<Long> presentStudentIds) {
        this.presentStudentIds = presentStudentIds;
    }
}

package org.attendance.dto.response;

import org.attendance.entity.Attendance;
import org.attendance.entity.Course;
import org.attendance.entity.Student;
import org.attendance.enums.AttendanceStatus;

import java.time.LocalDate;

public class AttendanceRecordDTO {
    private Long attendanceId;
    private String username;
    private String email;
    private String rollNumber;
    private AttendanceStatus status;
    private String courseName;

    public String getCourseName() {
        return courseName;
    }

    public void setCourseName(String courseName) {
        this.courseName = courseName;
    }

    public AttendanceRecordDTO(Attendance attendance) {
        this.attendanceId = attendance.getId();
        this.date = attendance.getAttendanceDate();
        this.status = attendance.getStatus();

        Student student = attendance.getStudent();
        if (student != null) {
            this.username = student.getUser().getUsername();
            this.email = student.getUser().getEmail();
            this.rollNumber = student.getRollNumber();
        }

        Course course = attendance.getCourse();
        if (course != null) {
            this.courseName = course.getCourseName();
        }
    }

    public LocalDate getDate() {
        return date;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }

    private LocalDate date;

    public AttendanceRecordDTO() {}


    public Long getAttendanceId() {
        return attendanceId;
    }

    public void setAttendanceId(Long attendanceId) {
        this.attendanceId = attendanceId;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getRollNumber() {
        return rollNumber;
    }

    public void setRollNumber(String rollNumber) {
        this.rollNumber = rollNumber;
    }

    public AttendanceStatus getStatus() {
        return status;
    }

    public void setStatus(AttendanceStatus status) {
        this.status = status;
    }
}
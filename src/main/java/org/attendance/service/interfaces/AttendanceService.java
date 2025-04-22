package org.attendance.service.interfaces;

import org.attendance.dto.response.AttendanceRecordDTO;
import org.attendance.dto.request.AttendanceBatchRequestDTO;
import org.attendance.dto.response.AttendanceSummaryDTO;
import org.attendance.entity.Attendance;
import org.attendance.enums.AttendanceStatus;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface AttendanceService {
    void markAttendance(AttendanceBatchRequestDTO dto);
    void updateAttendanceStatus(Long attendanceId, AttendanceStatus status);
    boolean existsByCourseAndDate(Long courseId, LocalDate now);
    List<AttendanceRecordDTO> getAttendanceRecords(Long courseId, LocalDate date);
    List<AttendanceSummaryDTO> getAttendanceSummaryForCurrentStudent();
    List<AttendanceRecordDTO> getStudentRecordsByCourse(Long courseId);
    List<AttendanceRecordDTO> getAttendanceReport(Long courseId, LocalDate start, LocalDate end, String status, String search);
    String generateQrToken(Long courseId);
    void markSingleAttendance(Long courseId, Long studentId, LocalDate date);
    Optional<Attendance> findByStudentAndCourseAndDate(Long studentId, Long courseId, LocalDate date);
}
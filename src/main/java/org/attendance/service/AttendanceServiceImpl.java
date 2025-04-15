package org.attendance.service;

import org.attendance.dao.AttendanceDAO;
import org.attendance.dao.CourseDAO;
import org.attendance.dao.StudentDAO;
import org.attendance.dto.response.AttendanceRecordDTO;
import org.attendance.dto.request.AttendanceBatchRequestDTO;
import org.attendance.entity.Attendance;
import org.attendance.entity.Course;
import org.attendance.entity.Student;
import org.attendance.enums.AttendanceStatus;
import org.attendance.service.interfaces.AttendanceService;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Transactional
public class AttendanceServiceImpl implements AttendanceService {

    private final AttendanceDAO attendanceDAO;
    private final StudentDAO studentDAO;
    private final CourseDAO courseDAO;

    public AttendanceServiceImpl(AttendanceDAO attendanceDAO, StudentDAO studentDAO, CourseDAO courseDAO) {
        this.attendanceDAO = attendanceDAO;
        this.studentDAO = studentDAO;
        this.courseDAO = courseDAO;
    }

    @Override
    public void markAttendance(AttendanceBatchRequestDTO dto) {
        Course course = courseDAO.findById(dto.getCourseId())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Course not found"));

        List<Student> allStudents = studentDAO.findStudentsByCourse(dto.getCourseId());
        LocalDate date = dto.getDate();

        for (Student s : allStudents) {
            AttendanceStatus status = dto.getPresentStudentIds().contains(s.getId()) ? AttendanceStatus.PRESENT : AttendanceStatus.ABSENT;

            Optional<Attendance> existing = attendanceDAO.findByStudentAndCourseAndDate(s.getId(), course.getId(), date);

            if (existing.isPresent()) {
                Attendance a = existing.get();
                a.setStatus(status); // Update existing record
                attendanceDAO.update(a);
            } else {
                Attendance a = new Attendance(s, course, date, status); // Create new
                attendanceDAO.save(a);
            }
        }
    }

    @Override
    public void updateAttendanceStatus(Long attendanceId, AttendanceStatus status) {
        Attendance attendance = attendanceDAO.findById(attendanceId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Attendance record not found"));
        attendance.setStatus(status);
        attendanceDAO.update(attendance);
    }

    @Override
    public boolean existsByCourseAndDate(Long courseId, LocalDate date) {
        return attendanceDAO.existsByCourseAndDate(courseId, date);
    }

    @Override
    public List<AttendanceRecordDTO> getAttendanceRecords(Long courseId, LocalDate date) {
        List<Attendance> attendanceList = attendanceDAO.findByCourseAndDate(courseId, date);

        return attendanceList.stream().map(a -> {
            AttendanceRecordDTO dto = new AttendanceRecordDTO();
            dto.setAttendanceId(a.getId());
            dto.setUsername(a.getStudent().getUser().getUsername());
            dto.setEmail(a.getStudent().getUser().getEmail());
            dto.setRollNumber(a.getStudent().getRollNumber());
            dto.setStatus(a.getStatus());
            return dto;
        }).collect(Collectors.toList());
    }
}

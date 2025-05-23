package org.attendance.service;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import jakarta.annotation.PostConstruct;
import org.attendance.dao.*;
import org.attendance.dto.response.AttendanceRecordDTO;
import org.attendance.dto.request.AttendanceBatchRequestDTO;
import org.attendance.dto.response.AttendanceSummaryDTO;
import org.attendance.entity.*;
import org.attendance.enums.AttendanceStatus;
import org.attendance.service.interfaces.AttendanceService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import javax.crypto.SecretKey;
import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

@Service
@Transactional
public class AttendanceServiceImpl implements AttendanceService {

    @Value("${app.qr.secret-key}")
    private String base64Key;

    private SecretKey secretKey;

    @PostConstruct
    public void init() {
        this.secretKey = Keys.hmacShaKeyFor(Base64.getDecoder().decode(base64Key));
    }

    private final AttendanceDAO attendanceDAO;
    private final StudentDAO studentDAO;
    private final CourseDAO courseDAO;
    private final EnrollmentDAO enrollmentDAO;
    private final ActiveQRTokenDAO activeQRTokenDAO;


    public AttendanceServiceImpl(AttendanceDAO attendanceDAO, StudentDAO studentDAO, CourseDAO courseDAO, EnrollmentDAO enrollmentDAO, ActiveQRTokenDAO activeQRTokenDAO) {
        this.attendanceDAO = attendanceDAO;
        this.studentDAO = studentDAO;
        this.courseDAO = courseDAO;
        this.enrollmentDAO = enrollmentDAO;
        this.activeQRTokenDAO = activeQRTokenDAO;
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

    @Override
    public List<AttendanceSummaryDTO> getAttendanceSummaryForCurrentStudent() {
        User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        Student student = studentDAO.findByUserId(user.getId())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Student not found, please contact admin."));

        List<Enrollment> enrollments = enrollmentDAO.findByStudentId(student.getId());
        List<Long> courseIds = enrollments.stream().map(e -> e.getCourse().getId()).toList();

        List<Attendance> allAttendance = attendanceDAO.findByStudentId(student.getId());

        Map<Long, List<Attendance>> grouped = allAttendance.stream()
                .filter(a -> courseIds.contains(a.getCourse().getId()))
                .collect(Collectors.groupingBy(a -> a.getCourse().getId()));

        List<AttendanceSummaryDTO> summaryList = new ArrayList<>();

        for (Enrollment enrollment : enrollments) {
            Course course = enrollment.getCourse();
            List<Attendance> courseAttendance = grouped.getOrDefault(course.getId(), new ArrayList<>());
            int total = courseAttendance.size();
            int present = (int) courseAttendance.stream()
                    .filter(a -> a.getStatus() == AttendanceStatus.PRESENT)
                    .count();
            double percentage = total == 0 ? 0 : (present * 100.0 / total);

            AttendanceSummaryDTO dto = new AttendanceSummaryDTO();
            dto.setCourseName(course.getCourseName());
            dto.setCrn(course.getCrn());
            dto.setDepartment(course.getDepartment());
            dto.setSemester(course.getSemester().name());
            dto.setTotalClasses(total);
            dto.setPresentCount(present);
            dto.setPercentage(Math.round(percentage * 100.0) / 100.0);

            summaryList.add(dto);
        }

        return summaryList;
    }

    @Override
    public List<AttendanceRecordDTO> getStudentRecordsByCourse(Long courseId) {
        User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        Student student = studentDAO.findByUserId(user.getId())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Student not registered"));

        List<Attendance> records = attendanceDAO.findByStudentAndCourse(student.getId(), courseId);

        return records.stream()
                .map(AttendanceRecordDTO::new)
                .toList();
    }

    @Override
    public List<AttendanceRecordDTO> getAttendanceReport(Long courseId, LocalDate start, LocalDate end, String status, String search) {
        List<Attendance> records = (start != null && end != null)
                ? attendanceDAO.findByCourseAndDateRange(courseId, start, end)
                : attendanceDAO.findByCourse(courseId);

        return records.stream()
                .filter(r -> {
                    boolean statusMatch = (status == null || r.getStatus().name().equalsIgnoreCase(status));
                    boolean searchMatch = (search == null || search.isBlank() ||
                            r.getStudent() != null && (
                                    r.getStudent().getRollNumber().toLowerCase().contains(search.toLowerCase()) ||
                                            r.getStudent().getUser().getUsername().toLowerCase().contains(search.toLowerCase()) ||
                                            r.getStudent().getUser().getEmail().toLowerCase().contains(search.toLowerCase())
                            ));
                    return statusMatch && searchMatch;
                })
                .map(AttendanceRecordDTO::new)
                .toList();
    }

    @Override
    public String generateQrToken(Long courseId) {
        Instant now = Instant.now();
        Instant expiry = now.plusSeconds(300);
        String token = Jwts.builder()
                .setSubject("QR-Attendance")
                .claim("courseId", courseId)
                .claim("date", LocalDate.now().toString())
                .setIssuedAt(Date.from(now))
                .setExpiration(Date.from(expiry))
                .signWith(secretKey, SignatureAlgorithm.HS256)
                .compact();

        ActiveQRToken qr = new ActiveQRToken();
        qr.setCourse(courseDAO.findById(courseId).orElseThrow());
        qr.setToken(token);
        qr.setDate(LocalDate.now());
        qr.setCreatedAt(LocalDateTime.now());
        qr.setExpiresAt(LocalDateTime.now().plusMinutes(5));
        qr.setActive(true);
        activeQRTokenDAO.save(qr);

        return token;
    }

    @Override
    public void markSingleAttendance(Long courseId, Long studentId, LocalDate date) {
        Course course = courseDAO.findById(courseId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Course not found"));

        Student student = studentDAO.findById(studentId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Student not found"));

        Optional<Enrollment> isEnrolled = enrollmentDAO.findByStudentAndCourse(studentId, courseId);
        if (isEnrolled.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "You are not enrolled in this course.");
        }

        boolean hasActiveQR = activeQRTokenDAO
                .findByCourseIdAndDate(courseId, date)
                .filter(ActiveQRToken::isActive)
                .isPresent();

        if (!hasActiveQR) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "No active QR session for this course.");
        }

        Optional<Attendance> existing = attendanceDAO.findByStudentAndCourseAndDate(studentId, courseId, date);
        if (existing.isPresent()) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Attendance already marked.");
        }

        Attendance a = new Attendance(student, course, date, AttendanceStatus.PRESENT);
        attendanceDAO.save(a);
    }

    @Override
    public Optional<Attendance> findByStudentAndCourseAndDate(Long studentId, Long courseId, LocalDate date) {
       return attendanceDAO.findByStudentAndCourseAndDate(studentId, courseId, date);
    }

    @Override
    public void endQRSession(Long courseId, LocalDate date) {
        activeQRTokenDAO.deactivateToken(courseId, date);
    }

    @Override
    public boolean hasActiveQR(Long courseId, LocalDate now) {
    return activeQRTokenDAO.findByCourseIdAndDate(courseId, LocalDate.now())
                .filter(ActiveQRToken::isActive).isPresent();
    }
}

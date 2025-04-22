package org.attendance.util;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import jakarta.annotation.PostConstruct;
import org.attendance.dto.request.QRMarkAttendanceRequestDTO;
import org.attendance.entity.Attendance;
import org.attendance.entity.Course;
import org.attendance.entity.Student;
import org.attendance.service.interfaces.AttendanceService;
import org.attendance.service.interfaces.CourseService;
import org.attendance.service.interfaces.StudentService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ResponseStatusException;

import javax.crypto.SecretKey;
import java.time.LocalDate;
import java.util.Base64;
import java.util.Date;
import java.util.Optional;

@Component
public class QRAttendanceTokenUtil {

    @Value("${app.qr.secret-key}")
    private String base64Key;

    private static SecretKey SECRET_KEY;

    private final AttendanceService attendanceService;
    private final StudentService studentService;
    private final CourseService courseService;

    public QRAttendanceTokenUtil(AttendanceService attendanceService,
                                 StudentService studentService,
                                 CourseService courseService) {
        this.attendanceService = attendanceService;
        this.studentService = studentService;
        this.courseService = courseService;
    }

    @PostConstruct
    public void init() {
        String cleanKey = base64Key.startsWith("base64:") ? base64Key.substring(7) : base64Key;
        SECRET_KEY = Keys.hmacShaKeyFor(Base64.getDecoder().decode(cleanKey));
    }

    public String generateToken(Long courseId, LocalDate date) {
        return Jwts.builder()
                .claim("courseId", courseId)
                .claim("date", date.toString())
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + 5 * 60 * 1000)) // 5 mins
                .signWith(SECRET_KEY, SignatureAlgorithm.HS256)
                .compact();
    }

    public Claims validateToken(String token) {
        try {
            return Jwts.parserBuilder()
                    .setSigningKey(SECRET_KEY)
                    .build()
                    .parseClaimsJws(token)
                    .getBody();
        } catch (JwtException e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Invalid or expired QR token");
        }
    }

    public void markAttendance(QRMarkAttendanceRequestDTO dto, Long studentId) {
        Claims claims = validateToken(dto.getQrToken());

        Long courseId = Long.parseLong(claims.get("courseId").toString());
        LocalDate date = LocalDate.parse(claims.get("date").toString());

        Course course = courseService.findById(courseId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Course not found"));
        Student student = studentService.findById(studentId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Student not found"));

        Optional<Attendance> existing = attendanceService.findByStudentAndCourseAndDate(studentId, courseId, date);
        if (existing.isPresent()) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Attendance already marked for this date");
        }

        attendanceService.markSingleAttendance(courseId, studentId, date);
    }
}

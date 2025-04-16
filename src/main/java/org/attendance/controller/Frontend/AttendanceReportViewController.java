package org.attendance.controller.Frontend;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/reports")
public class AttendanceReportViewController {

    @GetMapping("/attendance")
    public String getAttendanceReportPage() {
        return "attendance_report";
    }
}

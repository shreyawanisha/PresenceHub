package org.attendance.controller.Frontend;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/admin")
public class AdminViewController {

    @GetMapping("/dashboard")
    public String getAdminView() {
        return "admin_dashboard";
    }

    @GetMapping("/add-course")
    public String showAddCoursePage() {
        return "add_course";
    }

    @GetMapping("/assign-faculty")
    public String showAssignFacultyPage() {
        return "assign_faculty";
    }
}

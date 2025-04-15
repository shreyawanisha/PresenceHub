<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!DOCTYPE html>
<html>
<head>
    <title>Admin Dashboard - PresenceHub</title>
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/css/bootstrap.min.css" rel="stylesheet"/>
    <link href="https://cdn.jsdelivr.net/npm/bootstrap-icons@1.10.5/font/bootstrap-icons.css" rel="stylesheet"/>
    <style>
        html, body {
            font-family: 'Segoe UI', sans-serif;
            background: #f8f9fa;
            height: 100%;
            margin: 0;
            padding: 0;
        }
        main {
            padding-top: 30px;
            padding-bottom: 60px;
        }
        .dashboard-header {
            font-size: 2rem;
            font-weight: bold;
            margin: 30px 0;
        }
        .stat-card {
            border-left: 5px solid #0d6efd;
            padding: 20px;
            background: white;
            box-shadow: 0 2px 10px rgba(0,0,0,0.05);
            border-radius: 8px;
            cursor: pointer;
            transition: transform 0.2s, box-shadow 0.2s;
        }
        .stat-card:hover {
            transform: translateY(-4px);
            box-shadow: 0 4px 15px rgba(0, 0, 0, 0.15);
            background-color: #eaf2ff;
        }
        .user-card {
            background-color: #fff;
            padding: 20px;
            border-radius: 10px;
            box-shadow: 0 2px 8px rgba(0,0,0,0.1);
        }
        .scrollable-container {
            max-height: 500px;
            overflow-y: auto;
        }
    </style>
</head>
<body>
<jsp:include page="fragments/navbar.jsp" />
<main class="container">
    <h2 class="text-center dashboard-header">🛡️ Admin Dashboard</h2>

    <!-- Stats -->
    <div class="row text-center mb-5">
        <div class="col-md-4">
            <div class="stat-card" id="viewAllStudents" data-bs-toggle="tooltip" title="Click to view all students">
                <h4>Total Students</h4>
                <p class="fs-2" id="studentCount">...</p>
            </div>
        </div>
        <div class="col-md-4">
            <div class="stat-card" id="viewAllFaculty" data-bs-toggle="tooltip" title="Click to view all faculty">
                <h4>Total Faculty</h4>
                <p class="fs-2" id="facultyCount">...</p>
            </div>
        </div>
        <div class="col-md-4">
            <div class="stat-card" id="viewAllCourses" data-bs-toggle="tooltip" title="Click to view all courses">
                <h4>Total Courses</h4>
                <p class="fs-2" id="courseCount">...</p>
            </div>
        </div>
    </div>

    <!-- Quick Actions -->
    <div class="row text-center mb-4">
        <div class="col-md-6">
            <a href="/admin/add-course" class="btn btn-outline-primary w-100">➕ Add Course</a>
        </div>
        <div class="col-md-6">
            <a href="/admin/assign-faculty" class="btn btn-outline-success w-100">👩‍🏫 Assign Faculty to courses</a>
        </div>
    </div>

    <!-- Authorization Section -->
    <div class="accordion mt-5" id="authorizationAccordion">
        <div class="accordion-item">
            <h2 class="accordion-header" id="headingOne">
                <button class="accordion-button" type="button" data-bs-toggle="collapse" data-bs-target="#studentAuthBody" aria-expanded="true" aria-controls="studentAuthBody">
                    Authorize Students
                </button>
            </h2>
            <div id="studentAuthBody" class="accordion-collapse collapse show" aria-labelledby="headingOne" data-bs-parent="#authorizationAccordion">
                <div class="accordion-body">
                    <div id="unassignedStudentsContainer" class="row g-4"></div>
                </div>
            </div>
        </div>
        <div class="accordion-item">
            <h2 class="accordion-header" id="headingTwo">
                <button class="accordion-button collapsed" type="button" data-bs-toggle="collapse" data-bs-target="#facultyAuthBody" aria-expanded="false" aria-controls="facultyAuthBody">
                    Authorize Faculty
                </button>
            </h2>
            <div id="facultyAuthBody" class="accordion-collapse collapse" aria-labelledby="headingTwo" data-bs-parent="#authorizationAccordion">
                <div class="accordion-body">
                    <div id="unassignedFacultyContainer" class="row g-4"></div>
                </div>
            </div>
        </div>
    </div>
</main>
<jsp:include page="fragments/admin_modals.jsp" />
<jsp:include page="fragments/admin_lists.jsp" />
<script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/js/bootstrap.bundle.min.js"></script>
<script src="/js/admin-dashboard.js"></script>
<script>
    document.addEventListener("DOMContentLoaded", function () {
        const tooltipTriggerList = [].slice.call(document.querySelectorAll('[data-bs-toggle="tooltip"]'));
        tooltipTriggerList.forEach(el => new bootstrap.Tooltip(el));
    });
</script>
</body>
</html>

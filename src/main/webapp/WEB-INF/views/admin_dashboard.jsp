<%--
  Created by IntelliJ IDEA.
  User: shreyawanisha
  Date: 11/04/25
  Time: 10:32 pm
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!DOCTYPE html>
<html>
<head>
    <title>Admin Dashboard - PresenceHub</title>
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/css/bootstrap.min.css" rel="stylesheet"/>
    <style>
        body {
            font-family: 'Segoe UI', sans-serif;
            background: #f8f9fa;
            display: flex;
            flex-direction: column;
            min-height: 100vh;
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
        }
        .quick-actions a {
            text-decoration: none;
        }
    </style>
</head>
<body>

<jsp:include page="fragments/navbar.jsp"/>

<div class="container mt-5">
    <h2 class="text-center dashboard-header">🛡️ Admin Dashboard</h2>

    <div class="row text-center mb-5">
        <div class="col-md-4">
            <div class="stat-card">
                <h4>Total Students</h4>
                <p class="fs-2" id="studentCount">...</p>
            </div>
        </div>
        <div class="col-md-4">
            <div class="stat-card">
                <h4>Total Faculty</h4>
                <p class="fs-2" id="facultyCount">...</p>
            </div>
        </div>
        <div class="col-md-4">
            <div class="stat-card">
                <h4>Total Courses</h4>
                <p class="fs-2" id="courseCount">...</p>
            </div>
        </div>
    </div>

    <div class="row quick-actions text-center">
        <div class="col-md-3">
            <a href="/register" class="btn btn-outline-primary w-100">+ Register Student</a>
        </div>
        <div class="col-md-3">
            <a href="/register" class="btn btn-outline-primary w-100">+ Register Faculty</a>
        </div>
        <div class="col-md-3">
            <a href="/add-course" class="btn btn-outline-primary w-100">+ Add Course</a>
        </div>
        <div class="col-md-3">
            <a href="/assign-faculty" class="btn btn-outline-primary w-100">+ Assign Faculty</a>
        </div>
    </div>
</div>

<jsp:include page="fragments/footer.jsp"/>

<script>
    document.addEventListener("DOMContentLoaded", async function () {
        const token = localStorage.getItem("token");
        if (!token) {
            window.location.href = "/login";
            return;
        }

        try {
            const [studentsRes, facultyRes, coursesRes] = await Promise.all([
                fetch("/api/students", { headers: { Authorization: "Bearer " + token } }),
                fetch("/api/faculties", { headers: { Authorization: "Bearer " + token } }),
                fetch("/api/courses", { headers: { Authorization: "Bearer " + token } })
            ]);

            const students = await studentsRes.json();
            const faculty = await facultyRes.json();
            const courses = await coursesRes.json();

            document.getElementById("studentCount").textContent = students.length;
            document.getElementById("facultyCount").textContent = faculty.length;
            document.getElementById("courseCount").textContent = courses.length;

        } catch (err) {
            console.error("Error loading dashboard stats:", err);
        }
    });
</script>

</body>
</html>


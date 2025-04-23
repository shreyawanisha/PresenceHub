<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ page isELIgnored="true" %>
<!DOCTYPE html>
<html>
<head>
    <title>Faculty Dashboard - PresenceHub</title>
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/css/bootstrap.min.css" rel="stylesheet"/>
    <style>
        body {
            background-color: #f8f9fa;
            font-family: 'Segoe UI', sans-serif;
        }
        .dashboard-container {
            padding: 40px 0;
        }
        .card-actions {
            display: flex;
            gap: 10px;
            flex-wrap: wrap;
            margin-top: 15px;
        }
    </style>
</head>
<body>
<jsp:include page="fragments/navbar.jsp" />

<div class="container dashboard-container">
    <h2 class="text-center mb-4">🎓 Faculty Dashboard</h2>
    <div id="facultyCourses" class="row g-4"></div>
</div>

<jsp:include page="fragments/footer.jsp" />

<script>
    document.addEventListener("DOMContentLoaded", async function () {
        const token = localStorage.getItem("token");
        const res = await fetch("/api/courses/assigned-to-me", {
            headers: { Authorization: "Bearer " + token }
        });
        const courses = await res.json();
        const container = document.getElementById("facultyCourses");

        if (courses.length === 0) {
            container.innerHTML = `<p class="text-muted">No assigned courses.</p>`;
            return;
        }

        courses.forEach(course => {
            const div = document.createElement("div");
            div.className = "col-md-6";
            div.innerHTML = `
                <div class="card shadow-sm">
                    <div class="card-body">
                        <h5 class="card-title text-primary">${course.courseName}</h5>
                        <p class="card-text"><strong>CRN:</strong> ${course.crn}</p>
                        <p class="card-text"><strong>Department:</strong> ${course.department}</p>
                        <p class="card-text"><strong>Semester:</strong> ${course.semester}</p>
                        <div class="card-actions">
                            <a href="/faculty/attendance?courseId=${course.id}" class="btn btn-sm btn-outline-primary">📅 Mark Attendance</a>
                            <a href="/faculty/attendance/update?courseId=${course.id}" class="btn btn-sm btn-outline-secondary">✏️ Update Attendance</a>
                            <a href="/reports/attendance?courseId=${course.id}" class="btn btn-sm btn-outline-dark">📄 View Report</a>
                            <a href="/faculty/qr?courseId=${course.id}" class="btn btn-sm btn-outline-success">📷 Generate QR</a>
                        </div>
                    </div>
                </div>
            `;
            container.appendChild(div);
        });
    });
</script>
</body>
</html>
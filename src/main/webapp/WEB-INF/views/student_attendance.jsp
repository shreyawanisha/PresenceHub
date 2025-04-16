<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ page isELIgnored="true" %>
<!DOCTYPE html>
<html>
<head>
    <title>My Attendance Records - PresenceHub</title>
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/css/bootstrap.min.css" rel="stylesheet"/>
    <style>
        body {
            display: flex;
            flex-direction: column;
            min-height: 100vh;
            font-family: 'Segoe UI', sans-serif;
            background-color: #f8f9fa;
        }
        .dashboard-container {
            flex: 1;
            padding: 50px 0;
        }
        footer {
            background-color: #f8f9fa;
            padding: 15px 0;
            text-align: center;
            font-size: 14px;
            color: #888;
        }
    </style>
</head>
<body>
<jsp:include page="fragments/navbar.jsp" />

<div class="container dashboard-container">
    <h2 class="text-center mb-4">📅 My Attendance Records</h2>
    <div id="courseTitle" class="text-center mb-3 text-muted fw-bold"></div>

    <div class="table-responsive">
        <table class="table table-bordered table-hover">
            <thead class="table-light">
            <tr>
                <th>#</th>
                <th>Date</th>
                <th>Status</th>
            </tr>
            </thead>
            <tbody id="attendanceTableBody"></tbody>
        </table>
    </div>
    <div class="mt-3 text-end">
        <a href="/student/dashboard" class="btn btn-outline-secondary">Back to Dashboard</a>
    </div>
</div>

<jsp:include page="fragments/footer.jsp" />

<script>
    document.addEventListener("DOMContentLoaded", async function () {
        const token = localStorage.getItem("token");
        const params = new URLSearchParams(window.location.search);
        const courseId = params.get("courseId");

        const tbody = document.getElementById("attendanceTableBody");
        const courseTitle = document.getElementById("courseTitle");

        if (!courseId) {
            tbody.innerHTML = `<tr><td colspan="3" class="text-center text-muted">Course ID is missing in the URL.</td></tr>`;
            return;
        }

        try {
            const res = await fetch(`/api/attendance/my-records?courseId=${courseId}`, {
                headers: { Authorization: "Bearer " + token }
            });

            console.log(res.url + " " + res.body);
            if (!res.ok) {
                throw new Error("Server error while fetching attendance records");
            }

            const data = await res.json();

            if (data.length === 0) {
                tbody.innerHTML = `<tr><td colspan="3" class="text-center text-muted">No attendance records found.</td></tr>`;
                return;
            }

            courseTitle.innerText = data[0]?.courseName ?? "Course Name";

            data.forEach((record, index) => {
                const tr = document.createElement("tr");
                tr.innerHTML = `
                  <td>${index + 1}</td>
                  <td>${record.date}</td>
                  <td>${record.status}</td>
                `;
                tbody.appendChild(tr);
            });

        } catch (err) {
            console.error("❌ Error:", err);
            tbody.innerHTML = `<tr><td colspan="3" class="text-center text-danger">Failed to load attendance data. Please try again.</td></tr>`;
        }
    });
</script>
</body>
</html>
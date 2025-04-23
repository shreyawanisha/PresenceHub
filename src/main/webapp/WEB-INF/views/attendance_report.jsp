<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ page isELIgnored="true" %>
<!DOCTYPE html>
<html>
<head>
    <title>Attendance Report - PresenceHub</title>
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
    <h2 class="text-center mb-4">📄 Attendance Report</h2>

    <!-- Filter Row -->
    <div class="row mb-4">
        <div class="col-md-3">
            <label class="form-label">Select Course</label>
            <select class="form-select" id="courseSelect" required>
                <option value="">-- Choose a Course --</option>
            </select>
        </div>
        <div class="col-md-2">
            <label class="form-label">Start Date</label>
            <input type="date" class="form-control" id="startDate">
        </div>
        <div class="col-md-2">
            <label class="form-label">End Date</label>
            <input type="date" class="form-control" id="endDate">
        </div>
        <div class="col-md-2">
            <label class="form-label">Status</label>
            <select class="form-select" id="statusFilter">
                <option value="">All</option>
                <option value="PRESENT">Present</option>
                <option value="ABSENT">Absent</option>
            </select>
        </div>
        <div class="col-md-3">
            <label class="form-label">Search</label>
            <input type="text" class="form-control" id="searchInput" placeholder="Name, Roll No, Email">
        </div>
    </div>

    <!-- Action Row -->
    <div class="row mb-4">
        <div class="col-md-2">
            <button class="btn btn-primary w-100" id="loadReportBtn">📊 Load Report</button>
        </div>
        <div class="col-md-2">
            <button class="btn btn-outline-danger w-100" id="exportPdfBtn">📄 Export PDF</button>
        </div>
    </div>

    <!-- Table -->
    <div class="table-responsive">
        <table class="table table-bordered table-hover">
            <thead class="table-light">
            <tr>
                <th>#</th>
                <th>Date</th>
                <th>Student</th>
                <th>Roll No</th>
                <th>Email</th>
                <th>Status</th>
            </tr>
            </thead>
            <tbody id="reportTableBody"></tbody>
        </table>
    </div>

    <div class="text-end mt-4">
        <a id="dashboardLink" class="btn btn-outline-secondary">🏠 Back to Dashboard</a>
    </div>
</div>

<jsp:include page="fragments/footer.jsp" />

<script>
    document.addEventListener("DOMContentLoaded", async function () {
        const token = localStorage.getItem("token");
        const isAdmin = token && JSON.parse(atob(token.split('.')[1])).role?.includes("ADMIN");
        const role = JSON.parse(atob(token.split('.')[1])).role;
        const backLink = document.getElementById("dashboardLink");

        if (role.includes("ADMIN")) {
            backLink.href = "/admin/dashboard";
        } else if (role.includes("FACULTY")) {
            backLink.href = "/faculty/dashboard";
        }
        const courseSelect = document.getElementById("courseSelect");

        const courseEndpoint = isAdmin ? "/api/courses" : "/api/courses/assigned-to-me";

        const res = await fetch(courseEndpoint, {
            headers: { Authorization: "Bearer " + token }
        });
        const courses = await res.json();
        courses.forEach(course => {
            const opt = document.createElement("option");
            opt.value = course.id;
            opt.textContent = `${course.courseName} (${course.crn})`;
            courseSelect.appendChild(opt);
        });

        document.getElementById("loadReportBtn").addEventListener("click", async function () {
            const courseId = courseSelect.value;
            const start = document.getElementById("startDate").value;
            const end = document.getElementById("endDate").value;
            const status = document.getElementById("statusFilter").value;
            const search = document.getElementById("searchInput").value;

            if (!courseId) return alert("Please select a course!");

            const query = new URLSearchParams();
            if (start) query.append("startDate", start);
            if (end) query.append("endDate", end);
            if (status) query.append("status", status);
            if (search) query.append("search", search);

            const reportRes = await fetch(`/api/attendance/report/course/${courseId}?` + query.toString(), {
                headers: { Authorization: "Bearer " + token }
            });

            const data = await reportRes.json();
            const tbody = document.getElementById("reportTableBody");
            tbody.innerHTML = "";

            if (!data.length) {
                tbody.innerHTML = '<tr><td colspan="6" class="text-center text-muted">No records found.</td></tr>';
                return;
            }

            data.forEach((r, i) => {
                const tr = document.createElement("tr");
                tr.innerHTML = `
                    <td>${i + 1}</td>
                    <td>${r.date}</td>
                    <td>${r.username || "-"}</td>
                    <td>${r.rollNumber || "-"}</td>
                    <td>${r.email || "-"}</td>
                    <td>${r.status}</td>
                `;
                tbody.appendChild(tr);
            });
        });

        document.getElementById("exportPdfBtn").addEventListener("click", async function () {
            const token = localStorage.getItem("token");
            const courseId = document.getElementById("courseSelect").value;
            const start = document.getElementById("startDate").value;
            const end = document.getElementById("endDate").value;
            const status = document.getElementById("statusFilter").value;
            const search = document.getElementById("searchInput").value;

            if (!courseId) {
                alert("❗ Please select a course to export.");
                return;
            }

            const query = new URLSearchParams();
            if (start) query.append("startDate", start);
            if (end) query.append("endDate", end);
            if (status) query.append("status", status);
            if (search) query.append("search", search);

            try {
                const res = await fetch(`/api/export/pdf/course/${courseId}?${query}`, {
                    headers: {
                        Authorization: "Bearer " + token
                    }
                });

                if (!res.ok) {
                    const err = await res.text();
                    throw new Error(err || "Export failed");
                }

                const blob = await res.blob();
                const url = window.URL.createObjectURL(blob);

                const a = document.createElement("a");
                a.href = url;
                a.download = "attendance_report.pdf";
                document.body.appendChild(a);
                a.click();
                a.remove();
                window.URL.revokeObjectURL(url);

            } catch (err) {
                alert("❌ Failed to export PDF: " + err.message);
                console.error("Export error:", err);
            }
        });
});

</script>
</body>
</html>
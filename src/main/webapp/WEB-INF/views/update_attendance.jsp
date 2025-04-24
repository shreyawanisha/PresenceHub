<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ page isELIgnored="true" %>
<!DOCTYPE html>
<html>
<head>
    <title>Update Attendance - Faculty</title>
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/css/bootstrap.min.css" rel="stylesheet"/>
    <style>
        body {
            background-color: #f8f9fa;
            font-family: 'Segoe UI', sans-serif;
            margin: 0;
        }
        .page-wrapper {
            max-width: 900px;
            margin: 0 auto;
            padding: 40px 20px;
        }
        .card {
            margin-bottom: 20px;
        }
    </style>
</head>
<body>

<jsp:include page="fragments/navbar.jsp" />

<div class="page-wrapper">
    <h2 class="text-center mb-4">✏️ Update Attendance</h2>

    <div class="mb-3">
        <label for="courseSelect" class="form-label">Select Course</label>
        <select class="form-select" id="courseSelect"></select>
    </div>

    <div class="mb-3">
        <label for="attendanceDate" class="form-label">Select Date</label>
        <input type="date" class="form-control" id="attendanceDate" required>
    </div>

    <button class="btn btn-primary mb-4" id="loadBtn">🔍 Load Attendance</button>

    <div id="attendanceList"></div>

    <div class="mt-4 text-end">
        <a href="/faculty/dashboard" class="btn btn-outline-secondary">Back to Dashboard</a>
    </div>
</div>


<!-- Toast -->
<div class="position-fixed bottom-0 end-0 p-3" style="z-index: 11">
    <div id="toastMessage" class="toast align-items-center text-white bg-success border-0" role="alert">
        <div class="d-flex">
            <div class="toast-body"></div>
            <button type="button" class="btn-close btn-close-white me-2 m-auto" data-bs-dismiss="toast"></button>
        </div>
    </div>
</div>

<%--<jsp:include page="fragments/footer.jsp" />--%>

<script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/js/bootstrap.bundle.min.js"></script>
<script>
    const token = localStorage.getItem("token");

    async function fetchCourses() {
        const res = await fetch("/api/courses/assigned-to-me", {
            headers: { Authorization: "Bearer " + token }
        });
        const courses = await res.json();
        const select = document.getElementById("courseSelect");
        courses.forEach(course => {
            const opt = document.createElement("option");
            opt.value = course.id;
            opt.textContent = course.courseName + " (" + course.crn + ")";
            select.appendChild(opt);
        });

        const params = new URLSearchParams(window.location.search);
        const preselected = params.get("courseId");
        if (preselected) {
            select.value = preselected;
        }
    }

    function showToast(message, isError = false) {
        const toastEl = document.getElementById("toastMessage");
        toastEl.classList.remove("bg-success", "bg-danger");
        toastEl.classList.add(isError ? "bg-danger" : "bg-success");
        toastEl.querySelector(".toast-body").textContent = message;
        const toast = new bootstrap.Toast(toastEl);
        toast.show();
    }

    async function loadAttendance() {
        const courseId = document.getElementById("courseSelect").value;
        const date = document.getElementById("attendanceDate").value;

        if (!courseId || !date) {
            showToast("❗ Please select both course and date.", true);
            return;
        }

        const url = new URL(window.location.href);
        url.searchParams.set("courseId", courseId);
        window.history.replaceState({}, "", url);

        const res = await fetch(`/api/attendance/by-course-and-date?courseId=${courseId}&date=${date}`, {
            headers: { Authorization: "Bearer " + token }
        });

        const records = await res.json();
        const container = document.getElementById("attendanceList");
        container.innerHTML = "";

        if (!records || records.length === 0) {
            container.innerHTML = "<div class='alert alert-warning'>🚫 No attendance records found for this course and date.</div>";
            return;
        }

        const list = document.createElement("ul");
        list.className = "list-group";
        records.forEach(rec => {
            const li = document.createElement("li");
            li.className = "list-group-item d-flex justify-content-between align-items-center";
            li.innerHTML = `
                <span><strong>${rec.username}</strong> (${rec.rollNumber})</span>
                <div>
                    <button class="btn btn-sm ${rec.status == 'PRESENT' ? 'btn-success' : 'btn-outline-success'} me-2 toggle-btn" data-id="${rec.attendanceId}" data-status="PRESENT">Present</button>
                    <button class="btn btn-sm ${rec.status == 'ABSENT' ? 'btn-danger' : 'btn-outline-danger'} toggle-btn" data-id="${rec.attendanceId}" data-status="ABSENT">Absent</button>
                </div>
            `;
            list.appendChild(li);
        });

        container.appendChild(list);

        document.querySelectorAll(".toggle-btn").forEach(btn => {
            btn.addEventListener("click", async function () {
                const attendanceId = this.getAttribute("data-id");
                const newStatus = this.getAttribute("data-status");

                const res = await fetch(`/api/attendance/update-status/${attendanceId}?status=${newStatus}`, {
                    method: "PUT",
                    headers: { Authorization: "Bearer " + token }
                });

                const msg = await res.json();
                showToast(msg.message, !res.ok);

                document.getElementById("loadBtn").click();
            });
        });
    }

    document.getElementById("loadBtn").addEventListener("click", loadAttendance);

    document.getElementById("courseSelect").addEventListener("change", () => {
        const courseId = document.getElementById("courseSelect").value;
        const url = new URL(window.location.href);
        url.searchParams.set("courseId", courseId);
        window.history.replaceState({}, "", url);
        loadAttendance();
    });

    document.addEventListener("DOMContentLoaded", () => {
        fetchCourses().then(() => {
            const todayDate = new Date();
            const today = todayDate.getFullYear() + '-' +
                String(todayDate.getMonth() + 1).padStart(2, '0') + '-' +
                String(todayDate.getDate()).padStart(2, '0');
            const dateInput = document.getElementById("attendanceDate");
            dateInput.value = today;
            dateInput.max = today;
            loadAttendance();
        });
    });
</script>
</body>
</html>
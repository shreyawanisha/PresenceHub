<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ page isELIgnored="true" %>
<!DOCTYPE html>
<html>
<head>
    <title>Mark Attendance - Faculty</title>
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
    <h2 class="text-center mb-4">🗓️ Mark Attendance (Today Only)</h2>

    <div class="mb-3">
        <label for="courseSelect" class="form-label">Select Course</label>
        <select class="form-select" id="courseSelect"></select>
    </div>

    <div class="mb-3">
        <label for="attendanceDate" class="form-label">Date</label>
        <input type="date" class="form-control" id="attendanceDate" disabled>
    </div>

    <div id="studentList" class="mb-4"></div>

    <button id="submitBtn" class="btn btn-success">✅ Submit Attendance</button>
    <div class="mt-4 text-end">
        <a href="/faculty/dashboard" class="btn btn-outline-secondary">Back to Dashboard</a>
    </div>
</div>

<%--<jsp:include page="fragments/footer.jsp" />--%>

<!-- Toast -->
<div class="position-fixed bottom-0 end-0 p-3" style="z-index: 11">
    <div id="toastMessage" class="toast align-items-center text-white bg-success border-0" role="alert">
        <div class="d-flex">
            <div class="toast-body"></div>
            <button type="button" class="btn-close btn-close-white me-2 m-auto" data-bs-dismiss="toast"></button>
        </div>
    </div>
</div>

<script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/js/bootstrap.bundle.min.js"></script>
<script>
    const token = localStorage.getItem("token");
    let students = [];
    const today = new Date();
    const todayDate = today.getFullYear() + '-' +
        String(today.getMonth() + 1).padStart(2, '0') + '-' +
        String(today.getDate()).padStart(2, '0');

    document.getElementById("attendanceDate").value = todayDate;

    async function fetchCourses() {
        const res = await fetch("/api/courses/assigned-to-me", { headers: { Authorization: "Bearer " + token }});
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
            fetchStudents(preselected);
        }
    }

    async function fetchStudents(courseId) {
        const res = await fetch(`/api/enrollments/students-by-course/${courseId}`, {
            headers: { Authorization: "Bearer " + token }
        });
        students = await res.json();
        renderStudentCheckboxes();
    }

    function renderStudentCheckboxes() {
        const container = document.getElementById("studentList");
        container.innerHTML = "";

        if (students.length === 0) {
            container.innerHTML = "<div class='alert alert-warning'>🚫 No students are enrolled in this course.</div>";
            return;
        }

        container.innerHTML = "<h5>Mark Present</h5>";
        students.forEach(student => {
            const div = document.createElement("div");
            div.className = "form-check";
            div.innerHTML = `
                <input class="form-check-input" type="checkbox" value="${student.id}" id="s-${student.id}">
                <label class="form-check-label" for="s-${student.id}">
                    ${student.username} (${student.rollNumber})
                </label>
            `;
            container.appendChild(div);
        });
    }

    function showToast(message, isError = false) {
        const toastEl = document.getElementById("toastMessage");
        toastEl.classList.remove("bg-success", "bg-danger");
        toastEl.classList.add(isError ? "bg-danger" : "bg-success");
        toastEl.querySelector(".toast-body").textContent = message;
        const toast = new bootstrap.Toast(toastEl);
        toast.show();
    }

    document.getElementById("courseSelect").addEventListener("change", async function () {
        const courseId = this.value;
        const url = new URL(window.location.href);
        url.searchParams.set("courseId", courseId);
        window.history.replaceState({}, "", url);

        const checkRes = await fetch(`/api/attendance/check-today?courseId=${courseId}`, {
            headers: { Authorization: "Bearer " + token }
        });
        const check = await checkRes.json();

        if (check.alreadyMarked) {
            showToast("✅ Attendance already marked for today.", true);
            document.getElementById("submitBtn").disabled = true;
            document.getElementById("studentList").innerHTML = "";
        } else {
            document.getElementById("submitBtn").disabled = false;
            fetchStudents(courseId);
        }
    });

    document.getElementById("submitBtn").addEventListener("click", async function () {
        const courseId = document.getElementById("courseSelect").value;

        if (!courseId) {
            showToast("❗ Please select a course.", true);
            return;
        }

        if (students.length === 0) {
            showToast("❗ No students enrolled.", true);
            return;
        }

        const checked = Array.from(document.querySelectorAll("#studentList input:checked"))
            .map(cb => parseInt(cb.value));

        if (checked.length === 0) {
            const proceed = confirm("⚠️ You didn’t mark anyone present. All students will be marked absent. Continue?");
            if (!proceed) return;
        }

        const res = await fetch("/api/attendance/mark", {
            method: "POST",
            headers: {
                "Content-Type": "application/json",
                Authorization: "Bearer " + token
            },
            body: JSON.stringify({ courseId: parseInt(courseId), date: todayDate, presentStudentIds: checked })
        });

        const msg = await res.json();
        if (!res.ok) {
            showToast("❌ " + msg.message, true);
            return;
        }
        showToast("✅ " + msg.message);
        document.getElementById("submitBtn").disabled = true;
        document.querySelectorAll("#studentList input").forEach(cb => cb.disabled = true);
    });

    document.addEventListener("DOMContentLoaded", fetchCourses);
</script>
</body>
</html>
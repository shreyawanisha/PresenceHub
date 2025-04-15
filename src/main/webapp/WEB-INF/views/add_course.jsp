<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ page isELIgnored="true" %>
<!DOCTYPE html>
<html>
<head>
    <title>Add New Course - Admin</title>
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/css/bootstrap.min.css" rel="stylesheet"/>
    <style>
        html, body {
            margin: 0;
            padding: 0;
            height: 100%;
            font-family: 'Segoe UI', sans-serif;
            background-color: #f8f9fa;
        }
        main {
            min-height: calc(100vh - 100px);
            padding: 30px 20px 40px;
        }
        footer {
            background-color: #f8f9fa;
            text-align: center;
            padding: 15px;
            font-size: 14px;
            color: #888;
        }
    </style>
</head>
<body>
<jsp:include page="fragments/navbar.jsp" />
<main class="container">
    <h2 class="mb-4 text-center">➕ Add New Course</h2>

    <div id="responseMessage" class="alert d-none" role="alert"></div>

    <form id="addCourseForm">
        <div class="mb-3">
            <label class="form-label">CRN</label>
            <input type="text" class="form-control" id="crn" required>
        </div>
        <div class="mb-3">
            <label class="form-label">Course Name</label>
            <input type="text" class="form-control" id="courseName" required>
        </div>
        <div class="mb-3">
            <label class="form-label">Department</label>
            <input type="text" class="form-control" id="department" required>
        </div>
        <div class="mb-3">
            <label class="form-label">Semester</label>
            <select class="form-select" id="semester" required>
                <option value="">-- Select Semester --</option>
                <option value="SEM1">SEM1</option>
                <option value="SEM2">SEM2</option>
                <option value="SEM3">SEM3</option>
                <option value="SEM4">SEM4</option>
                <option value="SEM5">SEM5</option>
                <option value="SEM6">SEM6</option>
                <option value="SEM7">SEM7</option>
                <option value="SEM8">SEM8</option>
            </select>
        </div>
        <button type="submit" class="btn btn-primary">Add Course</button>
    </form>
    <div class="mt-4 text-end">
        <a href="/admin/dashboard" class="btn btn-outline-secondary">🏠 Back to Dashboard</a>
    </div>
</main>
<footer>
    © 2025 PresenceHub. Built by <a href="https://www.linkedin.com/in/shreya-wanisha" target="_blank">Shreya Wanisha</a>
</footer>
<script>
    document.getElementById("addCourseForm").addEventListener("submit", async function (e) {
        e.preventDefault();
        const token = localStorage.getItem("token");

        const crn = document.getElementById("crn").value;
        const courseName = document.getElementById("courseName").value;
        const department = document.getElementById("department").value;
        const semester = document.getElementById("semester").value;

        const res = await fetch("/api/courses", {
            method: "POST",
            headers: {
                "Content-Type": "application/json",
                "Authorization": "Bearer " + token
            },
            body: JSON.stringify({ crn, courseName, department, semester })
        });

        const msg = await res.json();
        const responseDiv = document.getElementById("responseMessage");
        responseDiv.classList.remove("d-none");
        responseDiv.classList.remove("alert-success", "alert-danger");
        responseDiv.classList.add("alert-" + (res.ok ? "success" : "danger"));
        responseDiv.textContent = msg.message;

        if (res.ok) {
            document.getElementById("addCourseForm").reset();
        }
    });
</script>
</body>
</html>

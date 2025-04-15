<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ page isELIgnored="true" %>
<!DOCTYPE html>
<html>
<head>
  <title>Assign Faculty to Course - Admin</title>
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
    .form-check {
      margin-bottom: 8px;
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
  <h2 class="mb-4 text-center">👩‍🏫 Assign Faculty to a Course</h2>

  <div id="responseMessage" class="alert d-none" role="alert"></div>

  <form id="assignFacultyForm">
    <div class="mb-3">
      <label class="form-label">Select Course</label>
      <select class="form-select" id="courseSelect" required></select>
    </div>
    <div class="mb-3">
      <label class="form-label">Select Faculties</label>
      <div id="facultyCheckboxes"></div>
    </div>
    <button type="submit" class="btn btn-primary">Assign Faculty</button>
  </form>
  <div class="mt-4 text-end">
    <a href="/admin/dashboard" class="btn btn-outline-secondary">🏠 Back to Dashboard</a>
  </div>
</main>
<footer>
  © 2025 PresenceHub. Built by <a href="https://www.linkedin.com/in/shreya-wanisha" target="_blank">Shreya Wanisha</a>
</footer>
<script>
  document.addEventListener("DOMContentLoaded", async function () {
    const token = localStorage.getItem("token");
    const courseSelect = document.getElementById("courseSelect");
    const facultyCheckboxes = document.getElementById("facultyCheckboxes");

    const [coursesRes, facultiesRes] = await Promise.all([
      fetch("/api/courses", { headers: { Authorization: "Bearer " + token } }),
      fetch("/api/faculties", { headers: { Authorization: "Bearer " + token } })
    ]);

    const courses = await coursesRes.json();
    const faculties = await facultiesRes.json();

    courses.forEach(course => {
      const opt = document.createElement("option");
      opt.value = course.id;
      opt.textContent = course.courseName + " (" + course.crn + ")";
      courseSelect.appendChild(opt);
    });

    faculties.forEach(fac => {
      const div = document.createElement("div");
      div.className = "form-check";
      div.innerHTML = `
        <input class="form-check-input" type="checkbox" value="${fac.userId || fac.id}" id="fac-${fac.userId || fac.id}">
        <label class="form-check-label" for="fac-${fac.userId || fac.id}">
          ${fac.username} (${fac.email})
        </label>`;
      facultyCheckboxes.appendChild(div);
    });
  });

  document.getElementById("assignFacultyForm").addEventListener("submit", async function (e) {
    e.preventDefault();
    const token = localStorage.getItem("token");
    const button = this.querySelector("button[type='submit']");
    button.disabled = true;

    const courseId = document.getElementById("courseSelect").value;
    const facultyIds = Array.from(document.querySelectorAll("#facultyCheckboxes input:checked"))
            .map(cb => parseInt(cb.value));

    const res = await fetch("/api/courses/assign-faculties", {
      method: "POST",
      headers: {
        "Content-Type": "application/json",
        "Authorization": "Bearer " + token
      },
      body: JSON.stringify({ courseId, facultyIds })
    });

    const msg = await res.json();
    const responseDiv = document.getElementById("responseMessage");
    responseDiv.classList.remove("d-none", "alert-success", "alert-danger");
    responseDiv.classList.add("alert-" + (res.ok ? "success" : "danger"));
    responseDiv.textContent = msg.message;

    button.disabled = false;

    if (res.ok) {
      this.reset();
    }
  });
</script>
</body>
</html>

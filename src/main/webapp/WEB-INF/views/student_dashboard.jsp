<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
`  <%@ page isELIgnored="true" %>
`<!DOCTYPE html>
<html>
<head>
  <title>Student Dashboard - PresenceHub</title>
  <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/css/bootstrap.min.css" rel="stylesheet"/>
  <style>
    body {
      display: flex;
      flex-direction: column;
      min-height: 100vh;
      font-family: 'Segoe UI', sans-serif;
    }
    .dashboard-container {
      flex: 1;
      padding: 50px 0;
    }
    footer {
      background-color: #f8f9fa;
      padding: 15px 0;
    }
  </style>
</head>
<body>

<jsp:include page="fragments/navbar.jsp" />

<div class="container dashboard-container">
  <h2 class="dashboard-title mb-4">📚 Your Enrolled Courses</h2>
  <div id="courseList" class="row"></div>
  <div id="emptyMessage" class="text-muted mt-3" style="display: none;">
    You are not enrolled in any courses yet.
  </div>
</div>

<script>
  document.addEventListener("DOMContentLoaded", async function () {
    const token = localStorage.getItem("token");

    if (!token) {
      window.location.href = "/login";
      return;
    }

    try {
      const response = await fetch("/api/enrollments/my-courses", {
        headers: {
          "Authorization": "Bearer " + token
        }
      });

      if (!response.ok) {
        throw new Error("Failed to fetch enrolled courses");
      }

      const courses = await response.json();
      console.log(courses);
      const container = document.getElementById("courseList");
      const emptyMessage = document.getElementById("emptyMessage");

      if (courses.length === 0) {
        emptyMessage.style.display = "block";
        return;
      }

      courses.forEach(course => {
        const div = document.createElement("div");
        div.className = "col-md-6";

        let facultyListHTML = "";

        if (course.faculties && Array.isArray(course.faculties) && course.faculties.length > 0) {
          facultyListHTML = '<ul class="mb-0">';
          course.faculties.forEach(faculty => {
            facultyListHTML += `<li>${faculty.username} (${faculty.email})</li>`;
          });
          facultyListHTML += '</ul>';
        } else {
          facultyListHTML = `<span class="text-muted">No faculty assigned yet</span>`;
        }
        console.log(course);
        div.innerHTML = `
  <div class="card course-card shadow-sm">
    <div class="card-body">
      <h5 class="card-title text-primary">${course.courseName}</h5>
      <p class="card-text"><strong>CRN:</strong> ${course.crn}</p>
      <p class="card-text"><strong>Department:</strong> ${course.department}</p>
      <p class="card-text"><strong>Semester:</strong> ${course.semester}</p>
      <h6 class="card-subtitle mb-2 text-muted"><strong>Faculty:</strong> ${facultyListHTML}</h6>
    </div>
  </div>
`;

        container.appendChild(div);
      });

    } catch (err) {
      console.error("Error loading courses:", err);
    }
  });
</script>

<jsp:include page="fragments/footer.jsp" />

</body>
</html>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ page isELIgnored="true" %>
<!DOCTYPE html>
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
    .course-card {
      height: 100%;
    }
  </style>
</head>
<body>

<jsp:include page="fragments/navbar.jsp" />

<div class="container dashboard-container">
  <div id="notAssignedAlert" class="alert alert-warning text-center mt-4" style="display: none;">
    🚫 You are not yet assigned as a student. Please contact the admin.
  </div>

  <div id="studentDashboardContent" style="display: none;">
    <div class="d-flex justify-content-between align-items-center mb-4">
      <h2 class="mb-0">📚 Your Enrolled Courses</h2>
    </div>
    <div id="enrolledCourses" class="row mb-5"></div>

    <div class="mb-3">
      <h4 class="mb-1">🎓 Available Courses to Enroll</h4>
      <p class="text-muted">You can enroll in a maximum of <strong>2 courses</strong>.</p>
      <button class="btn btn-outline-secondary" type="button" id="toggleCourseBtn">
        🔽 Show Courses
      </button>
      <div class="collapse mt-3" id="availableCourses">
        <div class="row" id="availableCoursesList"></div>
      </div>
    </div>
  </div>
</div>

<jsp:include page="fragments/footer.jsp" />

<script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/js/bootstrap.bundle.min.js"></script>

<script>
  document.addEventListener("DOMContentLoaded", async function () {
    const token = localStorage.getItem("token");
    if (!token) return (window.location.href = "/login");

    try {
      const checkResponse = await fetch("/api/students/me", {
        headers: { Authorization: "Bearer " + token }
      });

      if (!checkResponse.ok) {
        document.getElementById("notAssignedAlert").style.display = "block";
        return;
      }

      document.getElementById("studentDashboardContent").style.display = "block";

      const [enrolledRes, allCoursesRes, summaryRes] = await Promise.all([
        fetch("/api/enrollments/my-courses", { headers: { Authorization: "Bearer " + token } }),
        fetch("/api/courses", { headers: { Authorization: "Bearer " + token } }),
        fetch("/api/attendance/summary/my-courses", { headers: { Authorization: "Bearer " + token } })
      ]);

      const enrolledCourses = await enrolledRes.json();
      const allCourses = await allCoursesRes.json();
      const summaries = await summaryRes.json();

      const summaryMap = {};
      summaries.forEach(s => summaryMap[s.crn] = s);

      const enrolledIds = enrolledCourses.map(c => c.id);
      const enrolledContainer = document.getElementById("enrolledCourses");
      const availableContainer = document.getElementById("availableCoursesList");

      if (enrolledCourses.length === 0) {
        enrolledContainer.innerHTML = '<div class="text-muted">You are not enrolled in any courses yet.</div>';
      } else {
        enrolledCourses.forEach(course => {
          enrolledContainer.innerHTML += createCourseCardWithSummary(course, summaryMap[course.crn], false);
        });
      }

      const canEnroll = enrolledCourses.length < 2;
      const availableToShow = allCourses.filter(c => !enrolledIds.includes(c.id));

      if (availableToShow.length === 0) {
        availableContainer.innerHTML = '<div class="text-muted">No more courses available for enrollment.</div>';
      } else {
        availableToShow.forEach(course => {
          availableContainer.innerHTML += createCourseCardWithSummary(course, null, canEnroll);
        });
      }

      const collapseEl = document.getElementById("availableCourses");
      const toggleBtn = document.getElementById("toggleCourseBtn");
      const collapseInstance = new bootstrap.Collapse(collapseEl, { toggle: false });

      toggleBtn.addEventListener("click", () => {
        const isShown = collapseEl.classList.contains("show");
        if (isShown) {
          collapseInstance.hide();
          toggleBtn.innerHTML = "🔽 Show Courses";
        } else {
          collapseInstance.show();
          toggleBtn.innerHTML = "🔼 Hide Courses";
        }
      });

    } catch (err) {
      console.error("Error loading student dashboard:", err);
      document.getElementById("notAssignedAlert").textContent = "❌ Error loading dashboard. Please try again.";
      document.getElementById("notAssignedAlert").style.display = "block";
    }
  });

  function createCourseCardWithSummary(course, summary, showEnroll) {
    let html = '<div class="col-md-6 mb-4">' +
            '<div class="card course-card shadow-sm">' +
            '<div class="card-body">' +
            '<h5 class="card-title text-primary">' + course.courseName + '</h5>' +
            '<p class="card-text"><strong>CRN:</strong> ' + course.crn + '</p>' +
            '<p class="card-text"><strong>Department:</strong> ' + course.department + '</p>' +
            '<p class="card-text"><strong>Semester:</strong> ' + course.semester + '</p>' +
            '<h6 class="card-subtitle mb-2 text-muted">Faculty</h6>' +
            renderFacultyList(course.faculties);

    if (summary) {
      html += `<p class="card-text">
              <strong>Attendance:</strong> ${summary.presentCount} / ${summary.totalClasses}
              (${summary.percentage}% present)
           </p>
           <div class="d-flex gap-2 mt-3">
              <a href="/student/attendance/view?courseId=${course.id}" class="btn btn-sm btn-outline-primary">📊 View Records</a>
              <a href="/student/scan?courseId=${course.id}" class="btn btn-sm btn-dark">📷 Scan QR</a>
           </div>`;
    }

    if (showEnroll) {
      html += '<button class="btn btn-sm btn-success mt-3" onclick="enrollInCourse(' + course.id + ')">Enroll</button>';
    }

    html += '</div></div></div>';
    return html;
  }

  function renderFacultyList(faculties) {
    if (!faculties || faculties.length === 0) {
      return '<span class="text-muted">No faculty assigned yet</span>';
    }
    return '<ul>' + faculties.map(f => '<li>' + f.username + ' (' + f.email + ')</li>').join('') + '</ul>';
  }

  async function enrollInCourse(courseId) {
    const token = localStorage.getItem("token");
    const res = await fetch("/api/enrollments/self-enroll", {
      method: "POST",
      headers: {
        "Content-Type": "application/json",
        "Authorization": "Bearer " + token
      },
      body: JSON.stringify({ courseId })
    });

    const msg = await res.json();
    if (res.ok) {
      alert("🎉 Enrolled successfully!");
      location.reload();
    } else {
      alert("❌ " + msg.message);
    }
  }
</script>
</body>
</html>

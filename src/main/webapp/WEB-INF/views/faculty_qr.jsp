<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ page isELIgnored="true" %>
<!DOCTYPE html>
<html>
<head>
  <title>QR Code - PresenceHub</title>
  <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/css/bootstrap.min.css" rel="stylesheet"/>
  <style>
    #countdown {
      font-weight: bold;
      margin-top: 10px;
      color: #dc3545;
    }
  </style>
</head>
<body>
<jsp:include page="fragments/navbar.jsp"/>

<div class="container my-5 text-center">
  <h2>📷 QR Code for Attendance</h2>
  <h5 id="courseTitle" class="mb-4 text-muted">Selected Course</h5>

  <button id="generateBtn" class="btn btn-primary me-2">Generate QR Code</button>
  <button id="endBtn" class="btn btn-danger" style="display: none;">End QR</button>
  <a href="/faculty/dashboard" class="btn btn-secondary ms-2">Back to Dashboard</a>

  <div id="qrSection" class="mt-4" style="display:none;">
    <p class="text-muted">Let your students scan this QR code to mark attendance.</p>
    <img id="qrImage" src="" alt="QR Code" class="img-thumbnail" />
    <p id="countdown"></p>
  </div>
</div>

<script>
  let countdownInterval;
  let selectedCourseId = null;

  document.addEventListener("DOMContentLoaded", async () => {
    const token = localStorage.getItem("token");
    const courseId = new URLSearchParams(window.location.search).get("courseId");

    if (!token || !courseId) {
      alert("Invalid or missing course. Redirecting...");
      window.location.href = "/faculty/dashboard";
      return;
    }

    selectedCourseId = courseId;

    try {
      const res = await fetch("/api/courses/assigned-to-me", {
        headers: { Authorization: "Bearer " + token }
      });

      if (!res.ok) throw new Error("Could not fetch courses.");
      const courses = await res.json();
      const course = courses.find(c => c.id == courseId);

      if (!course) throw new Error("Course not assigned.");

      document.getElementById("courseTitle").textContent = `${course.courseName} (${course.crn})`;

    } catch (err) {
      console.error(err);
      alert("Failed to load course.");
    }

    document.getElementById("generateBtn").addEventListener("click", generateQR);
    document.getElementById("endBtn").addEventListener("click", endQR);
  });

  async function generateQR() {
    const token = localStorage.getItem("token");
    const generateBtn = document.getElementById("generateBtn");
    const endBtn = document.getElementById("endBtn");

    try {
      generateBtn.disabled = true;

      const res = await fetch(`/api/attendance/generate?courseId=${selectedCourseId}`, {
        method: "POST",
        headers: { Authorization: "Bearer " + token }
      });

      const data = await res.json();
      if (!res.ok) throw new Error(data.message || "QR generation failed");

      document.getElementById("qrImage").src =
              "https://api.qrserver.com/v1/create-qr-code/?size=250x250&data=" + encodeURIComponent(data.message);

      document.getElementById("qrSection").style.display = "block";
      endBtn.style.display = "inline-block";
      startCountdown(300);

    } catch (err) {
      alert("❌ QR generation failed.");
      console.error(err);
    } finally {
      generateBtn.disabled = false;
    }
  }

  async function endQR() {
    const token = localStorage.getItem("token");

    try {
      const res = await fetch(`/api/attendance/end-qr?courseId=${selectedCourseId}`, {
        method: "POST",
        headers: { Authorization: "Bearer " + token }
      });

      if (!res.ok) throw new Error("Failed to end QR");

      clearInterval(countdownInterval);
      document.getElementById("countdown").textContent = "🛑 QR session ended.";
      document.getElementById("endBtn").disabled = true;
      document.getElementById("generateBtn").disabled = false;

    } catch (err) {
      console.error(err);
      alert("Could not end session.");
    }
  }

  function startCountdown(seconds) {
    clearInterval(countdownInterval);
    const countdownEl = document.getElementById("countdown");

    function updateDisplay() {
      const min = Math.floor(seconds / 60);
      const sec = seconds % 60;
      countdownEl.textContent = `⏳ QR expires in ${min}:${sec.toString().padStart(2, '0')}`;

      if (seconds <= 0) {
        countdownEl.textContent = "🔄 Refreshing QR...";
        generateQR();
        return;
      }

      seconds--;
    }

    updateDisplay();
    countdownInterval = setInterval(updateDisplay, 1000);
  }
</script>
</body>
</html>
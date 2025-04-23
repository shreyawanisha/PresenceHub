<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ page isELIgnored="true" %>
<!DOCTYPE html>
<html>
<head>
    <title>Generate QR Code - PresenceHub</title>
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/css/bootstrap.min.css" rel="stylesheet"/>
    <style>
        body {
            background-color: #f8f9fa;
            font-family: 'Segoe UI', sans-serif;
            display: flex;
            flex-direction: column;
            min-height: 100vh;
        }
        .container {
            padding-top: 50px;
            flex: 1;
        }
        .qr-container {
            margin-top: 30px;
            text-align: center;
        }
        .qr-img {
            border: 1px solid #ccc;
            padding: 10px;
            background: white;
        }
        footer {
            background-color: #f8f9fa;
            text-align: center;
            padding: 15px 0;
            font-size: 14px;
            color: #888;
        }
    </style>
</head>
<body>

<jsp:include page="fragments/navbar.jsp"/>

<div class="container">
    <h2 class="text-center mb-4">📲 QR Code Generator</h2>

    <div class="row justify-content-center">
        <div class="col-md-6">
            <label class="form-label">Select Course</label>
            <select class="form-select" id="courseSelect">
                <option value="">-- Choose a Course --</option>
            </select>
            <button class="btn btn-primary mt-3 w-100" id="generateBtn">Generate QR Code</button>
        </div>
    </div>

    <div class="qr-container" id="qrContainer" style="display: none;">
        <h5 class="mt-4">🕒 Valid for 5 minutes</h5>
        <img id="qrImage" class="qr-img" alt="QR Code"/>
    </div>
</div>

<jsp:include page="fragments/footer.jsp"/>

<script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/js/bootstrap.bundle.min.js"></script>
<script>
    document.addEventListener("DOMContentLoaded", async function () {
        const token = localStorage.getItem("token");
        const courseSelect = document.getElementById("courseSelect");
        const generateBtn = document.getElementById("generateBtn");
        const qrContainer = document.getElementById("qrContainer");
        const qrImage = document.getElementById("qrImage");

        // Load assigned courses
        const res = await fetch("/api/courses/assigned-to-me", {
            headers: { Authorization: "Bearer " + token }
        });

        const courses = await res.json();
        courses.forEach(course => {
            const opt = document.createElement("option");
            opt.value = course.id;
            opt.textContent = `${course.courseName} (${course.crn})`;
            courseSelect.appendChild(opt);
        });

        // Generate QR Code
        generateBtn.addEventListener("click", async () => {
            const courseId = courseSelect.value;
            if (!courseId) {
                alert("Please select a course!");
                return;
            }

            const qrRes = await fetch(`/api/qr/generate?courseId=${courseId}`, {
                method: "POST",
                headers: { Authorization: "Bearer " + token }
            });

            const result = await qrRes.json();
            if (!qrRes.ok) {
                alert(result.message || "Failed to generate QR token");
                return;
            }

            const token = result.message;
            const encoded = encodeURIComponent(token);
            const qrUrl = `https://api.qrserver.com/v1/create-qr-code/?size=200x200&data=${encoded}`;

            qrImage.src = qrUrl;
            qrContainer.style.display = "block";
        });
    });
</script>

</body>
</html>
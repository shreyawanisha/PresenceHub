<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page isELIgnored="true" %>
<!DOCTYPE html>
<html>
<head>
    <title>QR Code Generator - PresenceHub</title>
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/css/bootstrap.min.css" rel="stylesheet"/>
    <script src="https://cdn.jsdelivr.net/npm/qrcode/build/qrcode.min.js"></script>
    <style>
        body {
            font-family: 'Segoe UI', sans-serif;
            background-color: #f8f9fa;
            display: flex;
            flex-direction: column;
            min-height: 100vh;
        }
        .dashboard-container {
            flex: 1;
            padding: 40px 0;
        }
        .qr-box {
            text-align: center;
            padding: 20px;
            border-radius: 12px;
            background-color: white;
            box-shadow: 0 0 10px rgba(0,0,0,0.1);
        }
        #qrCodeCanvas {
            margin-top: 20px;
        }
    </style>
</head>
<body>
<jsp:include page="fragments/navbar.jsp" />

<div class="container dashboard-container">
    <h2 class="text-center mb-4">📸 Generate Attendance QR</h2>

    <div class="row justify-content-center">
        <div class="col-md-6 qr-box">
            <label for="courseSelect" class="form-label">Select Course</label>
            <select class="form-select mb-3" id="courseSelect">
                <option value="">-- Choose Course --</option>
            </select>
            <button class="btn btn-primary w-100" id="generateBtn">🔄 Generate QR Code</button>

            <canvas id="qrCodeCanvas" class="mt-4"></canvas>
            <div id="qrMeta" class="text-muted mt-3" style="font-size: 14px;"></div>
        </div>
    </div>
</div>

<jsp:include page="fragments/footer.jsp" />

<script>
    const token = localStorage.getItem("token");
    let refreshTimer;

    async function loadCourses() {
        const res = await fetch("/api/courses/assigned-to-me", {
            headers: { Authorization: "Bearer " + token }
        });
        const data = await res.json();
        const select = document.getElementById("courseSelect");
        data.forEach(c => {
            const opt = document.createElement("option");
            opt.value = c.id;
            opt.textContent = `${c.courseName} (${c.crn})`;
            select.appendChild(opt);
        });
    }

    async function generateQRCode(auto = false) {
        const courseId = document.getElementById("courseSelect").value;
        if (!courseId) return !auto && alert("Please select a course!");

        const res = await fetch(`/api/qr/generate?courseId=${courseId}`, {
            method: "POST",
            headers: { Authorization: "Bearer " + token }
        });

        const result = await res.json();
        if (!res.ok) return alert(result.message || "Error generating QR");

        const qrToken = result.message;
        const qrCanvas = document.getElementById("qrCodeCanvas");
        const qrMeta = document.getElementById("qrMeta");

        QRCode.toCanvas(qrCanvas, qrToken, { width: 250 }, function (error) {
            if (error) console.error(error);
        });

        const expiry = new Date(Date.now() + 5 * 60 * 1000);
        qrMeta.innerText = `QR valid until: ${expiry.toLocaleTimeString()}`;

        clearInterval(refreshTimer);
        refreshTimer = setInterval(() => generateQRCode(true), 5 * 60 * 1000);
    }

    document.addEventListener("DOMContentLoaded", loadCourses);
    document.getElementById("generateBtn").addEventListener("click", () => generateQRCode(false));
</script>

</body>
</html>

<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page isELIgnored="true" %>
<!DOCTYPE html>
<html>
<head>
    <title>Scan QR - PresenceHub</title>
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/css/bootstrap.min.css" rel="stylesheet"/>
    <script src="https://unpkg.com/html5-qrcode" type="text/javascript"></script>
    <style>
        body {
            background-color: #f8f9fa;
            font-family: 'Segoe UI', sans-serif;
        }

        .scan-container {
            display: flex;
            flex-direction: column;
            align-items: center;
            justify-content: center;
            min-height: 90vh;
        }

        #reader {
            width: 300px;
            margin-bottom: 20px;
        }

        #statusMessage {
            font-size: 1.1rem;
            margin-top: 10px;
        }
    </style>
</head>
<body>
<jsp:include page="fragments/navbar.jsp"/>

<div class="container scan-container text-center">
    <h2 class="mb-4">📸 Scan QR to Mark Attendance</h2>
    <div id="reader"></div>
    <div id="statusMessage" class="fw-bold text-muted"></div>
    <a href="/student/dashboard" class="btn btn-secondary mt-3">⬅️ Back to Dashboard</a>
</div>

<script>
    document.addEventListener("DOMContentLoaded", function () {
        const token = localStorage.getItem("token");
        const statusMessage = document.getElementById("statusMessage");

        if (!token) {
            alert("❗ You are not logged in.");
            window.location.href = "/login.jsp";
            return;
        }

        const courseId = new URLSearchParams(window.location.search).get("courseId");
        if (!courseId) {
            statusMessage.innerHTML = `<span class="text-danger">❗ No course selected. Please go back and choose a course.</span>`;
            return;
        }

        let isProcessing = false;
        let html5QrcodeScanner;

        function onScanSuccess(qrToken) {
            if (isProcessing) return;
            isProcessing = true;

            html5QrcodeScanner.clear();
            statusMessage.innerHTML = `<span class="text-muted">⏳ Verifying attendance...</span>`;

            fetch("/api/attendance/mark-qr", {
                method: "POST",
                headers: {
                    "Content-Type": "application/json",
                    "Authorization": "Bearer " + token
                },
                body: JSON.stringify({ qrToken })
            })
                .then(res => res.json())
                .then(data => {
                    if (data.statusCode === 200) {
                        statusMessage.innerHTML = `<span class="text-success">✅ ${data.message}</span>`;
                        setTimeout(() => window.location.href = "/student/dashboard", 1500);
                    } else {
                        throw new Error(data.message || "Unexpected error");
                    }
                })
                .catch(err => {
                    statusMessage.innerHTML = `<span class="text-danger">❌ ${err.message}</span>`;
                    isProcessing = false; // allow retry
                });
        }

        fetch(`/api/attendance/has-active-qr?courseId=${courseId}`, {
            headers: { Authorization: "Bearer " + token }
        })
            .then(res => res.json())
            .then(data => {
                if (!data.active) {
                    statusMessage.innerHTML = `<span class="text-danger">❌ No active QR session for this course.</span>`;
                    return;
                }

                html5QrcodeScanner = new Html5QrcodeScanner(
                    "reader",
                    { fps: 10, qrbox: 250 },
                    false
                );
                html5QrcodeScanner.render(onScanSuccess);
            })
            .catch(err => {
                statusMessage.innerHTML = `<span class="text-danger">❌ Error checking QR session.</span>`;
                console.error(err);
            });
    });
</script>
</body>
</html>
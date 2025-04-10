<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!DOCTYPE html>
<html>
<head>
    <title>Welcome to PresenceHub</title>
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/css/bootstrap.min.css" rel="stylesheet"/>
    <style>
        body {
            display: flex;
            flex-direction: column;
            min-height: 100vh;
            font-family: 'Segoe UI', sans-serif;
        }

        .main-content {
            flex: 1;
            padding: 60px 0;
            background: linear-gradient(to bottom right, #e3f2fd, #ffffff);
        }

        .info-section {
            display: flex;
            align-items: center;
            justify-content: center;
            flex-wrap: wrap;
            gap: 40px;
        }

        .info-text {
            max-width: 500px;
        }

        .info-text h1 {
            font-size: 3rem;
            font-weight: bold;
            color: #0d6efd;
        }

        .info-text p {
            font-size: 1.2rem;
            margin-top: 15px;
            color: #333;
        }

        /*.banner-img {*/
        /*    max-width: 500px;*/
        /*    border-radius: 10px;*/
        /*    box-shadow: 0 5px 25px rgba(0, 0, 0, 0.2);*/
        /*}*/

        .banner-img {
            max-width: 500px;
            border-radius: 12px;
            background: rgba(255, 255, 255, 0.5);
            padding: 10px;
            box-shadow: 0 2px 10px rgba(0, 0, 0, 0.08); /* softer shadow */
        }

        footer {
            background-color: #f8f9fa;
            padding: 15px 0;
        }
    </style>
</head>
<script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/js/bootstrap.bundle.min.js"></script>
<body>

<!-- Navbar -->
<nav class="navbar navbar-expand-lg navbar-dark bg-primary">
    <div class="container">
        <a class="navbar-brand fw-bold" href="/">PresenceHub</a>
        <button class="navbar-toggler" type="button" data-bs-toggle="collapse"
                data-bs-target="#navbarNav" aria-controls="navbarNav"
                aria-expanded="false" aria-label="Toggle navigation">
            <span class="navbar-toggler-icon"></span>
        </button>
        <div class="collapse navbar-collapse justify-content-end" id="navbarNav">
            <div class="navbar-nav" id="auth-links">
                <a class="nav-link" href="/login">Login</a>
                <a class="nav-link" href="/register">Register</a>
            </div>
        </div>
    </div>
</nav>

<!-- Main Section -->
<div class="main-content">
    <div class="container info-section">
        <div class="info-text">
            <h1>Welcome to PresenceHub</h1>
            <p>
                Say goodbye to paper registers and complicated systems.
                PresenceHub is your go-to platform for effortless attendance tracking.
            </p>
            <p>
                🎓 Students can register and view attendance instantly. <br>
                👩‍🏫 Faculty can manage students across multiple courses. <br>
                🛡️ Admins get full control and visibility over everything.
            </p>
            <a href="/register" class="btn btn-primary btn-lg mt-3">Get Started</a>
        </div>
        <img src="<c:url value='/images/home.png'/>" alt="PresenceHub Preview" class="banner-img">
    </div>
</div>

<!-- Footer -->
<footer class="text-center text-muted">
    <p class="mb-0">&copy; 2025 PresenceHub. Built with ❤️ by Shreya.</p>
</footer>

<script>
    document.addEventListener("DOMContentLoaded", function () {
        const token = localStorage.getItem("token");
        const authLinks = document.getElementById("auth-links");

        if (token) {
            authLinks.innerHTML = `
                <a class="nav-link" href="#" onclick="logout()">Logout</a>
            `;
        } else {
            authLinks.innerHTML = `
                <a class="nav-link" href="/login">Login</a>
                <a class="nav-link" href="/register">Register</a>
            `;
        }

        // Show toast message if loggedOut flag is set
        if (sessionStorage.getItem("loggedOut")) {
            showToast("You have been logged out successfully.");
            sessionStorage.removeItem("loggedOut");
        }
    });

    function logout() {
        localStorage.removeItem("token");
        sessionStorage.setItem("loggedOut", "true"); // so we show message after redirect
        window.location.href = "/";
    }

    function showToast(message) {
        const toast = document.createElement("div");
        toast.className = "toast align-items-center text-white bg-success border-0 position-fixed bottom-0 end-0 m-4";
        toast.setAttribute("role", "alert");
        toast.setAttribute("aria-live", "assertive");
        toast.setAttribute("aria-atomic", "true");
        toast.innerHTML = `
            <div class="d-flex">
                <div class="toast-body">
                    ${message}
                </div>
                <button type="button" class="btn-close btn-close-white me-2 m-auto"
                        data-bs-dismiss="toast" aria-label="Close"></button>
            </div>
        `;
        document.body.appendChild(toast);
        const bsToast = new bootstrap.Toast(toast);
        bsToast.show();

        // Auto remove after shown
        toast.addEventListener("hidden.bs.toast", () => {
            document.body.removeChild(toast);
        });
    }
</script>
</body>
</html>
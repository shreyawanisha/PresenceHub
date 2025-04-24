<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ page isELIgnored="true" %>
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

        .banner-img {
            max-width: 500px;
            border-radius: 12px;
            background: rgba(255, 255, 255, 0.5);
            padding: 10px;
            box-shadow: 0 2px 10px rgba(0, 0, 0, 0.08);
        }

        footer {
            background-color: #f8f9fa;
            padding: 15px 0;
        }
    </style>
</head>
<body>

<!-- Navbar -->
<jsp:include page="fragments/navbar.jsp" />

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

            <!-- Dynamic Button -->
            <div id="mainAction"></div>
        </div>
        <img src="<c:url value='/images/home.png'/>" alt="PresenceHub Preview" class="banner-img">
    </div>
</div>

<jsp:include page="fragments/footer.jsp" />

<script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/js/bootstrap.bundle.min.js"></script>
<script>
    document.addEventListener("DOMContentLoaded", function () {
        const token = localStorage.getItem("token");
        const authLinks = document.getElementById("auth-links");
        const mainAction = document.getElementById("mainAction");

        if (token) {
            try {
                const payload = JSON.parse(atob(token.split('.')[1]));
                const role = payload.role;
                const email = payload.sub;

                let dashboardUrl = "#";
                if (role === "ADMIN") dashboardUrl = "/admin/dashboard";
                else if (role === "FACULTY") dashboardUrl = "/faculty/dashboard";
                else if (role === "STUDENT") dashboardUrl = "/student/dashboard";

                console.log(dashboardUrl);
                // Update navbar
                if (authLinks) {
                    authLinks.innerHTML = `
                        <span class="navbar-text me-2 fw-semibold text-white">${email}</span>
                        <%--<a class="nav-link" href="${dashboardUrl}">Dashboard</a>--%>
                        <a class="nav-link" href="#" onclick="logout()">Logout</a>
                    `;
                }

                // Update main button
                mainAction.innerHTML = `
                    <a href="${dashboardUrl}" class="btn btn-success btn-lg mt-3">Go to My Dashboard</a>
                `;

            } catch (e) {
                console.error("Token decoding error", e);
                fallbackUI();
            }
        } else {
            fallbackUI();
        }

        function fallbackUI() {
            console.log("fallbackUI");
            if (authLinks) {
                authLinks.innerHTML = `
                    <a class="nav-link" href="/login">Login</a>
                    <a class="nav-link" href="/register">Register</a>
                `;
            }
            mainAction.innerHTML = `<a href="/register" class="btn btn-primary btn-lg mt-3">Get Started</a>`;
        }
    });

    function logout() {
        localStorage.removeItem("token");
        sessionStorage.setItem("loggedOut", "true");
        window.location.href = "/";
    }
</script>
</body>
</html>
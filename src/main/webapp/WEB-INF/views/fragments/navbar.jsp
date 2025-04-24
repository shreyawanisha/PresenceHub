<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ page isELIgnored="true" %>
<nav class="navbar navbar-expand-lg navbar-dark bg-primary">
    <div class="container">
        <a class="navbar-brand fw-bold" href="/">PresenceHub</a>
        <button class="navbar-toggler" type="button" data-bs-toggle="collapse"
                data-bs-target="#navbarNav" aria-controls="navbarNav"
                aria-expanded="false" aria-label="Toggle navigation">
            <span class="navbar-toggler-icon"></span>
        </button>
        <div class="collapse navbar-collapse justify-content-end" id="navbarNav">
            <div class="navbar-nav align-items-center" id="auth-links">
                <!-- JS will inject Login/Register or Logout + Email here -->
            </div>
        </div>
    </div>
</nav>

<script>
    function getEmailFromToken(token) {
        if (!token) return null;
        try {
            const payload = JSON.parse(atob(token.split('.')[1]));
            return payload.sub || payload.email || payload.username || "Unknown User";
        } catch (e) {
            return null;
        }
    }

    function logout() {
        localStorage.removeItem("token");
        sessionStorage.setItem("loggedOut", "true");
        window.location.href = "/";
    }

    document.addEventListener("DOMContentLoaded", function () {
        const token = localStorage.getItem("token");
        const authLinks = document.getElementById("auth-links");

        if (!authLinks) return;

        if (token) {
            const email = getEmailFromToken(token);
            authLinks.innerHTML = `
                <span class="nav-link disabled small text-light me-2">${email}</span>
                <a class="nav-link" href="#" onclick="logout()">Logout</a>
            `;
        } else {
            authLinks.innerHTML = `
                <a class="nav-link" href="/login">Login</a>
                <a class="nav-link" href="/register">Register</a>
            `;
        }
    });
</script>
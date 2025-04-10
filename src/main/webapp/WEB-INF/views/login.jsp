<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!DOCTYPE html>
<html>
<head>
    <title>PresenceHub - Login</title>
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/css/bootstrap.min.css" rel="stylesheet"/>
    <style>
        body {
            background: #f0f2f5;
            font-family: 'Segoe UI', sans-serif;
        }

        .login-container {
            max-width: 400px;
            margin: 80px auto;
            padding: 30px;
            background: white;
            border-radius: 12px;
            box-shadow: 0 0 25px rgba(0,0,0,0.1);
        }

        .error-message {
            color: red;
            font-size: 0.9rem;
            margin-top: 10px;
        }
    </style>
</head>
<body>

<div class="login-container">
    <h3 class="text-center text-primary">Login to PresenceHub</h3>
    <form id="loginForm">
        <div class="mb-3">
            <label for="email" class="form-label">Email address</label>
            <input type="email" id="email" class="form-control" required
                   pattern="^[a-zA-Z0-9._%+-]+@northeastern\.edu$"
                   title="Email must be a valid northeastern.edu email" />
        </div>
        <div class="mb-3">
            <label for="password" class="form-label">Password</label>
            <input type="password" class="form-control" id="password" placeholder="Password" required/>
        </div>
        <div class="d-grid">
            <button type="submit" class="btn btn-primary">Login</button>
        </div>
        <p class="text-center mt-3">
            Don't have an account? <a href="/register">Register</a>
        </p>
        <div id="error" class="error-message text-center"></div>
    </form>
</div>

<script>
    document.getElementById("email").addEventListener("input", function () {
        const emailInput = this.value;
        const errorEl = document.getElementById("error");
        if (!emailInput.endsWith("@northeastern.edu")) {
            errorEl.textContent = "Only northeastern.edu emails are allowed.";
        } else {
            errorEl.textContent = "";
        }
    });

    document.getElementById("loginForm").addEventListener("submit", async function (e) {
        e.preventDefault();

        const email = document.getElementById("email").value;
        const password = document.getElementById("password").value;
        const errorEl = document.getElementById("error");

        const response = await fetch("/api/users/login", {
            method: "POST",
            headers: {
                "Content-Type": "application/json"
            },
            body: JSON.stringify({ email, password })
        });

        if (response.ok) {
            const result = await response.json();
            const token = result.token;
            errorEl.textContent = "";

            // Store token
            localStorage.setItem("token", token);

            try {
                const payload = JSON.parse(atob(token.split('.')[1]));
                const role = payload.role;

                if (role === 'ADMIN') {
                    window.location.href = "/admin/dashboard";
                } else if (role === 'FACULTY') {
                    window.location.href = "/faculty/dashboard";
                } else {
                    window.location.href = "/student/dashboard";
                }

            } catch (e) {
                console.error("Token parse error:", e);
                errorEl.textContent = "Login succeeded but redirection failed. Please refresh.";
            }

        } else {
            const error = await response.json();
            errorEl.textContent = error.message || "Login failed. Please try again.";
        }
    });
</script>

</body>
</html>
<%--
  Created by IntelliJ IDEA.
  User: shreyawanisha
  Date: 10/04/25
  Time: 4:45 pm
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!DOCTYPE html>
<html>
<head>
  <title>Register | PresenceHub</title>
  <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/css/bootstrap.min.css" rel="stylesheet"/>
  <style>
    body {
      background: linear-gradient(to right, #e3f2fd, #ffffff);
      min-height: 100vh;
      display: flex;
      flex-direction: column;
    }

    .register-container {
      max-width: 450px;
      margin: auto;
      margin-top: 80px;
    }

    .form-label {
      font-weight: 500;
    }

    #successMessage, #errorMessage {
      display: none;
    }
  </style>
</head>
<body>

<div class="register-container shadow p-4 rounded bg-white">
  <h2 class="text-center mb-4 text-primary">Create Your Account</h2>

  <div id="successMessage" class="alert alert-success" role="alert"></div>
  <div id="errorMessage" class="alert alert-danger" role="alert"></div>

  <form id="registerForm">
    <div class="mb-3">
      <label for="username" class="form-label">Full Name</label>
      <input type="text" class="form-control" id="username" required/>
    </div>

    <div class="mb-3">
      <label for="email" class="form-label">Email (Northeastern Email)</label>
<%--      <input type="email" class="form-control" id="email" required/>--%>
      <input type="email" id="email" class="form-control" required
             pattern="^[a-zA-Z0-9._%+-]+@northeastern\.edu$"
             title="Email must be a valid northeastern.edu email" />
    </div>

    <div class="mb-3">
      <label for="password" class="form-label">Create Password</label>
      <input type="password" class="form-control" id="password" required/>
    </div>

    <div class="mb-3">
      <label for="role" class="form-label">Select Role</label>
      <select class="form-select" id="role" required>
        <option value="STUDENT">Student</option>
        <option value="FACULTY">Faculty</option>
<%--        <option value="ADMIN">Admin</option>--%>
      </select>
    </div>

    <button type="submit" class="btn btn-primary w-100">Register</button>
    <p class="mt-3 text-center">
      Already have an account? <a href="/login">Login here</a>
    </p>
  </form>
</div>

<script>
  document.getElementById("registerForm").addEventListener("submit", function (e) {
    e.preventDefault();

    const data = {
      username: document.getElementById("username").value,
      email: document.getElementById("email").value,
      password: document.getElementById("password").value,
      role: document.getElementById("role").value
    };

    fetch("/api/users/register", {
      method: "POST",
      headers: {
        "Content-Type": "application/json"
      },
      body: JSON.stringify(data)
    }).then(response => response.json())
            .then(result => {
              if (result.token) {
                document.getElementById("successMessage").textContent = result.message;
                document.getElementById("successMessage").style.display = "block";
                document.getElementById("errorMessage").style.display = "none";
                // Optional: store token in localStorage and redirect
                localStorage.setItem("token", result.token);
                setTimeout(() => {
                  window.location.href = "/";
                }, 1500);
              } else {
                throw new Error(result.message || "Something went wrong");
              }
            }).catch(error => {
      document.getElementById("errorMessage").textContent = error.message;
      document.getElementById("errorMessage").style.display = "block";
      document.getElementById("successMessage").style.display = "none";
    });
  });
</script>

<script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/js/bootstrap.bundle.min.js"></script>
</body>
</html>
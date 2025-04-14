<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!DOCTYPE html>
<html>
<head>
    <title>Register - PresenceHub</title>
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/css/bootstrap.min.css" rel="stylesheet"/>
</head>
<body class="bg-light">

<div class="container mt-5">
    <div class="row justify-content-center">
        <div class="col-md-6">
            <h2 class="text-center mb-4">👤 Register</h2>
            <form id="registerForm">
                <div class="mb-3">
                    <label for="username" class="form-label">Full Name</label>
                    <input type="text" class="form-control" id="username" required>
                </div>

                <div class="mb-3">
                    <label for="email" class="form-label">Email address</label>
                    <input type="email" class="form-control" id="email" required>
                </div>

                <div class="mb-3">
                    <label for="password" class="form-label">Password</label>
                    <input type="password" class="form-control" id="password" required>
                </div>

                <div class="mb-3">
                    <label for="role" class="form-label">Registering As</label>
                    <select class="form-select" id="role" required onchange="toggleExtraFields()">
                        <option value="">-- Select Role --</option>
                        <option value="STUDENT">Student</option>
                        <option value="FACULTY">Faculty</option>
<%--                        <option value="ADMIN">Admin</option>--%>
                    </select>
                </div>

                <!-- Student Fields -->
                <div id="studentFields" style="display: none;">
                    <div class="mb-3">
                        <label for="rollNumber" class="form-label">Roll Number</label>
                        <input type="text" class="form-control" id="rollNumber">
                    </div>
                    <div class="mb-3">
                        <label for="studentDept" class="form-label">Department</label>
                        <input type="text" class="form-control" id="studentDept">
                    </div>
                    <div class="mb-3">
                        <label for="semester" class="form-label">Semester</label>
                        <select class="form-select" id="semester">
                            <option value="">-- Select Semester --</option>
                            <c:forEach var="i" begin="1" end="8">
                                <option value="SEM${i}">SEM${i}</option>
                            </c:forEach>
                        </select>
                    </div>
                </div>

                <!-- Faculty Fields -->
                <div id="facultyFields" style="display: none;">
                    <div class="mb-3">
                        <label for="facultyDept" class="form-label">Department</label>
                        <input type="text" class="form-control" id="facultyDept">
                    </div>
                </div>

                <button type="submit" class="btn btn-primary w-100">Register</button>
            </form>
        </div>
    </div>
</div>

<script>
    function toggleExtraFields() {
        const role = document.getElementById("role").value;
        document.getElementById("studentFields").style.display = role === "STUDENT" ? "block" : "none";
        document.getElementById("facultyFields").style.display = role === "FACULTY" ? "block" : "none";
    }

    document.getElementById("registerForm").addEventListener("submit", async function (e) {
        e.preventDefault();

        const role = document.getElementById("role").value;
        const userData = {
            username: document.getElementById("username").value,
            email: document.getElementById("email").value,
            password: document.getElementById("password").value,
            role: role
        };

        if (role === "STUDENT") {
            userData.rollNumber = document.getElementById("rollNumber").value;
            userData.department = document.getElementById("studentDept").value;
            userData.semester = document.getElementById("semester").value;
        } else if (role === "FACULTY") {
            userData.department = document.getElementById("facultyDept").value;
        }

        if (!userData.username || !userData.email || !userData.password || !userData.role) {
            return alert("Please fill out all required fields!");
        }

        if (role === "STUDENT") {
            if (!userData.rollNumber || !userData.department || !userData.semester) {
                return alert("Please fill out all student fields!");
            }
        }

        if (role === "FACULTY" && !userData.department) {
            return alert("Please fill in faculty department.");
        }

        try {
            const res = await fetch("/api/users/register", {
                method: "POST",
                headers: { "Content-Type": "application/json" },
                body: JSON.stringify(userData)
            });

            if (res.ok) {
                alert("Registration successful! You can now login.");
                window.location.href = "/login";
            } else {
                const err = await res.json();
                alert("Error: " + err.message);
            }
        } catch (err) {
            alert("Something went wrong. See console.");
            console.error(err);
        }
    });
</script>

<script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/js/bootstrap.bundle.min.js"></script>
</body>
</html>
// admin-dashboard.js

let unassigned = [], unassignedFaculty = [];
let students = [], faculties = [], courses = [];

window.addEventListener("DOMContentLoaded", async () => {
    const token = localStorage.getItem("token");
    if (!token) return (window.location.href = "/login");

    try {
        const [studentsRes, facultyRes, coursesRes] = await Promise.all([
            fetch("/api/students", { headers: { Authorization: "Bearer " + token } }),
            fetch("/api/faculties", { headers: { Authorization: "Bearer " + token } }),
            fetch("/api/courses", { headers: { Authorization: "Bearer " + token } })
        ]);

        students = await studentsRes.json();
        faculties = await facultyRes.json();
        courses = await coursesRes.json();

        document.getElementById("studentCount").textContent = students.length;
        document.getElementById("facultyCount").textContent = faculties.length;
        document.getElementById("courseCount").textContent = courses.length;

        document.getElementById("viewAllStudents").addEventListener("click", () => showAllStudents(students));
        document.getElementById("viewAllFaculty").addEventListener("click", () => showAllFaculty(faculties));
        document.getElementById("viewAllCourses").addEventListener("click", () => showAllCourses(courses));

        await loadUnassignedStudents(token);
        await loadUnassignedFaculty(token);
    } catch (err) {
        console.error("Dashboard load error:", err);
    }

    document.getElementById("authorizeStudentForm").addEventListener("submit", handleStudentAuthorize);
    document.getElementById("authorizeFacultyForm").addEventListener("submit", handleFacultyAuthorize);
});

async function loadUnassignedStudents(token) {
    const res = await fetch("/api/users/unassigned-students", {
        headers: { Authorization: "Bearer " + token }
    });
    unassigned = await res.json();
    renderUnassignedStudents();
}

async function loadUnassignedFaculty(token) {
    const res = await fetch("/api/users/unassigned-faculties", {
        headers: { Authorization: "Bearer " + token }
    });
    unassignedFaculty = await res.json();
    renderUnassignedFaculty();
}

function renderUnassignedStudents() {
    const container = document.getElementById("unassignedStudentsContainer");
    container.innerHTML = "";
    unassigned.forEach(user => {
        const div = document.createElement("div");
        div.className = "col-md-4";
        div.innerHTML = `
            <div class="user-card">
                <h5>${user.username || "Unnamed"}</h5>
                <p class="text-muted">${user.email || "No Email"}</p>
                <p><strong>Roll No:</strong> ${user.rollNumber || "N/A"}</p>
                <p><strong>Department:</strong> ${user.department || "N/A"}</p>
                <p><strong>Semester:</strong> ${user.semester || "N/A"}</p>
                <button class="btn btn-sm btn-primary">Authorize</button>
            </div>`;
        div.querySelector("button").addEventListener("click", () => openAuthorizeModal(user));
        container.appendChild(div);
    });
}

function renderUnassignedFaculty() {
    const container = document.getElementById("unassignedFacultyContainer");
    container.innerHTML = "";
    unassignedFaculty.forEach(user => {
        const div = document.createElement("div");
        div.className = "col-md-4";
        div.innerHTML = `
            <div class="user-card">
                <h5>${user.username || "Unnamed"}</h5>
                <p class="text-muted">${user.email || "No Email"}</p>
                <p><strong>Department:</strong> ${user.department || "N/A"}</p>
                <button class="btn btn-sm btn-primary">Authorize</button>
            </div>`;
        div.querySelector("button").addEventListener("click", () => openFacultyAuthorizeModal(user));
        container.appendChild(div);
    });
}

function openAuthorizeModal(user) {
    document.getElementById("userIdInput").value = user.id;
    document.getElementById("rollNumber").value = user.rollNumber || '';
    document.getElementById("department").value = user.department || '';
    document.getElementById("semester").value = user.semester || '';
    new bootstrap.Modal(document.getElementById("authorizeStudentModal")).show();
}

function openFacultyAuthorizeModal(user) {
    document.getElementById("facultyUserIdInput").value = user.id;
    document.getElementById("facultyDepartment").value = user.department || '';
    new bootstrap.Modal(document.getElementById("authorizeFacultyModal")).show();
}

async function handleStudentAuthorize(e) {
    e.preventDefault();
    const userId = document.getElementById("userIdInput").value;
    const token = localStorage.getItem("token");
    const res = await fetch("/api/students", {
        method: "POST",
        headers: {
            "Content-Type": "application/json",
            Authorization: "Bearer " + token
        },
        body: JSON.stringify({ userId })
    });
    if (res.ok) {
        alert("Student authorized!");
        unassigned = unassigned.filter(u => u.id !== parseInt(userId));
        renderUnassignedStudents();
        bootstrap.Modal.getInstance(document.getElementById("authorizeStudentModal")).hide();
    } else {
        const error = await res.json();
        alert("Error: " + error.message);
    }
}

async function handleFacultyAuthorize(e) {
    e.preventDefault();
    const userId = document.getElementById("facultyUserIdInput").value;
    const token = localStorage.getItem("token");
    const res = await fetch("/api/faculties", {
        method: "POST",
        headers: {
            "Content-Type": "application/json",
            Authorization: "Bearer " + token
        },
        body: JSON.stringify({ userId })
    });
    if (res.ok) {
        alert("Faculty authorized!");
        unassignedFaculty = unassignedFaculty.filter(u => u.id !== parseInt(userId));
        renderUnassignedFaculty();
        bootstrap.Modal.getInstance(document.getElementById("authorizeFacultyModal")).hide();
    } else {
        const error = await res.json();
        alert("Error: " + error.message);
    }
}

function showAllStudents(data) {
    const tbody = document.getElementById("allStudentsTableBody");
    tbody.innerHTML = data.map(s => `
        <tr><td>${s.username}</td><td>${s.email}</td><td>${s.rollNumber}</td><td>${s.department}</td><td>${s.semester}</td></tr>
    `).join('');
    new bootstrap.Modal(document.getElementById("allStudentsModal")).show();
}

function showAllFaculty(data) {
    const tbody = document.getElementById("allFacultyTableBody");
    tbody.innerHTML = data.map(f => `
        <tr><td>${f.username}</td><td>${f.email}</td><td>${f.department}</td></tr>
    `).join('');
    new bootstrap.Modal(document.getElementById("allFacultyModal")).show();
}

function showAllCourses(data) {
    const tbody = document.getElementById("allCoursesTableBody");
    tbody.innerHTML = data.map(c => `
        <tr><td>${c.courseName}</td><td>${c.crn}</td><td>${c.department}</td><td>${c.semester}</td></tr>
    `).join('');
    new bootstrap.Modal(document.getElementById("allCoursesModal")).show();
}

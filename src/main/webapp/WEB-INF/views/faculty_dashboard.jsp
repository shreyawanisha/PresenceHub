<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ page isELIgnored="true" %>
<!DOCTYPE html>
<html>
<head>
    <title>Faculty Dashboard - PresenceHub</title>
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/css/bootstrap.min.css" rel="stylesheet"/>
    <style>
        body {
            font-family: 'Segoe UI', sans-serif;
            background: #f7f9fc;
            display: flex;
            flex-direction: column;
            min-height: 100vh;
        }
        .dashboard-title {
            font-size: 2rem;
            font-weight: bold;
            margin: 30px 0;
        }
        .accordion-button:not(.collapsed) {
            background-color: #0d6efd;
            color: white;
        }
    </style>
</head>
<body>

<jsp:include page="fragments/navbar.jsp"/>

<div class="container mt-5">
    <h2 class="dashboard-title text-center">👩‍🏫 Your Assigned Courses</h2>

    <div class="accordion" id="facultyCourseAccordion">
        <!-- Populated dynamically via JS -->
    </div>

    <div class="text-center mt-4 text-muted" id="noCoursesMsg" style="display: none;">
        You are not assigned to any courses yet.
    </div>
</div>

<jsp:include page="fragments/footer.jsp"/>

<script>
    document.addEventListener("DOMContentLoaded", async function () {
        const token = localStorage.getItem("token");
        const accordion = document.getElementById("facultyCourseAccordion");
        const noCoursesMsg = document.getElementById("noCoursesMsg");

        if (!token) {
            window.location.href = "/login";
            return;
        }

        try {
            const courseRes = await fetch("/api/courses/assigned-to-me", {
                headers: { "Authorization": "Bearer " + token }
            });

            if (!courseRes.ok) throw new Error("Could not load courses");

            const courses = await courseRes.json();
            if (!courses || courses.length === 0) {
                noCoursesMsg.style.display = "block";
                return;
            }

            for (const course of courses) {
                const index = courses.indexOf(course);
                const courseId = course.id;
                const panelId = `course${index}`;

                let students = [];
                const studentRes = await fetch(`/api/enrollments/students-by-course/${courseId}`, {
                    headers: { "Authorization": "Bearer " + token }
                });

                if (studentRes.ok) {
                    students = await studentRes.json();
                }

                let studentListHTML = "";
                if (students.length > 0) {
                    studentListHTML = "<ul class='list-unstyled'>";
                    for (var i = 0; i < students.length; i++) {
                        var s = students[i];
                        studentListHTML += "<li class='mb-2'>• <strong>" + s.username + "</strong> (" + s.email + ") <span class='text-muted'></span></li>";
                    }
                    studentListHTML += "</ul>";
                } else {
                    studentListHTML = "<p class='text-muted'>No students enrolled</p>";
                }

                accordion.innerHTML += `
                    <div class="accordion-item">
                        <h2 class="accordion-header" id="heading${panelId}">
                            <button class="accordion-button collapsed" type="button" data-bs-toggle="collapse" data-bs-target="#collapse${panelId}" aria-expanded="false" aria-controls="collapse${panelId}">
                                ${course.courseName} (${course.crn})
                            </button>
                        </h2>
                        <div id="collapse${panelId}" class="accordion-collapse collapse" aria-labelledby="heading${panelId}" data-bs-parent="#facultyCourseAccordion">
                            <div class="accordion-body">
                                <p><strong>Department:</strong> ${course.department}</p>
                                <p><strong>Semester:</strong> ${course.semester}</p>
                                <h6>Enrolled Students:</h6>
                                ${studentListHTML}
                            </div>
                        </div>
                    </div>`;
            }

        } catch (err) {
            console.error("Error loading faculty dashboard:", err);
        }
    });
</script>

</body>
</html>
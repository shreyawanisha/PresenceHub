<%--
  Created by IntelliJ IDEA.
  User: shreyawanisha
  Date: 14/04/25
  Time: 1:07 am
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>Title</title>
</head>
<body>
<!-- Student Authorization Modal -->
<div class="modal fade" id="authorizeStudentModal" tabindex="-1" aria-labelledby="authorizeStudentModalLabel" aria-hidden="true">
    <div class="modal-dialog">
        <form id="authorizeStudentForm" class="modal-content">
            <div class="modal-header">
                <h5 class="modal-title">Authorize Student</h5>
                <button type="button" class="btn-close" data-bs-dismiss="modal" aria-label="Close"></button>
            </div>
            <div class="modal-body">
                <input type="hidden" id="userIdInput">
                <div class="mb-3">
                    <label class="form-label">Roll Number</label>
                    <input type="text" class="form-control" id="rollNumber" readonly>
                </div>
                <div class="mb-3">
                    <label class="form-label">Department</label>
                    <input type="text" class="form-control" id="department" readonly>
                </div>
                <div class="mb-3">
                    <label class="form-label">Semester</label>
                    <input type="text" class="form-control" id="semester" readonly>
                </div>
            </div>
            <div class="modal-footer">
                <button type="submit" class="btn btn-success">Authorize</button>
            </div>
        </form>
    </div>
</div>

<!-- Faculty Authorization Modal -->
<div class="modal fade" id="authorizeFacultyModal" tabindex="-1" aria-labelledby="authorizeFacultyModalLabel" aria-hidden="true">
    <div class="modal-dialog">
        <form id="authorizeFacultyForm" class="modal-content">
            <div class="modal-header">
                <h5 class="modal-title">Authorize Faculty</h5>
                <button type="button" class="btn-close" data-bs-dismiss="modal" aria-label="Close"></button>
            </div>
            <div class="modal-body">
                <input type="hidden" id="facultyUserIdInput">
                <div class="mb-3">
                    <label class="form-label">Department</label>
                    <input type="text" class="form-control" id="facultyDepartment" readonly>
                </div>
            </div>
            <div class="modal-footer">
                <button type="submit" class="btn btn-success">Authorize</button>
            </div>
        </form>
    </div>
</div>

</body>
</html>

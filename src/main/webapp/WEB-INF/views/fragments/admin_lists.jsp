<!-- ✅ This is correct content for admin_lists.jsp -->
<!-- Modal: View All Students -->
<div class="modal fade" id="allStudentsModal" tabindex="-1">
    <div class="modal-dialog modal-lg">
        <div class="modal-content">
            <div class="modal-header">
                <h5 class="modal-title">All Students</h5>
                <button type="button" class="btn-close" data-bs-dismiss="modal"></button>
            </div>
            <div class="modal-body">
                <table class="table table-striped">
                    <thead>
                    <tr><th>Name</th><th>Email</th><th>Roll No</th><th>Department</th><th>Semester</th></tr>
                    </thead>
                    <tbody id="allStudentsTableBody"></tbody>
                </table>
            </div>
        </div>
    </div>
</div>

<!-- Modal: View All Faculty -->
<div class="modal fade" id="allFacultyModal" tabindex="-1">
    <div class="modal-dialog modal-lg">
        <div class="modal-content">
            <div class="modal-header">
                <h5 class="modal-title">All Faculty</h5>
                <button type="button" class="btn-close" data-bs-dismiss="modal"></button>
            </div>
            <div class="modal-body">
                <table class="table table-striped">
                    <thead><tr><th>Name</th><th>Email</th><th>Department</th></tr></thead>
                    <tbody id="allFacultyTableBody"></tbody>
                </table>
            </div>
        </div>
    </div>
</div>

<!-- Modal: View All Courses -->
<div class="modal fade" id="allCoursesModal" tabindex="-1">
    <div class="modal-dialog modal-lg">
        <div class="modal-content">
            <div class="modal-header">
                <h5 class="modal-title">All Courses</h5>
                <button type="button" class="btn-close" data-bs-dismiss="modal"></button>
            </div>
            <div class="modal-body">
                <table class="table table-striped">
                    <thead><tr><th>Course Name</th><th>CRN</th><th>Department</th><th>Semester</th></tr></thead>
                    <tbody id="allCoursesTableBody"></tbody>
                </table>
            </div>
        </div>
    </div>
</div>
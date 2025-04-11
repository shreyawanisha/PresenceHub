<%--
  Created by IntelliJ IDEA.
  User: shreyawanisha
  Date: 10/04/25
  Time: 10:23 pm
  To change this template use File | Settings | File Templates.
--%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/js/bootstrap.bundle.min.js"></script>

<!-- Footer -->
<footer class="bg-light text-center text-muted mt-auto py-3 border-top">
    <div class="container">
        <p class="mb-0">&copy; 2025 PresenceHub. Built by <a href="https://www.linkedin.com/in/shreya-wanisha/">Shreya Wanisha</a></p>
    </div>
</footer>

<!-- Toast container -->
<div class="position-fixed bottom-0 end-0 p-3" style="z-index: 1055">
    <div id="logoutToast" class="toast align-items-center text-white bg-success border-0" role="alert" aria-live="assertive" aria-atomic="true">
        <div class="d-flex">
            <div class="toast-body" id="toastMessage">
                <!-- Message will go here -->
            </div>
            <button type="button" class="btn-close btn-close-white me-2 m-auto" data-bs-dismiss="toast" aria-label="Close"></button>
        </div>
    </div>
</div>

<script>
    // // Show toast with dynamic message
    // function showToast(message) {
    //     const toastMessage = document.getElementById("toastMessage");
    //     toastMessage.textContent = message;
    //
    //     const toastEl = document.getElementById("logoutToast");
    //     const toast = new bootstrap.Toast(toastEl);
    //     toast.show();
    // }

    // Show logout message if flag is set
    document.addEventListener("DOMContentLoaded", function () {
        if (sessionStorage.getItem("loggedOut")) {
            // showToast("✅ You’ve been logged out successfully.");
            sessionStorage.removeItem("loggedOut");
        }
    });
</script>
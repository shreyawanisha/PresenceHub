<%--
  Created by IntelliJ IDEA.
  User: shreyawanisha
  Date: 10/04/25
  Time: 10:23 pm
  To change this template use File | Settings | File Templates.
--%>
<footer class="bg-light text-center text-muted py-3 mt-5">
    <p class="mb-0">&copy; 2025 PresenceHub. Built by <a href="https://www.linkedin.com/in/shreya-wanisha/">Shreya Wanisha</a></p>
</footer>

<!-- Bootstrap JS -->
<script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/js/bootstrap.bundle.min.js"></script>

<!-- Optional toast logic -->
<script>

    function showToast(message) {
        const toast = document.createElement("div");
        toast.className = "toast align-items-center text-white bg-success border-0 position-fixed bottom-0 end-0 m-4";
        toast.setAttribute("role", "alert");
        toast.setAttribute("aria-live", "assertive");
        toast.setAttribute("aria-atomic", "true");
        toast.innerHTML = `
            <div class="d-flex">
                <div class="toast-body">
                    ${message}
                </div>
                <button type="button" class="btn-close btn-close-white me-2 m-auto"
                        data-bs-dismiss="toast" aria-label="Close"></button>
            </div>
        `;
        document.body.appendChild(toast);
        new bootstrap.Toast(toast).show();

        toast.addEventListener("hidden.bs.toast", () => {
            document.body.removeChild(toast);
        });
    }
</script>

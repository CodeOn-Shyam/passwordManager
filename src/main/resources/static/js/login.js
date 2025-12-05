// js/login.js

const loginStatus = document.getElementById("loginStatus");

document.getElementById("loginBtn").addEventListener("click", async () => {
    const username = document.getElementById("loginUsername").value.trim();
    const password = document.getElementById("loginPassword").value;

    if (!username || !password) {
        setStatus(loginStatus, "Please enter username and password", false);
        return;
    }

    // Save and test credentials
    saveAuth(username, password);

    try {
        const res = await fetch("/api/credentials", {
            headers: getAuthHeaders()
        });

        if (res.status === 401 || res.status === 403) {
            clearAuth();
            setStatus(loginStatus, "Invalid credentials", false);
            return;
        }

        setStatus(loginStatus, "Login successful. Redirecting...", true);
        setTimeout(() => {
            window.location.href = "/dashboard.html";
        }, 1000);

    } catch (e) {
        clearAuth();
        setStatus(loginStatus, "Login check failed: " + e.message, false);
    }
});

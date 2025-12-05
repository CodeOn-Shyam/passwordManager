// js/security.js

// Ensure user is logged in
const auth = requireAuthOrRedirect();
const userBadge = document.getElementById("userBadge");
userBadge.textContent = "Logged in as: " + auth.username;

document.getElementById("logoutLink").addEventListener("click", (e) => {
    e.preventDefault();
    logoutAndRedirect();
});

// ---- Vault key ----
const vaultKeyStatus = document.getElementById("vaultKeyStatus");

document.getElementById("vaultKeyBtn").addEventListener("click", async () => {
    const vaultKey = document.getElementById("vaultKey").value;
    const curPass = document.getElementById("vaultCurPass").value;

    if (!vaultKey || !curPass) {
        setStatus(vaultKeyStatus, "Vault key and current password are required", false);
        return;
    }

    try {
        const res = await fetch("/auth/vault-key", {
            method: "POST",
            headers: getAuthHeaders(),
            body: JSON.stringify({
                vaultKey: vaultKey,
                currentPassword: curPass
            })
        });
        const text = await res.text();
        if (!res.ok) {
            setStatus(vaultKeyStatus, "Error: " + res.status + " " + text, false);
            return;
        }
        setStatus(vaultKeyStatus, text, true);
    } catch (e) {
        setStatus(vaultKeyStatus, "Request failed: " + e.message, false);
    }
});

// ---- Recovery info ----
const recInfoStatus = document.getElementById("recInfoStatus");

document.getElementById("recInfoBtn").addEventListener("click", async () => {
    const q = document.getElementById("recQuestion").value.trim();
    const a = document.getElementById("recAnswer").value.trim();
    const cur = document.getElementById("recCurPass").value;

    if (!q || !a || !cur) {
        setStatus(recInfoStatus, "Question, answer, and current password are required", false);
        return;
    }

    try {
        const res = await fetch("/auth/recovery-info", {
            method: "POST",
            headers: getAuthHeaders(),
            body: JSON.stringify({
                question: q,
                answer: a,
                currentPassword: cur
            })
        });
        const text = await res.text();
        if (!res.ok) {
            setStatus(recInfoStatus, "Error: " + res.status + " " + text, false);
            return;
        }
        setStatus(recInfoStatus, text, true);
    } catch (e) {
        setStatus(recInfoStatus, "Request failed: " + e.message, false);
    }
});

// ---- Change password ----
const cpStatus = document.getElementById("cpStatus");

document.getElementById("cpBtn").addEventListener("click", async () => {
    const cur = document.getElementById("cpCurPass").value;
    const neu = document.getElementById("cpNewPass").value;

    if (!cur || !neu) {
        setStatus(cpStatus, "Current and new password are required", false);
        return;
    }

    try {
        const res = await fetch("/auth/change-password", {
            method: "POST",
            headers: getAuthHeaders(),
            body: JSON.stringify({
                currentPassword: cur,
                newPassword: neu
            })
        });
        const text = await res.text();
        if (!res.ok) {
            setStatus(cpStatus, "Error: " + res.status + " " + text, false);
            return;
        }
        setStatus(cpStatus, text + " Please log in again with the new password.", true);

        // Optionally clear auth after a short delay
        setTimeout(() => {
            logoutAndRedirect();
        }, 1500);
    } catch (e) {
        setStatus(cpStatus, "Request failed: " + e.message, false);
    }
});

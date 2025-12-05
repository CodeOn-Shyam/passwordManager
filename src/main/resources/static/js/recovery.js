// js/recovery.js

const fpStatus = document.getElementById("fpStatus");
const fvStatus = document.getElementById("fvStatus");

// Forgot password
document.getElementById("fpBtn").addEventListener("click", async () => {
    const username = document.getElementById("fpUsername").value.trim();
    const answer = document.getElementById("fpAnswer").value.trim();
    const newPass = document.getElementById("fpNewPass").value;

    if (!username || !answer || !newPass) {
        setStatus(fpStatus, "All fields are required", false);
        return;
    }

    try {
        const res = await fetch("/auth/forgot-password", {
            method: "POST",
            headers: { "Content-Type": "application/json" },
            body: JSON.stringify({
                username: username,
                recoveryAnswer: answer,
                newPassword: newPass
            })
        });
        const text = await res.text();
        if (!res.ok) {
            setStatus(fpStatus, "Error: " + res.status + " " + text, false);
            return;
        }
        setStatus(fpStatus, text + " You can now log in with the new password.", true);
    } catch (e) {
        setStatus(fpStatus, "Request failed: " + e.message, false);
    }
});

// Forgot vault key
document.getElementById("fvBtn").addEventListener("click", async () => {
    const username = document.getElementById("fvUsername").value.trim();
    const answer = document.getElementById("fvAnswer").value.trim();
    const newKey = document.getElementById("fvNewKey").value;

    if (!username || !answer || !newKey) {
        setStatus(fvStatus, "All fields are required", false);
        return;
    }

    try {
        const res = await fetch("/auth/forgot-vault-key", {
            method: "POST",
            headers: { "Content-Type": "application/json" },
            body: JSON.stringify({
                username: username,
                recoveryAnswer: answer,
                newVaultKey: newKey
            })
        });
        const text = await res.text();
        if (!res.ok) {
            setStatus(fvStatus, "Error: " + res.status + " " + text, false);
            return;
        }
        setStatus(fvStatus, text + " Use this new vault key when revealing passwords.", true);
    } catch (e) {
        setStatus(fvStatus, "Request failed: " + e.message, false);
    }
});

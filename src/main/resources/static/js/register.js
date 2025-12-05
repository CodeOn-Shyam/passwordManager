// js/register.js
const regStatus = document.getElementById("regStatus");

function setStatus(el, msg, ok) {
    el.textContent = msg || "";
    el.classList.remove("ok", "err");
    if (!msg) return;
    el.classList.add(ok ? "ok" : "err");
}

document.getElementById("regBtn").addEventListener("click", async () => {
    const username = document.getElementById("regUsername").value.trim();
    const pass1 = document.getElementById("regPassword").value;
    const pass2 = document.getElementById("regPassword2").value;

    if (!username || !pass1 || !pass2) {
        setStatus(regStatus, "All fields are required", false);
        return;
    }
    if (pass1 !== pass2) {
        setStatus(regStatus, "Passwords do not match", false);
        return;
    }

    try {
        const res = await fetch("/auth/register", {
            method: "POST",
            headers: {"Content-Type": "application/json"},
            body: JSON.stringify({ username, password: pass1 })
        });
        const text = await res.text();
        if (!res.ok) {
            setStatus(regStatus, "Error: " + res.status + " " + text, false);
            return;
        }
        setStatus(regStatus, text + " Redirecting to login...", true);
        setTimeout(() => {
            window.location.href = "/login.html";
        }, 1200);
    } catch (e) {
        setStatus(regStatus, "Request failed: " + e.message, false);
    }
});

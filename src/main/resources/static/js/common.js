// js/common.js

const AUTH_KEY = "pawword_auth"; // stored as { username, password }

function saveAuth(username, password) {
    localStorage.setItem(AUTH_KEY, JSON.stringify({ username, password }));
}

function clearAuth() {
    localStorage.removeItem(AUTH_KEY);
}

function getAuth() {
    const raw = localStorage.getItem(AUTH_KEY);
    if (!raw) return null;
    try {
        return JSON.parse(raw);
    } catch {
        return null;
    }
}

function getAuthHeaders() {
    const auth = getAuth();
    if (!auth || !auth.username || !auth.password) {
        throw new Error("Not logged in");
    }
    const token = btoa(auth.username + ":" + auth.password);
    return {
        "Authorization": "Basic " + token,
        "Content-Type": "application/json"
    };
}

function requireAuthOrRedirect() {
    const auth = getAuth();
    if (!auth) {
        window.location.href = "/login.html";
        return null;
    }
    return auth;
}

function setStatus(el, msg, ok) {
    el.textContent = msg || "";
    el.classList.remove("ok", "err");
    if (!msg) return;
    el.classList.add(ok ? "ok" : "err");
}

function logoutAndRedirect() {
    clearAuth();
    window.location.href = "/login.html";
}

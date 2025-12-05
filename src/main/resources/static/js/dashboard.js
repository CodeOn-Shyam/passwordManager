// js/dashboard.js

const auth = requireAuthOrRedirect();
const userBadge = document.getElementById("userBadge");
userBadge.textContent = "Logged in as: " + auth.username;

document.getElementById("logoutLink").addEventListener("click", (e) => {
    e.preventDefault();
    logoutAndRedirect();
});

const addCredStatus = document.getElementById("addCredStatus");
const listStatus = document.getElementById("listStatus");
const decryptCard = document.getElementById("decryptCard");
const decryptMeta = document.getElementById("decryptMeta");
const decryptPasswordDiv = document.getElementById("decryptPassword");

async function loadCredentials() {
    decryptCard.style.display = "none";
    try {
        const res = await fetch("/api/credentials", {
            headers: getAuthHeaders()
        });
        if (!res.ok) {
            const text = await res.text();
            setStatus(listStatus, "Error: " + res.status + " " + text, false);
            return;
        }
        const arr = await res.json();
        renderTable(arr);
        setStatus(listStatus, "Loaded " + arr.length + " credential(s).", true);
    } catch (e) {
        setStatus(listStatus, "Request failed: " + e.message, false);
    }
}

function renderTable(credentials) {
    const tbody = document.getElementById("credTbody");
    tbody.innerHTML = "";

    if (!credentials || credentials.length === 0) {
        const tr = document.createElement("tr");
        const td = document.createElement("td");
        td.colSpan = 6;
        td.textContent = "No credentials saved yet.";
        td.style.color = "#6b7280";
        tr.appendChild(td);
        tbody.appendChild(tr);
        return;
    }

    credentials.forEach(c => {
        const tr = document.createElement("tr");

        const tdId = document.createElement("td");
        tdId.textContent = c.id;

        const tdService = document.createElement("td");
        tdService.textContent = c.servicename;

        const tdUser = document.createElement("td");
        tdUser.textContent = c.username;

        const tdNote = document.createElement("td");
        tdNote.textContent = c.note || "";

        const tdCreated = document.createElement("td");
        tdCreated.textContent = c.createdAt ? c.createdAt.replace("T", " ").slice(0, 16) : "";

        const tdActions = document.createElement("td");

        const revealBtn = document.createElement("button");
        revealBtn.textContent = "Reveal";
        revealBtn.classList.add("secondary");
        revealBtn.addEventListener("click", () => promptAndReveal(c.id, c.servicename, c.username));

        const delBtn = document.createElement("button");
        delBtn.textContent = "Delete";
        delBtn.classList.add("danger");
        delBtn.style.marginLeft = "0.4rem";
        delBtn.addEventListener("click", () => {
            if (confirm(`Delete credential ${c.id} (${c.servicename})?`)) {
                deleteCredential(c.id);
            }
        });

        tdActions.appendChild(revealBtn);
        tdActions.appendChild(delBtn);

        tr.appendChild(tdId);
        tr.appendChild(tdService);
        tr.appendChild(tdUser);
        tr.appendChild(tdNote);
        tr.appendChild(tdCreated);
        tr.appendChild(tdActions);

        tbody.appendChild(tr);
    });
}

async function promptAndReveal(id, service, user) {
    const vaultKey = prompt("Enter your vault key to reveal this password:");
    if (!vaultKey) return;

    try {
        const res = await fetch(`/api/credentials/${id}/reveal`, {
            method: "POST",
            headers: getAuthHeaders(),
            body: JSON.stringify({ vaultKey })
        });
        if (!res.ok) {
            const text = await res.text();
            setStatus(listStatus, "Reveal failed: " + res.status + " " + text, false);
            return;
        }
        const plain = await res.text();
        decryptMeta.textContent = `ID ${id} – ${service} (${user})`;
        decryptPasswordDiv.textContent = plain;
        decryptCard.style.display = "block";
    } catch (e) {
        setStatus(listStatus, "Reveal failed: " + e.message, false);
    }
}

async function deleteCredential(id) {
    try {
        const res = await fetch(`/api/credentials/${id}`, {
            method: "DELETE",
            headers: getAuthHeaders()
        });
        if (!res.ok && res.status !== 204) {
            const text = await res.text();
            setStatus(listStatus, "Delete failed: " + res.status + " " + text, false);
            return;
        }
        setStatus(listStatus, "Deleted credential " + id, true);
        await loadCredentials();
    } catch (e) {
        setStatus(listStatus, "Delete failed: " + e.message, false);
    }
}

document.getElementById("reloadBtn").addEventListener("click", loadCredentials);

document.getElementById("addCredBtn").addEventListener("click", async () => {
    const service = document.getElementById("credService").value.trim();
    const user = document.getElementById("credUser").value.trim();
    const pass = document.getElementById("credPass").value;
    const note = document.getElementById("credNote").value;

    if (!service || !user || !pass) {
        setStatus(addCredStatus, "Service, username and password are required", false);
        return;
    }

    try {
        const res = await fetch("/api/credentials", {
            method: "POST",
            headers: getAuthHeaders(),
            body: JSON.stringify({
                servicename: service,
                username: user,
                password: pass,
                note: note
            })
        });
        if (!res.ok) {
            const text = await res.text();
            setStatus(addCredStatus, "Error: " + res.status + " " + text, false);
            return;
        }
        const json = await res.json();
        setStatus(addCredStatus, "Saved credential with ID " + json.id, true);
        document.getElementById("credPass").value = "";
        await loadCredentials();
    } catch (e) {
        setStatus(addCredStatus, "Request failed: " + e.message, false);
    }
});

// initial load
loadCredentials();

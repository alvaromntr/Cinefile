// js/profile.js – perfil fake (decodifica Basic) + últimos avaliados do localStorage
document.addEventListener("DOMContentLoaded", async () => {
  if (!API.Auth.has()) { location.href = "login.html?redirect=" + encodeURIComponent("profile.html"); return; }

  const nameEl   = document.getElementById("profileName");
  const avatarEl = document.getElementById("profileAvatar");
  const lastBox  = document.getElementById("profileLast");

  // username a partir do Basic
  let username = "usuario";
  try {
    const basic = API.Auth.get();
    const decoded = atob(String(basic).replace(/^Basic\s+/i, ""));
    username = decoded.split(":")[0] || "usuario";
  } catch {}
  if (nameEl) nameEl.textContent = "@" + username;

  if (avatarEl) {
    // tenta arquivo, senão fallback texto/sem img
    const saved = localStorage.getItem("cinefile_avatar");
    avatarEl.src = saved || "assets/avatar.png";
    avatarEl.alt = "Avatar do usuário";
    avatarEl.onerror = () => { avatarEl.removeAttribute("src"); avatarEl.alt = "Avatar do usuário"; };
  }

  if (!lastBox) return;

  // últimos 4
  const recent = (window.Ratings?.listSortedDesc?.() || []).slice(0, 4);
  if (!recent.length) { lastBox.innerHTML = `<p class="muted">Sem avaliações recentes.</p>`; return; }

  lastBox.innerHTML = "";
  for (const r of recent) {
    try {
      const ob = await API.getObra(r.id.replace(/\D/g, "") || r.id);
      const a = document.createElement("a");
      a.href = `details.html?id=${encodeURIComponent(ob?.obraid ?? r.id)}`;
      a.className = "miniCard";
      a.innerHTML = `
        <img class="miniCard__img" src="${ob?.poster_url || ""}" alt="${ob?.titulo || "Título"}"/>
        <div class="miniCard__body">
          <div class="miniCard__title">${ob?.titulo || "Título"}</div>
          <div class="miniCard__meta">${(ob?.anolancamento || "")} • ★${r.rating}</div>
        </div>
      `;
      lastBox.appendChild(a);
    } catch {
      // ignora erro individual
    }
  }
});

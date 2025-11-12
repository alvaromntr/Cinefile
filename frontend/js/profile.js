// js/profile.js
// Perfil do usuário: exige login, mostra header e últimos avaliados (até 4).
// Cruza com catálogo local e, se faltar, busca no TMDB para reconstruir o card.

document.addEventListener("DOMContentLoaded", async () => {
  // Garante catálogo do backend quando em modo http
  async function ensureCatalogLoaded(){
    try {
      if (Array.isArray(window.DATA) && window.DATA.length) return window.DATA;
      const base = (localStorage.getItem("cinefile_api_base") || "http://localhost:8080").replace(/\/$/, "");
      if (base) {
        const res = await fetch(`${base}/api/obras`, { credentials: "include" });
        if (res.ok) {
          const arr = await res.json();
          const toItem = (o) => ({
            id: String((o?.obraid ?? o?.id) ?? ""),
            type: String(o?.tipo || "").toUpperCase() === "SERIE" ? "series" : "movie",
            title: o?.titulo || o?.nome || "Sem título",
            year: o?.anolancamento ?? o?.anoLancamento ?? o?.ano ?? "",
            tmdbId: o?.tmdbId ?? null,
            poster: o?.poster_url || "",
            rating: o?.mediaAvaliacao ?? o?.rating ?? 0,
          });
          const data = Array.isArray(arr) ? arr.map(toItem) : [];
          if (data.length) { window.DATA = data; return data; }
        }
      }
    } catch {}
    return window.DATA || [];
  }
  // ===== 1) Exigir login =====
  const isLogged =
    (window.AUTH && typeof AUTH.isLogged === "function" && AUTH.isLogged()) ||
    localStorage.getItem("cinefile_logged_in") === "true" ||
    localStorage.getItem("cinefile_logged") === "true";

  if (!isLogged) {
    window.location.href = "login.html";
    return;
  }

  // ===== 2) Header do perfil =====
  const rawUser  = localStorage.getItem("cinefile_username") || "usuario";
  const username = rawUser.startsWith("@") ? rawUser : `@${rawUser}`;
  const nameEl   = document.getElementById("profileName");
  if (nameEl) nameEl.textContent = username;

  const avatarEl = document.getElementById("profileAvatar");
  if (avatarEl) {
    const saved = localStorage.getItem("cinefile_avatar");
    avatarEl.src = saved || "assets/avatar.png";
    avatarEl.onerror = () => { avatarEl.src = "assets/picfoto.png"; };
  }

  // ===== 3) Últimos avaliados =====
  await ensureCatalogLoaded();
  const CATALOG  = (window.DATA || []).slice();
  const indexById = new Map(CATALOG.map(obj => [String(obj.id), obj]));

  const scroller = document.getElementById("pfScroller");
  const metaEl   = document.getElementById("pfLastMeta");
  if (!scroller) return;

  // Lê avaliações salvas
  let RATINGS = {};
  try { RATINGS = JSON.parse(localStorage.getItem("cinefile_ratings") || "{}"); }
  catch { RATINGS = {}; }

  // Ordena por mais recente, filtra > 0 e pega até 4
  const ordered = Object.entries(RATINGS)
    .map(([id, r]) => ({
      id: String(id),
      rating: Number(r?.rating || 0),
      ts: Number(r?.ts || 0)
    }))
    .filter(x => x.rating > 0)
    .sort((a, b) => b.ts - a.ts)
    .slice(0, 4);

  // Se nada avaliado
  if (!ordered.length) {
    scroller.innerHTML = "";
    if (metaEl) metaEl.textContent = "Nenhum título avaliado ainda";
    return;
  }

  // Helper de estrelas
  const starString = (n) => {
    const s = Math.max(0, Math.min(5, Number(n || 0)));
    return "★".repeat(s) + "☆".repeat(5 - s);
  };

  // Imagem TMDB
  const tmdbImage = (path) => path ? `https://image.tmdb.org/t/p/w342${path}` : "";

  // Mapeia detalhes TMDB para o nosso modelo
  function mapTMDBDetails(d, id) {
    if (!d) return null;
    const isTv = !!d.first_air_date || d.media_type === "tv" || d.type === "series";
    return {
      id: String(id),
      title: d.title || d.name || "(sem título)",
      year: (d.release_date || d.first_air_date || "").slice(0, 4) || "",
      poster: tmdbImage(d.poster_path),
      type: isTv ? "series" : "movie"
    };
  }

  // Busca por ID no TMDB usando tmdb.js se existir, senão fetch direto
  async function fetchFromTMDBById(id) {
    // 1) tmdb.js
    try {
      if (window.TMDB) {
        if (typeof TMDB.getDetails === "function") {
          const d = await TMDB.getDetails(id);
          return mapTMDBDetails(d, id);
        }
        if (typeof TMDB.details === "function") {
          const d = await TMDB.details(id);
          return mapTMDBDetails(d, id);
        }
      }
    } catch {}

    // 2) fetch direto
    try {
      const apiKey = window.TMDB?.API_KEY || window.TMDB_KEY || undefined;
      const lang   = "pt-BR";
      if (!apiKey) return null;

      // movie
      let res = await fetch(`https://api.themoviedb.org/3/movie/${id}?api_key=${apiKey}&language=${lang}`);
      if (res.ok) {
        const d = await res.json();
        return {
          id: String(id),
          title: d.title || d.name || "(sem título)",
          year: (d.release_date || "").slice(0, 4) || "",
          poster: tmdbImage(d.poster_path),
          type: "movie"
        };
      }
      // tv
      res = await fetch(`https://api.themoviedb.org/3/tv/${id}?api_key=${apiKey}&language=${lang}`);
      if (res.ok) {
        const d = await res.json();
        return {
          id: String(id),
          title: d.name || d.title || "(sem título)",
          year: (d.first_air_date || "").slice(0, 4) || "",
          poster: tmdbImage(d.poster_path),
          type: "series"
        };
      }
    } catch {}
    return null;
  }

  (async () => {
    // Encontra no catálogo local ou busca no TMDB se faltar
    const items = [];
    for (const e of ordered) {
      const local = indexById.get(String(e.id));
      if (local) {
        items.push({ ...local, rating: e.rating, ts: e.ts });
      } else {
        const fetched = await fetchFromTMDBById(e.id);
        if (fetched) items.push({ ...fetched, rating: e.rating, ts: e.ts });
      }
    }

    scroller.innerHTML = "";

    if (!items.length) {
      if (metaEl) metaEl.textContent = "Nenhum título avaliado ainda";
      return;
    }

    if (metaEl) metaEl.textContent = `${items.length} ${items.length === 1 ? "título" : "títulos"}`;

    // Render idêntico ao de “Assistidos”
    items.forEach(item => {
      let link;

      if (typeof window.createCarouselItem === "function") {
        link = window.createCarouselItem(item);
        link.querySelector(".item__overlay")?.remove();
        link.querySelector(".item__body")?.remove();
      } else {
        // Fallback simples
        link = document.createElement("a");
        link.href = `details.html?id=${encodeURIComponent(String(item.id))}`;
        link.className = "item__link";
        link.innerHTML = `
          <article class="item">
            <img class="item__img" src="${item.poster || ""}" alt="Pôster de ${item.title}">
          </article>
        `;
      }

      const wrap = document.createElement("div");
      wrap.style.display = "grid";
      wrap.style.justifyItems = "center";
      wrap.appendChild(link);

      const stars = document.createElement("div");
      stars.className = "watched__stars";
      stars.textContent = starString(item.rating || 0);
      wrap.appendChild(stars);

      scroller.appendChild(wrap);
    });

    // Garante visibilidade
    scroller.removeAttribute("hidden");
  })();
});


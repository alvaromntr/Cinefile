// js/watched.js
// Lê 'cinefile_ratings' do localStorage, cruza com o catálogo (DATA/MOCK_DATA)
// e renderiza "Assistidos". Se um id não existir no catálogo local, busca no TMDB.

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
  const row      = document.getElementById("wathedRow") || document.getElementById("watchedRow");
  const scroller = document.getElementById("watchedScroller");
  const meta     = document.getElementById("watchedMeta");
  const dots     = document.getElementById("watchedDots");
  const btnPrev  = document.getElementById("btnPrev");
  const btnNext  = document.getElementById("btnNext");

  if (!row || !scroller || !meta || !dots) {
    console.warn("[Cinefile] Estrutura de Assistidos incompleta em watched.html.");
    return;
  }

  const isLogged = (() => {
    try {
      if (window.AUTH?.isLogged) return !!window.AUTH.isLogged();
    } catch {}
    try {
      return (
        localStorage.getItem("cinefile_logged") === "true" ||
        localStorage.getItem("cinefile_logged_in") === "true" ||
        !!localStorage.getItem("cinefile_username")
      );
    } catch {}
    return false;
  })();

  if (!isLogged) {
    const back = encodeURIComponent("watched.html");
    location.href = `login.html?redirect=${back}`;
    return;
  }

  // Catálogo local
  await ensureCatalogLoaded();
  const CATALOG = (window.DATA || []).slice();
  const indexById = new Map(CATALOG.map(obj => [String(obj.id), obj]));

  // Ratings do localStorage
  let ratingsDict = {};
  try {
    const raw = localStorage.getItem("cinefile_ratings");
    ratingsDict = raw ? JSON.parse(raw) : {};
  } catch { ratingsDict = {}; }

  // Lista [{id, rating, ts}]
  const entries = Object.keys(ratingsDict).map(id => {
    const v = ratingsDict[id] || {};
    return { id: String(id), rating: Number(v.rating || 0), ts: Number(v.ts || 0) };
  }).filter(e => e.rating > 0);

  if (!entries.length) {
    meta.textContent = "0 títulos";
    const p = document.createElement("p");
    p.className = "muted";
    p.style.padding = "8px 20px";
    p.textContent = "Ainda não há títulos avaliados.";
    row.prepend(p);
    return;
  }

  // Ordena por recente
  entries.sort((a, b) => (b.ts || 0) - (a.ts || 0));

  // Seletor de imagem do TMDB
  const tmdbImage = (path) => path ? `https://image.tmdb.org/t/p/w342${path}` : "";

  // Fallback TMDB: tenta via tmdb.js se existir, senão fetch direto
  async function fetchFromTMDBById(id) {
    // 1) tmdb.js conhecido?
    try {
      if (window.TMDB) {
        // tente nomes comuns
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

    // 2) fetch direto: tenta como movie, depois como tv
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
          year: (d.release_date || "").slice(0,4) || "",
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
          year: (d.first_air_date || "").slice(0,4) || "",
          poster: tmdbImage(d.poster_path),
          type: "series"
        };
      }
    } catch {}
    return null;
  }

  function mapTMDBDetails(d, id) {
    if (!d) return null;
    const isTv = !!d.first_air_date || d.media_type === "tv" || d.type === "series";
    return {
      id: String(id),
      title: d.title || d.name || "(sem título)",
      year: (d.release_date || d.first_air_date || "").slice(0,4) || "",
      poster: tmdbImage(d.poster_path),
      type: isTv ? "series" : "movie"
    };
  }

  (async () => {
    // Separa encontrados vs faltantes no catálogo local
    const found   = [];
    const missing = [];

    for (const e of entries) {
      const local = indexById.get(String(e.id));
      if (local) {
        found.push({ ...local, rating: e.rating, ts: e.ts });
      } else {
        missing.push(e);
      }
    }

    // Busca os faltantes no TMDB
    const fetched = [];
    for (const m of missing) {
      const d = await fetchFromTMDBById(m.id);
      if (d) fetched.push({ ...d, rating: m.rating, ts: m.ts });
    }

    const items = [...found, ...fetched];

    if (!items.length) {
      meta.textContent = "0 títulos";
      const p = document.createElement("p");
      p.className = "muted";
      p.style.padding = "8px 20px";
      p.textContent = "Suas avaliações existem, mas não batem com o catálogo atual.";
      row.prepend(p);
      return;
    }

    meta.textContent = `${items.length} ${items.length === 1 ? "título" : "títulos"}`;

    // helper: estrelas
    const starStr = (n) => "★".repeat(n) + "☆".repeat(5 - n);

    // Render
    scroller.innerHTML = "";
    dots.innerHTML = "";

    items.forEach(item => {
      let link;
      if (typeof window.createCarouselItem === "function") {
        link = window.createCarouselItem(item);
        link.querySelector(".item__overlay")?.remove();
        link.querySelector(".item__body")?.remove();
      } else {
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
      stars.textContent = starStr(item.rating);
      wrap.appendChild(stars);

      scroller.appendChild(wrap);
    });

    // Dots de paginação
    const pageSize = 6;
    const totalPages = Math.ceil(items.length / pageSize);
    for (let i = 0; i < totalPages; i++) {
      const dot = document.createElement("button");
      dot.type = "button";
      dot.className = "row__dot";
      dot.setAttribute("aria-label", `Página ${i + 1}`);
      dot.addEventListener("click", () => {
        const targetX = i * (scroller.clientWidth);
        scroller.scrollTo({ left: targetX, behavior: "smooth" });
      });
      dots.appendChild(dot);
    }

    // Controles Prev/Next
    const scrollByPage = (dir) => {
      const delta = dir * scroller.clientWidth;
      scroller.scrollBy({ left: delta, behavior: "smooth" });
    };
    btnPrev?.addEventListener("click", () => scrollByPage(-1));
    btnNext?.addEventListener("click", () => scrollByPage(1));
  })();
});


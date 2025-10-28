// js/watched.js – "Assistidos" consumindo avaliações locais + backend
document.addEventListener("DOMContentLoaded", () => {
  const row      = document.getElementById("wathedRow") || document.getElementById("watchedRow");
  const scroller = document.getElementById("watchedScroller");
  const meta     = document.getElementById("watchedMeta");
  const dots     = document.getElementById("watchedDots");
  const btnPrev  = document.getElementById("btnPrev");
  const btnNext  = document.getElementById("btnNext");

  if (!row || !scroller || !meta || !dots) { console.warn("[watched] Estrutura HTML incompleta."); return; }

  if (!API.hasAuth()) { location.href = `login.html?redirect=${encodeURIComponent("watched.html")}`; return; }

  let ratingsDict = {};
  try { const raw = localStorage.getItem("cinefile_ratings"); ratingsDict = raw ? JSON.parse(raw) : {}; } catch { ratingsDict = {}; }

  const entries = Object.keys(ratingsDict).map(id => ({ id: String(id), rating: Number(ratingsDict[id]?.rating || 0), ts: Number(ratingsDict[id]?.ts || 0) }))
    .filter(e => e.rating > 0).sort((a, b) => (b.ts || 0) - (a.ts || 0));

  if (!entries.length) {
    meta.textContent = "0 títulos";
    const p = document.createElement("p"); p.className = "muted"; p.style.padding = "8px 20px";
    p.textContent = "Ainda não há títulos avaliados."; row.prepend(p); return;
  }

  const sanitizeId = (id) => {
    if (/^\d+$/.test(String(id))) return String(id);
    const m = String(id).match(/\d+/); return m ? m[0] : String(id);
  };

  (async () => {
    const items = [];
    for (const e of entries) {
      const sid = sanitizeId(e.id);
      try {
        const ob = await API.getObra(sid);
        const poster = ob?.poster_url || "";
        const title  = ob?.titulo || ob?.title || "Título";
        const year   = ob?.anolancamento || ob?.year || "";
        const type   = /seri|tv|series|serie/i.test(ob?.tipo || "") ? "series" : "movie";
        items.push({ id: String(ob?.obraid ?? sid), title, year, type, poster, rating: e.rating, ts: e.ts });
      } catch {
        console.warn("Obra não encontrada no backend:", e.id);
      }
    }

    meta.textContent = `${items.length} ${items.length === 1 ? "título" : "títulos"}`;
    scroller.innerHTML = ""; dots.innerHTML = "";

    const starStr = (n) => "★".repeat(n) + "☆".repeat(5 - n);

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
        link.innerHTML = `<article class="item"><img class="item__img" src="${item.poster || ""}" alt="Pôster de ${item.title}"></article>`;
      }
      const wrap = document.createElement("div");
      wrap.style.display = "grid"; wrap.style.justifyItems = "center";
      wrap.appendChild(link);
      const stars = document.createElement("div");
      stars.className = "watched__stars";
      stars.textContent = starStr(item.rating);
      wrap.appendChild(stars);
      scroller.appendChild(wrap);
    });

    const pageSize = 6;
    const totalPages = Math.ceil(items.length / pageSize);
    for (let i = 0; i < totalPages; i++) {
      const dot = document.createElement("button");
      dot.type = "button"; dot.className = "row__dot"; dot.setAttribute("aria-label", `Página ${i + 1}`);
      dot.addEventListener("click", () => scroller.scrollTo({ left: i * scroller.clientWidth, behavior: "smooth" }));
      dots.appendChild(dot);
    }

    const scrollByPage = (dir) => scroller.scrollBy({ left: dir * scroller.clientWidth, behavior: "smooth" });
    btnPrev?.addEventListener("click", () => scrollByPage(-1));
    btnNext?.addEventListener("click", () => scrollByPage(1));
  })();
});

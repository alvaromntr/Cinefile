// js/app.js (LEGADO, apenas para páginas com MOCK_DATA)
// Não use na home nova nem em details.html.

(function () {
  document.addEventListener("DOMContentLoaded", () => {
    const DATA = window.MOCK_DATA || window.DATA || [];
    // se não houver nenhum carrossel esperado, não faz nada
    const get = (id) => document.getElementById(id);
    const scFeatured = get("featuredScroller");
    const scMovies   = get("moviesScroller");
    const scSeries   = get("seriesScroller");
    const scSearch   = get("searchScroller");
    const rowSearch  = get("rowSearch");
    const searchCount= get("searchCount");
    const input      = get("searchInput");

    if (!scFeatured && !scMovies && !scSeries && !scSearch) return;

    const BATCH = 24;

    function nodeFor(item, opts) {
      if (typeof window.createCarouselItem === "function") {
        return window.createCarouselItem(item, opts);
      }
      // fallback mínimo
      const a = document.createElement("a");
      a.href = `details.html?id=${encodeURIComponent(String(item.id))}`;
      a.className = "card card--sm";
      a.style.display = "inline-block";
      a.style.margin = "6px";
      const img = document.createElement("img");
      img.src = item.poster || "";
      img.alt = item.title || "Poster";
      img.style.width = "150px";
      img.style.height = "225px";
      img.style.objectFit = "cover";
      img.style.borderRadius = "8px";
      a.appendChild(img);
      return a;
    }

    function renderBatch(scroller, list, opts) {
      if (!scroller) return;
      scroller.innerHTML = "";
      let i = 0;
      function step() {
        const frag = document.createDocumentFragment();
        for (let n = 0; n < BATCH && i < list.length; n++, i++) {
          frag.appendChild(nodeFor(list[i], opts));
        }
        scroller.appendChild(frag);
        if (i < list.length) requestAnimationFrame(step);
      }
      requestAnimationFrame(step);
    }

    const featured = DATA.slice(0, 20);
    const movies   = DATA.filter(x => x.type === "movie");
    const series   = DATA.filter(x => x.type === "series");

    renderBatch(scFeatured, featured, { showOverlay: false });
    renderBatch(scMovies,   movies,   { showOverlay: false });
    renderBatch(scSeries,   series,   { showOverlay: false });

    function debounce(fn, wait = 250) {
      let t; return (...args) => { clearTimeout(t); t = setTimeout(() => fn(...args), wait); };
    }

    if (input && scSearch && rowSearch && searchCount) {
      const doSearch = debounce(() => {
        const q = (input.value || "").toLowerCase().trim();
        if (!q) { rowSearch.hidden = true; scSearch.innerHTML = ""; return; }
        const found = DATA.filter(x =>
          (x.title || "").toLowerCase().includes(q) ||
          String(x.year || "").includes(q)
        );
        searchCount.textContent = `${found.length} resultado${found.length !== 1 ? "s" : ""}`;
        rowSearch.hidden = false;
        renderBatch(scSearch, found, { showOverlay: false });
      }, 250);
      input.addEventListener("input", doSearch, { passive: true });
    }

    document.querySelectorAll(".row__ctrlBtn").forEach(btn => {
      const targetId = btn.getAttribute("data-target");
      const dir = btn.getAttribute("data-dir");
      const t = get(targetId);
      if (!t) return;
      btn.addEventListener("click", () => {
        const delta = Math.floor(t.clientWidth * 0.85) * (dir === "next" ? 1 : -1);
        t.scrollBy({ left: delta, behavior: "smooth" });
      });
    });
  });
})();
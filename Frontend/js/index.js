// js/index.js – Catálogo consumindo /obras
function setCatalogState(html = "", show = true) {
  const box = document.getElementById("catalogState");
  if (!box) return;
  box.innerHTML = html;
  box.hidden = !show;
}

document.addEventListener("DOMContentLoaded", () => { bootCatalog(); });

let ALL_ITEMS = [];

async function bootCatalog() {
  const featuredScroller = document.getElementById("featuredScroller");
  const moviesScroller   = document.getElementById("moviesScroller");
  const seriesScroller   = document.getElementById("seriesScroller");
  const rowSearch        = document.getElementById("rowSearch");
  const searchScroller   = document.getElementById("searchScroller");
  const searchForm       = document.getElementById("searchForm");
  const searchInput      = document.getElementById("searchInput");
  const searchCount      = document.getElementById("searchCount");
  const clear = (el) => { if (el) el.innerHTML = ""; };

  try {
    setCatalogState('<span class="muted">Carregando catálogo...</span>', true);
    clear(featuredScroller); clear(moviesScroller); clear(seriesScroller);
    if (rowSearch) rowSearch.hidden = true;

    // >>> ajuste principal: usar API.Obras.listar
    const raw   = await API.Obras.listar("page=0&size=60");
    const obras = Array.isArray(raw) ? raw : (raw?.content || []);

    if (!Array.isArray(obras) || obras.length === 0) {
      setCatalogState("Nenhum título disponível no momento.", true);
      return;
    }

    const items = obras.map((o, i) => {
      const n = normalizeObraForUI(o);
      if (n.id === undefined || n.id === null || n.id === "") n.id = o?.id ?? o?.obraId ?? o?.obraid ?? String(i + 1);
      n.id = String(n.id);
      return n;
    });

    ALL_ITEMS = items;
    setCatalogState("", false);

    const featured = items.slice(0, 15);
    const movies   = items.filter(x => x.type === "movie");
    const series   = items.filter(x => x.type === "series");

    renderScroller(featuredScroller, featured);
    renderScroller(moviesScroller, movies);
    renderScroller(seriesScroller, series);

    if (searchForm && searchInput && searchScroller && rowSearch && searchCount) {
      const doSearch = () => {
        const q = (searchInput.value || "").trim().toLowerCase();
        if (!q) {
          rowSearch.hidden = true;
          clear(searchScroller);
          searchCount.textContent = "0 resultados";
          return;
        }
        const res = ALL_ITEMS.filter(x =>
          (x.title || "").toLowerCase().includes(q) || String(x.year || "").includes(q)
        );
        clear(searchScroller);
        renderScroller(searchScroller, res);
        searchCount.textContent = `${res.length} ${res.length === 1 ? "resultado" : "resultados"}`;
        rowSearch.hidden = false;
      };
      searchForm.addEventListener("submit", e => { e.preventDefault(); doSearch(); });
      searchInput.addEventListener("input", () => { if (!searchInput.value) doSearch(); });
    }

    wireRowArrows();
  } catch (err) {
    console.error("[index] erro:", err);
    setCatalogState('<p style="padding:8px;color:#b00">Erro ao carregar catálogo.</p>', true);
  }
}

function normalizeObraForUI(ob) {
  const rawId = ob?.obraid ?? ob?.id ?? ob?.obraId ?? ob?.codigo;
  const title = ob?.titulo ?? ob?.title ?? ob?.nome ?? "Título";
  const year  = ob?.anolancamento ?? ob?.year ?? ob?.ano ?? "";
  let type    = ob?.tipo ?? ob?.type ?? "";
  if (typeof type === "string") {
    const t = type.toLowerCase();
    if (t.includes("film") || t === "filme" || t === "movie") type = "movie";
    else if (t.includes("seri") || t === "tv" || t === "series" || t === "serie") type = "series";
    else type = "";
  }
  let poster = ob?.poster_url ?? ob?.poster ?? ob?.capa ?? ob?.imagem ?? ob?.poster_path ?? "";
  if (poster && poster.startsWith("/")) poster = `https://image.tmdb.org/t/p/w342${poster}`;
  return { id: String(rawId), title, year, type, poster, ...ob };
}

function renderScroller(scroller, itemsArr) {
  if (!scroller) return;
  scroller.innerHTML = "";
  if (!Array.isArray(itemsArr) || itemsArr.length === 0) {
    scroller.innerHTML = '<p class="muted" style="padding:6px">Sem itens.</p>';
    return;
  }
  const frag = document.createDocumentFragment();
  for (const item of itemsArr) {
    try {
      if (typeof window.createCarouselItem === "function") {
        const node = window.createCarouselItem(item);
        frag.appendChild(node || createFallbackCard(item));
      } else {
        frag.appendChild(createFallbackCard(item));
      }
    } catch {
      frag.appendChild(createFallbackCard(item));
    }
  }
  scroller.appendChild(frag);
}

function createFallbackCard(item) {
  const a = document.createElement("a");
  a.href = `details.html?id=${encodeURIComponent(item.id)}`;
  a.className = "card card--sm";
  a.style.display = "inline-block";
  a.style.margin = "6px";
  a.style.textDecoration = "none";
  const img = document.createElement("img");
  img.src = item.poster || "";
  img.alt = item.title || "Poster";
  img.style.width = "150px";
  img.style.height = "225px";
  img.style.objectFit = "cover";
  img.style.borderRadius = "8px";
  img.style.display = "block";
  const cap = document.createElement("div");
  cap.className = "card__caption";
  cap.style.fontSize = "12px";
  cap.style.marginTop = "4px";
  cap.style.color = "inherit";
  cap.textContent = `${item.title}${item.year ? ` (${item.year})` : ""}`;
  a.appendChild(img);
  a.appendChild(cap);
  return a;
}

function wireRowArrows() {
  document.querySelectorAll(".row__ctrlBtn").forEach(btn => {
    const targetId = btn.getAttribute("data-target");
    const dir = btn.getAttribute("data-dir");
    const scroller = document.getElementById(targetId);
    if (!scroller) return;
    btn.addEventListener("click", () => {
      const delta = Math.floor(scroller.clientWidth * 0.85) * (dir === "next" ? 1 : -1);
      scroller.scrollBy({ left: delta, behavior: "smooth" });
    });
  });
}

// Carrega catálogo do backend quando disponível; senão, usa mock
async function loadCatalogData(){
  try {
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
          tmdbId: (o?.tmdbId ?? o?.tmdb_id ?? o?.tmdbID ?? o?.tmdbid) ?? null,
          poster: o?.poster_url || "",
          rating: o?.mediaAvaliacao ?? o?.rating ?? 0,
        });
        const data = Array.isArray(arr) ? arr.map(toItem) : [];
        if (data.length) { window.DATA = data; return data; }
      }
    }
  } catch(_) { /* ignore */ }
  return window.DATA || [];
}

document.addEventListener("DOMContentLoaded", async () => {
  const DATA = await loadCatalogData();

  const scFeatured = document.getElementById("featuredScroller");
  const scMovies   = document.getElementById("moviesScroller");
  const scSeries   = document.getElementById("seriesScroller");
  const scSearch   = document.getElementById("searchScroller");
  const rowSearch  = document.getElementById("rowSearch");
  const searchCount= document.getElementById("searchCount");
  const input      = document.getElementById("searchInput");

  // Se não estamos na home, apenas prepara DATA e sai
  if (!scFeatured && !scMovies && !scSeries) {
    window.__APP_INIT_DONE__ = true;
    return;
  }

  const featured = DATA.slice(0, 20);
  const movies   = DATA.filter(x => x.type === "movie");
  const series   = DATA.filter(x => x.type === "series");

  const BATCH = 24;
  function renderBatch(scroller, list, opts){
    scroller.innerHTML = "";
    let i = 0;
    function step(){
      const frag = document.createDocumentFragment();
      for(let n=0; n<BATCH && i<list.length; n++, i++){
        frag.appendChild(createCarouselItem(list[i], opts));
      }
      scroller.appendChild(frag);
      if(i < list.length) requestAnimationFrame(step);
    }
    requestAnimationFrame(step);
  }

  renderBatch(scFeatured, featured, { showOverlay:false });
  renderBatch(scMovies,   movies,   { showOverlay:false });
  renderBatch(scSeries,   series,   { showOverlay:false });

  // busca com debounce
  let t;
  input?.addEventListener("input", e=>{
    clearTimeout(t);
    t=setTimeout(()=>{
      const q=(e.target.value||"").toLowerCase().trim();
      if(!q){ rowSearch.hidden=true; scSearch.innerHTML=""; return; }
      const found = DATA.filter(x =>
        (x.title||"").toLowerCase().includes(q) || String(x.year||"").includes(q)
      );
      searchCount.textContent=`${found.length} resultado${found.length!==1?"s":""}`;
      rowSearch.hidden=false;
      renderBatch(scSearch, found, { showOverlay:false });
    }, 250);
  });

  // setas
  document.querySelectorAll(".row__ctrlBtn").forEach(btn=>{
    btn.addEventListener("click", ()=>{
      const t=document.getElementById(btn.getAttribute("data-target"));
      if(!t) return;
      const dir=btn.getAttribute("data-dir");
      t.scrollBy({ left: (dir==="next"?800:-800), behavior:"smooth" });
  });

  // marca inicialização desta página (evita bloco duplicado abaixo)
  window.__APP_INIT_DONE__ = true;
});

// uso do debounce na busca e montagem das linhas sem re-render excessivo

function debounce(fn, wait = 250) {
  let t;
  return (...args) => {
    clearTimeout(t);
    t = setTimeout(() => fn(...args), wait);
  };
}

document.addEventListener("DOMContentLoaded", async () => {
  if (window.__APP_INIT_DONE__) return; // já inicializado acima
  const DATA = await loadCatalogData();

  // Monta seções uma vez (Em Alta, Filmes, Séries) — carrosséis reaproveitam createCarouselItem
  function mountRow(scrollerId, list) {
    const scroller = document.getElementById(scrollerId);
    if (!scroller) return;
    scroller.innerHTML = "";
    list.forEach(item => scroller.appendChild(createCarouselItem(item)));
  }

  // Exemplo de filtro inicial (ajuste ao seu dataset)
  const featured = DATA.slice(0, 14); // não coloque 500 itens de primeira
  const movies   = DATA.filter(x => x.type === "movie").slice(0, 40);
  const series   = DATA.filter(x => x.type === "series").slice(0, 40);

  mountRow("featuredScroller", featured);
  mountRow("moviesScroller", movies);
  mountRow("seriesScroller", series);

  // Busca com debounce
  const input = document.getElementById("searchInput");
  const rowSearch = document.getElementById("rowSearch");
  const searchScroller = document.getElementById("searchScroller");
  const searchCount = document.getElementById("searchCount");

  const doSearch = debounce(() => {
    const q = (input.value || "").trim().toLowerCase();
    if (!q) {
      rowSearch.hidden = true;
      searchScroller.innerHTML = "";
      return;
    }
    const results = DATA.filter(x => (x.title || "").toLowerCase().includes(q));
    searchScroller.innerHTML = "";
    results.slice(0, 100).forEach(item => searchScroller.appendChild(createCarouselItem(item)));
    searchCount.textContent = `${results.length} resultados`;
    rowSearch.hidden = false;
  }, 250);

  if (input) input.addEventListener("input", doSearch, { passive: true });
});


});


// js/watchlist.js – sem imports, usando window.API
document.addEventListener("DOMContentLoaded", () => {
  initWatchlist().catch(err => {
    console.error("[watchlist] erro:", err);
    const stateBox = document.getElementById("wlState");
    if (stateBox) {
      stateBox.hidden = false;
      stateBox.innerHTML = `<p style="padding:6px;color:#b00">Erro ao carregar sua Watchlist. Tente novamente.</p>`;
    }
  });
});

function isLogged() { return !!localStorage.getItem("cinefile_auth_basic"); }

async function initWatchlist() {
  const stateBox  = document.getElementById("wlState");
  const boxMovies = document.getElementById("wlMovies");
  const boxSeries = document.getElementById("wlSeries");
  const tabMovies = document.getElementById("tabMovies");
  const tabSeries = document.getElementById("tabSeries");

  if (!stateBox || !boxMovies || !boxSeries || !tabMovies || !tabSeries) {
    console.warn("watchlist.js: estrutura incompleta.");
    return;
  }

  function activate(which) {
    const isMovies = which === "movies";
    tabMovies.classList.toggle("is-active", isMovies);
    tabSeries.classList.toggle("is-active", !isMovies);
    boxMovies.classList.toggle("is-hidden", !isMovies);
    boxSeries.classList.toggle("is-hidden",  isMovies);
    tabMovies.setAttribute("aria-selected", String(isMovies));
    tabSeries.setAttribute("aria-selected", String(!isMovies));
    boxMovies.setAttribute("aria-hidden",   String(!isMovies));
    boxSeries.setAttribute("aria-hidden",   String(isMovies));
  }
  tabMovies.addEventListener("click", () => activate("movies"));
  tabSeries.addEventListener("click", () => activate("series"));

  const clear = el => { if (el) el.innerHTML = ""; };
  function showState(html = "", show = true) { stateBox.innerHTML = html; stateBox.hidden = !show; }
  function renderGrid(container, list) {
    clear(container);
    if (!list || !list.length) {
      const p = document.createElement("p");
      p.className = "muted"; p.style.padding = "6px";
      p.textContent = "Sem itens nesta categoria.";
      container.appendChild(p);
      return;
    }
    list.forEach(item => {
      const node = (typeof window.createCarouselItem === "function")
        ? (window.createCarouselItem(item) || createFallbackCard(item))
        : createFallbackCard(item);
      container.appendChild(node);
    });
  }

  if (!isLogged()) {
    const back = encodeURIComponent("watchlist.html");
    showState(`
      <strong>Você não está logado.</strong><br/>
      Para ver e gerenciar sua Watchlist, faça login.
      <div style="margin-top:8px">
        <a class="btn" href="login.html?redirect=${back}">Entrar</a>
      </div>
    `, true);
    clear(boxMovies); clear(boxSeries);
    activate("movies");
    return;
  }

  showState(`<span class="muted">Carregando sua Watchlist...</span>`, true);
  clear(boxMovies); clear(boxSeries);
  activate("movies");

  const rawList = await API.getWatchlist();
  const ids = normalizeWatchlistToIds(rawList);

  if (!ids.length) {
    showState(`Sua Watchlist está vazia.<br/>Abra um título e clique em <strong>+ Watchlist</strong>.`, true);
    return;
  }

  const items = await fetchObrasDetails(ids);
  const movies = items.filter(x => x.type === "movie");
  const series = items.filter(x => x.type === "series");

  showState("", false);
  renderGrid(boxMovies, movies);
  renderGrid(boxSeries, series);
}

function normalizeWatchlistToIds(arr) {
  if (!Array.isArray(arr)) return [];
  const out = [];
  for (const x of arr) {
    if (!x) continue;
    if (typeof x === "number" || typeof x === "string") out.push(String(x));
    else if (typeof x === "object") {
      if (x.obraId != null) out.push(String(x.obraId));
      else if (x.id != null) out.push(String(x.id));
      else if (x.obraid != null) out.push(String(x.obraid));
    }
  }
  return [...new Set(out)];
}

async function fetchObrasDetails(ids) {
  const promises = ids.map(async (id) => {
    try {
      const ob = await API.getObra(id);
      const rawId = ob?.obraid ?? ob?.id ?? id;
      const title = ob?.titulo ?? ob?.title ?? ob?.nome ?? "Título";
      const year  = ob?.anolancamento ?? ob?.year ?? ob?.ano ?? "";
      let type = "";
      const t = (ob?.tipo ?? ob?.type ?? "").toLowerCase();
      if (t.includes("film") || t === "filme" || t === "movie") type = "movie";
      else if (t.includes("seri") || t === "tv" || t === "series" || t === "serie") type = "series";
      let poster = ob?.poster_url ?? ob?.poster ?? ob?.capa ?? ob?.imagem ?? ob?.poster_path ?? "";
      if (poster && poster.startsWith("/")) poster = `https://image.tmdb.org/t/p/w342${poster}`;
      return { id: String(rawId), title, year, type, poster, ...ob };
    } catch { return null; }
  });
  return (await Promise.all(promises)).filter(Boolean);
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
  img.style.width = "120px";
  img.style.height = "180px";
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

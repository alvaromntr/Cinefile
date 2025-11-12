// watchlist separada entre filmes e séries 

// aguarda o DOM carregar
document.addEventListener("DOMContentLoaded", async () => {
// garante catálogo carregado do backend quando disponível
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
// declara variável stateBox
  const stateBox = document.getElementById("wlState");
// declara variável boxMovies
  const boxMovies = document.getElementById("wlMovies");
// declara variável boxSeries
  const boxSeries = document.getElementById("wlSeries");
// declara variável tabMovies
  const tabMovies = document.getElementById("tabMovies");
// declara variável tabSeries
  const tabSeries = document.getElementById("tabSeries");

  // Alternância de abas
// define função activate
  function activate(which){
// declara variável isMovies
    const isMovies = which === "movies";
// manipula classes CSS
    tabMovies.classList.toggle("is-active", isMovies);
// manipula classes CSS
    tabSeries.classList.toggle("is-active", !isMovies);

// manipula classes CSS
    boxMovies.classList.toggle("is-hidden", !isMovies);
// manipula classes CSS
    boxSeries.classList.toggle("is-hidden",  isMovies);

    // Acessibilidade
    tabMovies.setAttribute("aria-selected", String(isMovies));
    tabSeries.setAttribute("aria-selected", String(!isMovies));
    boxMovies.setAttribute("aria-hidden", String(!isMovies));
    boxSeries.setAttribute("aria-hidden", String(isMovies));
  }
// adiciona ouvinte de evento
  tabMovies.addEventListener("click", () => activate("movies"));
// adiciona ouvinte de evento
  tabSeries.addEventListener("click", () => activate("series"));

  // Helpers
// declara variável clear
  const clear = (el) => { el.innerHTML = ""; };
// define função renderGrid
  function renderGrid(container, list){
    clear(container);
// condição
    if (!list || !list.length){
// declara variável p
      const p = document.createElement("p");
// atribui valor
      p.className = "muted";
// atribui valor
      p.style.padding = "6px";
// atribui valor
      p.textContent = "Sem itens nesta categoria.";
// adiciona nó ao DOM
      container.appendChild(p);
// retorna
      return;
    }
// itera coleção
    list.forEach(item => container.appendChild(createCarouselItem(item)));
  }
// define função showState
  function showState(html = "", show = true){
// define HTML interno
    stateBox.innerHTML = html;
// atribui valor
    stateBox.hidden = !show;
  }

  // Fluxo
// declara variável DATA
  await ensureCatalogLoaded();
  const DATA = window.DATA || [];

// condição
  if (!LISTS || !LISTS.isLogged()){
// declara variável back
    const back = encodeURIComponent("watchlist.html");
    showState(`
      <strong>Você não está logado.</strong><br/>
      Para ver e gerenciar sua Watchlist, faça login.
      <div style="margin-top:8px">
        <a class="btn" href="login.html?redirect=${back}">Entrar</a>
      </div>
    `, true);
    clear(boxMovies); clear(boxSeries);
    activate("movies"); // ainda assim, só um painel visível
// retorna
    return;
  }

  // --- ORDEM DE ADIÇÃO ---
  
  // LISTS.all() retorna um array na ordem em que o usuário foi adicionando.
  // Queremos o MAIS NOVO primeiro => invertido.
// declara variável idList
  const idList = LISTS.all().slice().reverse(); // ex.: ["idC","idB","idA"]

  // Cruza com o catálogo preservando a ordem escolhida
// declara variável mapById
  const mapById = new Map(DATA.map(x => [x.id, x]));
// declara variável orderedItems
  const orderedItems = idList.map(id => mapById.get(id)).filter(Boolean);

// declara variável movies
  const movies = orderedItems.filter(x => x.type === "movie");
// declara variável series
  const series = orderedItems.filter(x => x.type === "series");

  showState(orderedItems.length ? "" : `
    Sua Watchlist está vazia.<br/>
    Dica: abra um título e clique em <strong>+ Watchlist</strong>.
  `, !orderedItems.length);

  renderGrid(boxMovies, movies);
  renderGrid(boxSeries, series);

  // Inicia em FILMES
  activate("movies");
});


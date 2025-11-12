// Função para criar estrelas
function starString(n) {
  const s = Math.max(0, Math.min(5, Number(n || 0)));
  return "★".repeat(s) + "☆".repeat(5 - s);
}

// Placeholder leve (SVG)
function buildPlaceholder(title = "Sem título") {
  const safe = String(title).replace(/&/g, "&amp;").slice(0, 50);
  const svg = `
    <svg xmlns='http://www.w3.org/2000/svg' viewBox='0 0 342 513'>
      <defs><linearGradient id='g' x1='0' x2='0' y1='0' y2='1'>
        <stop offset='0%' stop-color='#0f1419'/><stop offset='100%' stop-color='#1a2128'/>
      </linearGradient></defs>
      <rect width='100%' height='100%' fill='url(#g)'/>
      <text x='50%' y='50%' fill='#f5c518' font-size='24' font-family='sans-serif'
            text-anchor='middle' dominant-baseline='middle'>${safe}</text>
    </svg>`;
  return "data:image/svg+xml;utf8," + encodeURIComponent(svg);
}

// Observador único de lazy-loading
const PosterObserver = (() => {
  let io;
  const watched = new WeakMap();
  function ensure() {
    if (io) return io;
    io = new IntersectionObserver((entries) => {
      entries.forEach(async (en) => {
        if (!en.isIntersecting) return;
        const img = en.target;
        const meta = watched.get(img);
        if (!meta) return;
        io.unobserve(img);
        if (img.dataset.fetched === "1") return;
        img.dataset.fetched = "1";

        const url = await TMDB.getPosterSmart({
          tmdbId: meta.tmdbId || null,
          typeHere: meta.typeHere,
          title: meta.title,
          year: meta.year
        });
        if (url) img.src = url;
      });
    }, { rootMargin: "600px 0px" });
    return io;
  }
  function observe(img, meta) {
    watched.set(img, meta);
    ensure().observe(img);
  }
  return { observe };
})();

// ==============================
// Função para INDEX / carrossel
// ==============================
function createCarouselItem(item) {
  const link = document.createElement("a");
  link.className = "item__link";
  // A página de detalhes consome o ID do TMDB para buscar no próprio TMDB.
  // Alguns dados vindos do backend têm `id` interno (DB) e `tmdbId` separado.
  // Para garantir, priorizamos `tmdbId` e, se ausente, caímos para `id`.
  const detailsId = (item.tmdbId ?? item.id ?? "").toString();
  const obraIdParam = item.id != null ? `&obraId=${encodeURIComponent(String(item.id))}` : "";
  link.href = `details.html?id=${encodeURIComponent(detailsId)}&type=${item.type || "movie"}${obraIdParam}`;
  link.setAttribute("aria-label", `${item.title} (${item.year})`);

  const card = document.createElement("article");
  card.className = "item";

  const img = document.createElement("img");
  img.className = "item__img";
  img.loading = "lazy";
  img.src = buildPlaceholder(item.title);
  img.alt = `Poster de ${item.title}`;

  PosterObserver.observe(img, {
    tmdbId: item.tmdbId || null,
    typeHere: item.type === "series" ? "series" : "movie",
    title: item.title,
    year: item.year
  });

  const overlay = document.createElement("div");
  overlay.className = "item__overlay";
  overlay.innerHTML = `
    <div class="item__ovTitle">${item.title}</div>
    <div class="item__ovMeta">${item.year} • ${item.type === "movie" ? "Filme" : "Série"}</div>
    <div class="item__ovStars">${starString(item.rating || 0)}</div>
  `;

  const body = document.createElement("div");
  body.className = "item__body";
  body.innerHTML = `
    <h3 class="item__title">${item.title}</h3>
    <p class="item__meta">${item.year} • ${item.type === "movie" ? "Filme" : "Série"}</p>
  `;

  card.appendChild(img);
  card.appendChild(overlay);
  card.appendChild(body);
  link.appendChild(card);
  return link;
}

// ==============================
// Função exclusiva para DETAILS
// ==============================
function createDetailsCard(item) {
  const card = document.createElement("article");
  card.className = "details__card-inner";

  const poster = document.createElement("img");
  poster.className = "details__poster";
  poster.src = buildPlaceholder(item.title || item.name);
  poster.alt = `Poster de ${item.title || item.name}`;
  PosterObserver.observe(poster, {
    tmdbId: item.id,
    typeHere: item.media_type === "tv" ? "series" : "movie",
    title: item.title || item.name,
    year: item.release_date ? item.release_date.slice(0,4) : (item.first_air_date ? item.first_air_date.slice(0,4) : "")
  });

  const info = document.createElement("div");
  info.className = "details__info";
  info.innerHTML = `
    <h1 class="details__title">${item.title || item.name}</h1>
    <p class="details__meta">
      ${(item.release_date ? item.release_date.slice(0,4) : (item.first_air_date ? item.first_air_date.slice(0,4) : ""))} • 
      ${(item.genres ? item.genres.map(g=>g.name).join(", ") : "")}
    </p>
    <p class="details__rating">${starString(item.vote_average ? Math.round(item.vote_average/2) : 0)}</p>
    <p class="details__overview">${item.overview || "Sinopse não disponível."}</p>
  `;

  card.appendChild(poster);
  card.appendChild(info);

  return card;
}

// js/details.js – Detalhes + Avaliar + Watchlist
document.addEventListener("DOMContentLoaded", initDetails);

async function initDetails() {
  const card = document.getElementById("detailsCard");
  if (!card) return;

  const id = new URLSearchParams(location.search).get("id");
  if (!id) { card.textContent = "ID ausente."; return; }

  try {
    const ob = await API.getObra(id);
    renderDetails(card, ob);
    setupRating(id);
    setupWatchlist(id);
  } catch (err) {
    console.error(err);
    card.textContent = "Erro ao carregar detalhes.";
  }
}

function renderDetails(card, ob) {
  const year   = ob.anolancamento || "";
  const poster = ob.poster_url || "";
  const desc   = ob.descricao || "Sem descrição disponível.";
  const title  = ob.titulo || ob.title || "Título";

  card.innerHTML = `
    <div class="details__posterBox">
      <img class="details__poster" src="${poster}" alt="${title}">
    </div>

    <div class="details__info">
      <h1 class="details__title">${title}</h1>
      <p class="details__meta">${year}</p>
      <p class="details__desc">${desc}</p>

      <section class="details__actions">
        <div>
          <span class="rating__label">Avaliação:</span>
          <div id="ratingStars" class="rating"></div>
        </div>
        <button id="btnWatchlist" class="btn btn--wl">+ Watchlist</button>
      </section>
    </div>
  `;
}

function setupRating(id) {
  const box = document.getElementById("ratingStars");
  if (!box) return;

  const current = (window.Ratings?.get(id)?.rating) || 0;
  renderStars(box, current);

  if (!API.hasAuth()) return;

  box.addEventListener("click", async (ev) => {
    const star = ev.target.closest("span[data-val]");
    if (!star) return;
    const val = Number(star.dataset.val);
    try { await API.avaliar(id, val); }
    catch { window.Ratings?.set(id, val); }
    renderStars(box, val);
  });
}

function renderStars(container, value) {
  container.innerHTML = "";
  for (let i = 1; i <= 5; i++) {
    const s = document.createElement("span");
    s.dataset.val = i;
    s.className = "rating__star";
    s.textContent = i <= value ? "★" : "☆";
    container.appendChild(s);
  }
}

async function setupWatchlist(id) {
  const btn = document.getElementById("btnWatchlist");
  if (!btn) return;

  if (!API.hasAuth()) {
    btn.textContent = "Entrar para salvar";
    btn.addEventListener("click", () => {
      location.href = `login.html?redirect=${encodeURIComponent(location.pathname + location.search)}`;
    });
    return;
  }

  let lista = [];
  try { lista = await API.getWatchlist(); } catch {}
  const isIn = Array.isArray(lista) && lista.some(x => String(x.obraId ?? x.id ?? x.obraid) === String(id));

  updateWatchlistButton(btn, isIn);

  btn.addEventListener("click", async () => {
    const novo = btn.dataset.state !== "1";
    try {
      if (novo) await API.addWatchlist(id);
      else await API.removeWatchlist(id);
      updateWatchlistButton(btn, novo);
    } catch {
      alert("Erro ao alterar Watchlist!");
    }
  });
}

function updateWatchlistButton(btn, isActive) {
  btn.dataset.state = isActive ? "1" : "0";
  btn.textContent   = isActive ? "✓ Na Watchlist" : "+ Watchlist";
  btn.classList.toggle("btn--active", isActive);
}

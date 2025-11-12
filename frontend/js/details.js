// details.js
// details.js
document.addEventListener("DOMContentLoaded", () => {
  const detailsCard = document.getElementById("detailsCard");

  function paramsFromURL() {
    const p = new URLSearchParams(window.location.search);
    return {
      tmdbId: p.get("id") || p.get("tmdbId") || p.get("tmdb_id"),
      obraId: p.get("obraId"),
      type: p.get("type") || "movie"
    };
  }

  function buildActions({ obraId, tmdbId }){
    const wrap = document.createElement("div");
    wrap.className = "details__rating";

    // Rating sempre pode usar um id estável (obraId quando existir, senão tmdbId)
    const ratingId = String(obraId || tmdbId);
    const ratingBox = document.createElement("div");
    ratingBox.className = "rating";

    let current = (window.Ratings?.get(ratingId)?.rating) || 0;
    let hoverVal = 0;
    const render = (val) => {
      ratingBox.querySelectorAll(".rating__star").forEach((b, i) => {
        const on = (hoverVal || val) >= (i + 1);
        b.classList.toggle("is-on", on);
        b.setAttribute("aria-pressed", on ? "true" : "false");
      });
    };
    for (let i=1;i<=5;i++){
      const b=document.createElement("button");
      b.type="button"; b.className="rating__star"; b.textContent = "\u2605";
      b.addEventListener("mouseenter",()=>{hoverVal=i;render(current)});
      b.addEventListener("mouseleave",()=>{hoverVal=0;render(current)});
      b.addEventListener("click",()=>{
        try{ window.Ratings?.set(ratingId,i);}catch{}
        current = i; // mantém seleção após sair do hover
        render(current);
      });
      ratingBox.appendChild(b);
    }
    render(current);

    const actions = document.createElement("div");
    actions.style.display = "flex";
    actions.style.gap = "12px";
    actions.style.alignItems = "center";
    // Watchlist só aparece se soubermos o id da obra no backend
    if (obraId){
      const btn = document.createElement("button");
      btn.type="button"; btn.className="btn auth-action";
      const refresh = ()=>{
        const on = !!(window.LISTS && window.LISTS.has && window.LISTS.has(obraId));
        btn.textContent = (on ? "\u2713 Watchlist" : "+ Watchlist");
        btn.classList.toggle("is-on", on);
      };
      refresh();
      btn.addEventListener("click", (e)=>{
        e.preventDefault();
        try { const r = window.LISTS?.toggle?.(obraId); if (r && r.changed) refresh(); } catch {}
      });
      actions.appendChild(btn);
    }
    // Rótulo auxiliar
    const label = document.createElement("div");
    label.className = "rating__label";
    label.textContent = obraId ? "Avalie e salve na Watchlist" : "Avalie este título";

    actions.insertBefore(ratingBox, actions.firstChild);
    wrap.appendChild(actions);
    wrap.appendChild(label);
    return wrap;
  }

  async function renderDetailsPage(tmdbId, type) {
    try {
      const apiKey = TMDB.API_KEY || "ac239e8cfa775a3f0165865fbab3d463";
      const apiType = type === "series" ? "tv" : "movie";
      const url = `https://api.themoviedb.org/3/${apiType}/${tmdbId}?api_key=${apiKey}&language=pt-BR`;
      const res = await fetch(url);
      if (!res.ok) throw new Error("Erro ao buscar detalhes do item");
      const data = await res.json();

      detailsCard.innerHTML = "";
      const card = createDetailsCard({ ...data, media_type: apiType });

      // Injeta ações (rating e watchlist)
      const info = card.querySelector(".details__info");
      const { obraId } = paramsFromURL();
      if (info){
        const wrap = buildActions({ obraId, tmdbId });
        const staticRating = info.querySelector('.details__rating');
        if (staticRating && staticRating.parentNode === info){
          // move somente as estrelas (.rating) para dentro da linha do TMDB
          const overlay = wrap.querySelector('.rating');
          if (overlay){
            staticRating.style.position = 'relative';
            overlay.style.position = 'absolute';
            overlay.style.left = '0';
            overlay.style.top = '0';
            try {
              const cs = getComputedStyle(staticRating);
              overlay.style.fontSize      = cs.fontSize;
              overlay.style.lineHeight    = cs.lineHeight;
              overlay.style.letterSpacing = cs.letterSpacing;
              overlay.style.gap           = '0px';
            } catch {}
            staticRating.appendChild(overlay);
          }
          // insere o restante (botão + label) logo abaixo
          staticRating.insertAdjacentElement('afterend', wrap);
        } else {
          info.appendChild(wrap);
        }
      }

      detailsCard.appendChild(card);
    } catch (err) {
      console.error(err);
      detailsCard.innerHTML = "<p style='color:red;'>Não foi possível carregar os detalhes do item.</p>";
    }
  }

  const { tmdbId, type } = paramsFromURL();
  if (tmdbId) renderDetailsPage(tmdbId, type);
  else detailsCard.innerHTML = "<p style='color:red;'>ID do item não informado na URL.</p>";
});



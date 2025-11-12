// Watchlist (e Favoritos opcional) por usuário em localStorage, com
// compatibilidade com AUTH.isLogged() e normalização de IDs (string).

(function () {
  // ===== helpers de login/usuário =====
// define função isReallyLogged
  function isReallyLogged() {
// tenta executar bloco
    try {
// condição
      if (window.AUTH?.isLogged) return !!window.AUTH.isLogged(); // preferível
// captura erro
    } catch {}
    // compat: flags antigas/novas
// retorna
    return (
// acessa localStorage
      localStorage.getItem("cinefile_logged")    === "true" ||
// acessa localStorage
      localStorage.getItem("cinefile_logged_in") === "true" ||
// acessa localStorage
      !!localStorage.getItem("cinefile_username")
    );
  }
// define função currentUser
  function currentUser() {
// acessa localStorage
    return localStorage.getItem("cinefile_username") || "";
  }

  // ===== keys por usuário =====
// declara variável KEY_W
  const KEY_W = (u) => `cinefile_watchlist_${u}`;
// declara variável KEY_F
  const KEY_F = (u) => `cinefile_favorites_${u}`;

  // ===== utilidades de leitura/escrita =====
// define função loadSet
  function loadSet(key) {
// itera coleção
    try { return new Set(JSON.parse(localStorage.getItem(key) || "[]").map(String)); }
// retorna
    catch { return new Set(); }
  }
// define função saveSet
  function saveSet(key, set) {
// itera coleção
    try { localStorage.setItem(key, JSON.stringify([...set].map(String))); } catch {}
  }
// define função ensureCtx
  function ensureCtx(kind /* 'wl'|'fav' */) {
// declara variável u
    const u = currentUser();
// condição
    if (!isReallyLogged() || !u) {
// retorna
      return { key: null, set: new Set(), user: u };
    }
// declara variável key
    const key = (kind === "fav" ? KEY_F(u) : KEY_W(u));
// retorna
    return { key, set: loadSet(key), user: u };
  }

  // Sincroniza watchlist local com backend ao iniciar (se logado)
  (async function syncWatchlistFromBackend(){
    try {
      const ctx = ensureCtx("wl");
      if (!ctx.key) return;
      const base = (localStorage.getItem("cinefile_api_base") || "http://localhost:8080").replace(/\/$/, "");
      const auth = localStorage.getItem("cinefile_auth_basic");
      const res = await fetch(`${base}/api/watchlist`, {
        credentials: "include",
        headers: auth ? { "Authorization": auth } : undefined,
      });
      if (res.ok) {
        const arr = await res.json();
        // Backend retorna { obraId, obraTitulo }. Aceitar nomes alternativos por segurança.
        const ids = (Array.isArray(arr) ? arr : []).map(o => {
          const v = o?.obraId ?? o?.obraid ?? o?.id;
          return String(v ?? "");
        }).filter(x => x !== "");
        // Não zera o local se o backend estiver vazio (usuário não autenticado no servidor, por ex.).
        if (ids.length > 0) {
          ctx.set = new Set(ids);
          saveSet(ctx.key, ctx.set);
        }
      }
    } catch {}
  })();

  // ===== API Watchlist =====
// define função wlAll
  function wlAll() {
// atribui valor
    const { set } = ensureCtx("wl");
// retorna
    return [...set]; // strings
  }
// define função wlHas
  function wlHas(id) {
// atribui valor
    const { set } = ensureCtx("wl");
// retorna
    return set.has(String(id));
  }
// define função wlAdd
  function wlAdd(id) {
// declara variável ctx
    const ctx = ensureCtx("wl");
// condição
    if (!ctx.key) return false; // não logado
// declara variável sid
    const sid = String(id);
    ctx.set.add(sid);
    saveSet(ctx.key, ctx.set);
    try {
      const base = (localStorage.getItem("cinefile_api_base") || "http://localhost:8080").replace(/\/$/, "");
      const auth = localStorage.getItem("cinefile_auth_basic");
      fetch(`${base}/api/watchlist/${encodeURIComponent(sid)}`, {
        method: "POST",
        credentials: "include",
        headers: auth ? { "Authorization": auth } : undefined,
      });
    } catch {}
// retorna
    return true;
  }
// define função wlRemove
  function wlRemove(id) {
// declara variável ctx
    const ctx = ensureCtx("wl");
// condição
    if (!ctx.key) return false;
// declara variável sid
    const sid = String(id);
// declara variável ok
    const ok = ctx.set.delete(sid);
// condição
    if (ok) {
      saveSet(ctx.key, ctx.set);
      try {
      const base = (localStorage.getItem("cinefile_api_base") || "http://localhost:8080").replace(/\/$/, "");
      const auth = localStorage.getItem("cinefile_auth_basic");
      fetch(`${base}/api/watchlist/${encodeURIComponent(sid)}`, {
        method: "DELETE",
        credentials: "include",
        headers: auth ? { "Authorization": auth } : undefined,
      });
      } catch {}
    }
// retorna
    return ok;
  }
// define função wlToggle
  function wlToggle(id) {
// declara variável ctx
    const ctx = ensureCtx("wl");
// condição
    if (!ctx.key) return { changed: false, value: false, reason: "not_logged" };
// declara variável sid
    const sid = String(id);
// declara variável value
    let value;
// condição
    if (ctx.set.has(sid)) { ctx.set.delete(sid); value = false; }
// caso contrário
    else { ctx.set.add(sid); value = true; }
    saveSet(ctx.key, ctx.set);
    try {
      const base = (localStorage.getItem("cinefile_api_base") || (window.location?.origin || "")).replace(/\/$/, "");
      const method = value ? "POST" : "DELETE";
      const auth = localStorage.getItem("cinefile_auth_basic");
      fetch(`${base}/api/watchlist/${encodeURIComponent(sid)}`, {
        method,
        credentials: "include",
        headers: auth ? { "Authorization": auth } : undefined,
      });
    } catch {}
    // retorna
    return { changed: true, value };
  }

  // ===== API Favoritos (opcional) =====
// define função favAll
  function favAll() {
// atribui valor
    const { set } = ensureCtx("fav");
// retorna
    return [...set];
  }
// define função favHas
  function favHas(id) {
// atribui valor
    const { set } = ensureCtx("fav");
// retorna
    return set.has(String(id));
  }
// define função favToggle
  function favToggle(id) {
// declara variável ctx
    const ctx = ensureCtx("fav");
// condição
    if (!ctx.key) return { changed: false, value: false, reason: "not_logged" };
// declara variável sid
    const sid = String(id);
// declara variável value
    let value;
// condição
    if (ctx.set.has(sid)) { ctx.set.delete(sid); value = false; }
// caso contrário
    else { ctx.set.add(sid); value = true; }
    saveSet(ctx.key, ctx.set);
// retorna
    return { changed: true, value };
  }

  // ===== export =====
// atribui valor
  window.LISTS = {
    // login/usuário
    isLogged: isReallyLogged,
    currentUser,

    // watchlist
    all: wlAll,             // -> array de strings
    has: wlHas,             // (id) -> bool
    add: wlAdd,             // (id) -> bool (true se adicionou)
    remove: wlRemove,       // (id) -> bool
    toggle(id) {            // (id) -> {changed, value, reason?}
// declara variável r
      const r = wlToggle(id);
      // atalho prático: se quiser só booleano:
      // return !!wlToggle(id).value;
// retorna
      return r;
    },

    // favoritos (opcional)
    fav: {
      all: favAll,
      has: favHas,
      toggle: favToggle,
    },
  };
})();

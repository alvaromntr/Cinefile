// js/api.js — auto-detector com LOG (obras, avaliacoes, watchlist, usuarios)
(function (global) {
  const BASE = "http://localhost:8080";

  // ---------- Auth ----------
  function getBasic() { return localStorage.getItem("cinefile_auth_basic") || ""; }
  function setBasic(user, pass) {
    localStorage.setItem("cinefile_auth_basic", "Basic " + btoa(`${user}:${pass}`));
  }
  function clearBasic() { localStorage.removeItem("cinefile_auth_basic"); }
  function hasAuth() { return !!getBasic(); }
  function authHeaders() {
    const h = {};
    const b = getBasic();
    if (b) h["Authorization"] = b;
    return h;
  }

  // ---------- fetch helper ----------
  async function jfetch(url, opts = {}) {
    console.debug("[API] tentando:", url);
    const res = await fetch(url, {
      ...opts,
      headers: { "Content-Type": "application/json", ...authHeaders(), ...(opts.headers || {}) },
      credentials: "include",
      mode: "cors",
    });
    if (!res.ok) {
      const text = await res.text().catch(() => "");
      const err = new Error(`${res.status} ${res.statusText} :: ${text}`);
      err.status = res.status;
      err.body = text;
      throw err;
    }
    if (res.status === 204) return null;
    return res.json();
  }

  // ---------- tenta vários caminhos até um responder 2xx ----------
  async function tryEndpoints(paths, opts = {}) {
    let lastErr;
    for (const p of paths) {
      try {
        const url = BASE + p;
        const out = await jfetch(url, opts);
        console.info("[API] OK em:", url);
        return out;
      } catch (e) {
        console.warn("[API] falhou:", BASE + p, "->", e?.status || e?.message);
        lastErr = e;
        // se 404, tenta o próximo; se diferente, para
        if (e && e.status === 404) {
          continue;
        } else if (String(e?.message || "").includes("404")) {
          continue;
        } else {
          throw e;
        }
      }
    }
    // se todos deram 404, devolve o último erro 404 para aparecer no console
    throw lastErr || new Error("Nenhuma rota respondeu.");
  }

  // ---------- prefixos em ordem ----------
  const pref = ["", "/api", "/v1", "/api/v1"];

  function buildListPaths(base, query = "") {
    const q = query ? `?${query}` : "";
    return pref.map(px => `${px}${base}${q}`);
  }
  function buildIdPaths(base, id) {
    const enc = encodeURIComponent(id);
    return pref.map(px => `${px}${base}/${enc}`);
  }

  // ---------- API ----------
  const API = {
    baseURL: BASE,

    Auth: { get: getBasic, set: setBasic, clear: clearBasic, has: hasAuth },
    hasAuth,

    Obras: {
      async listar(query = "") {
        // tenta /obras, /api/obras, /v1/obras, /api/v1/obras
        const data = await tryEndpoints(buildListPaths("/obras", query));
        // suporta lista simples ou {content:[]}
        return Array.isArray(data) ? data : (data?.content || []);
      },
      async obter(id) {
        return tryEndpoints(buildIdPaths("/obras", id));
      }
    },
    // compat com código antigo
    async getObra(id) { return API.Obras.obter(id); },

    Usuarios: {
      async login(usernameOrEmail, senha) {
        return tryEndpoints(buildListPaths("/usuarios/login"), {
          method: "POST",
          body: JSON.stringify({ usernameOrEmail, senha })
        });
      },
      async cadastro({ username, email, senha }) {
        return tryEndpoints(buildListPaths("/usuarios/cadastro"), {
          method: "POST",
          body: JSON.stringify({ username, email, senha })
        });
      }
    },

    // Avaliações
    async avaliar(obraId, rating) {
      return tryEndpoints(buildListPaths("/avaliacoes"), {
        method: "POST",
        body: JSON.stringify({ obraId, nota: Number(rating) })
      });
    },

    // Watchlist
    async getWatchlist() {
      // nosso back mapeou como /api/me/watchlist em algumas versões; tentamos ambos
      const roots = ["/me/watchlist", "/api/me/watchlist", "/v1/me/watchlist", "/api/v1/me/watchlist"];
      return tryEndpoints(roots);
    },
    async addWatchlist(obraId) {
      const branches = ["/me/watchlist", "/api/me/watchlist", "/v1/me/watchlist", "/api/v1/me/watchlist"]
        .map(p => `${p}/${encodeURIComponent(obraId)}`);
      return tryEndpoints(branches, { method: "POST" });
    },
    async removeWatchlist(obraId) {
      const branches = ["/me/watchlist", "/api/me/watchlist", "/v1/me/watchlist", "/api/v1/me/watchlist"]
        .map(p => `${p}/${encodeURIComponent(obraId)}`);
      return tryEndpoints(branches, { method: "DELETE" });
    }
  };

  global.API = API;
})(window);
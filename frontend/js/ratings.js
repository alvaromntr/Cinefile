// Persistência única de avaliações do usuário.
// Formato no localStorage (chave 'cinefile_ratings'):
// { [id: string]: { rating: number (1..5), ts: number (epoch ms) } }

(function (global) {
  const STORAGE_KEY = "cinefile_ratings";

  function _read() {
    try {
      const raw = localStorage.getItem(STORAGE_KEY);
      return raw ? JSON.parse(raw) : {};
    } catch {
      return {};
    }
  }

  function _write(obj) {
    try {
      localStorage.setItem(STORAGE_KEY, JSON.stringify(obj));
    } catch {}
  }

  const Ratings = {
    // Retorna todo o dicionário { id: { rating, ts } }
    all() {
      return _read();
    },

    // Define/atualiza nota (1..5) para um id
    set(id, rating) {
      const r = Math.max(1, Math.min(5, Number(rating || 0)));
      const map = _read();
      map[String(id)] = { rating: r, ts: Date.now() };
      _write(map);
      return map[String(id)];
    },

    // Remove uma avaliação
    remove(id) {
      const map = _read();
      delete map[String(id)];
      _write(map);
    },

    // Pega a avaliação de um id (ou null)
    get(id) {
      const map = _read();
      return map[String(id)] || null;
    },

    // Lista ordenada por mais recente [{id, rating, ts}, ...]
    listSortedDesc() {
      return Object.entries(_read())
        .map(([id, v]) => ({ id, rating: Number(v.rating || 0), ts: Number(v.ts || 0) }))
        .sort((a, b) => b.ts - a.ts);
    }
  };

  // expõe no escopo global
  global.Ratings = Ratings;
})(window);

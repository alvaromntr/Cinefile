// js/ratings.js – persistência local
(function (global) {
  const STORAGE_KEY = "cinefile_ratings";
  function _read() { try { const raw = localStorage.getItem(STORAGE_KEY); return raw ? JSON.parse(raw) : {}; } catch { return {}; } }
  function _write(obj) { try { localStorage.setItem(STORAGE_KEY, JSON.stringify(obj)); } catch {} }

  const Ratings = {
    all() { return _read(); },
    set(id, rating) {
      const n = Number(rating); const safe = isNaN(n) ? 0 : n;
      const r = Math.max(1, Math.min(5, safe));
      const map = _read(); map[String(id)] = { rating: r, ts: Date.now() }; _write(map); return map[String(id)];
    },
    remove(id) { const map = _read(); delete map[String(id)]; _write(map); },
    get(id) { const map = _read(); const k = String(id); return Object.prototype.hasOwnProperty.call(map, k) ? map[k] : null; },
    listSortedDesc() {
      const arr = Object.entries(_read()).map(([id, v]) => {
        const rn = Number(v && v.rating); const tn = Number(v && v.ts);
        return { id, rating: isNaN(rn) ? 0 : rn, ts: isNaN(tn) ? 0 : tn };
      });
      arr.sort((a, b) => b.ts - a.ts);
      return arr;
    }
  };
  global.Ratings = Ratings;
})(window);

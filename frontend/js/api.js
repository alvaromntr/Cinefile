// Mock API with optional HTTP mode
// Accepts JSON payloads and returns booleans, keeping interface stable
(function(){
  const USERS_KEY = "cinefile_users";

  // Configuration (can be overridden later)
  let CONFIG = {
    mode: localStorage.getItem("cinefile_api_mode") || "http", // 'mock' | 'http'
    baseUrl: localStorage.getItem("cinefile_api_base") || "http://localhost:8080/",
    timeoutMs: 6000
  };

  if (window.API_CONFIG && typeof window.API_CONFIG === "object") {
    CONFIG = { ...CONFIG, ...window.API_CONFIG };
  }

  function configure(opts){
    CONFIG = { ...CONFIG, ...(opts || {}) };
  }

  // ---------- Utilities ----------
  function loadUsers(){
    try { return JSON.parse(localStorage.getItem(USERS_KEY) || "[]"); }
    catch { return []; }
  }

  function saveUsers(list){
    localStorage.setItem(USERS_KEY, JSON.stringify(list));
  }

  function delay(ms){ return new Promise(r => setTimeout(r, ms)); }

  async function httpPostBoolean(path, body){
    if (!CONFIG.baseUrl) return false;
    const url = CONFIG.baseUrl.replace(/\/$/, "") + path;
    const ctrl = ("AbortController" in window) ? new AbortController() : null;
    const t = ctrl ? setTimeout(() => ctrl.abort(), CONFIG.timeoutMs) : null;
    try {
      const res = await fetch(url, {
        method: "POST",
        headers: { "Content-Type": "application/json" },
        body: JSON.stringify(body || {}),
        signal: ctrl ? ctrl.signal : undefined,
        credentials: "include" // allow cookie-based auth if server uses it
      });
      const contentType = res.headers.get("content-type") || "";
      let data = null;
      if (contentType.includes("application/json")) {
        try { data = await res.json(); } catch { data = null; }
      } else {
        // Accept plain true/false body as text
        try {
          const txt = await res.text();
          if (txt.trim().toLowerCase() === "true") data = true;
          else if (txt.trim().toLowerCase() === "false") data = false;
        } catch { /* ignore */ }
      }
      // Normalize to boolean: accept { ok:true } or { success:true } or plain true
      const ok = (
        (data && typeof data.ok === "boolean" && data.ok === true) ||
        (data && typeof data.success === "boolean" && data.success === true) ||
        data === true || data === "true"
      );
      // Persist Basic token if provided by backend
      try {
        if (data && typeof data.auth === "string" && data.auth.startsWith("Basic ")) {
          localStorage.setItem("cinefile_auth_basic", data.auth);
        }
      } catch {}
      return !!ok;
    } catch (_){
      return false;
    } finally {
      if (t) clearTimeout(t);
    }
  }

  // ---------- Operations ----------
  async function register(payload){
    // payload: { login(username), email, senha }
    const { login, email, senha } = payload || {};
    // Backend espera { username, email, senha }
    return httpPostBoolean("/api/usuarios/cadastro", { username: login, email, senha });
  }

  async function login(payload){
    // payload: { login(username or email), senha }
    const { login, senha } = payload || {};
    return httpPostBoolean("/api/usuarios/login", { usernameOrEmail: login, senha });
  }

  window.API = { register, login, configure };
})();

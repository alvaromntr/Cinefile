// js/auth.js – API simples p/ outras páginas
window.AUTH = {
  isLogged() { return !!(window.API && API.Auth && API.Auth.has && API.Auth.has()); },
  logout() { if (window.API && API.Auth) API.Auth.clear(); }
};

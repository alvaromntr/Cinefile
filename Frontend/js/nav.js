// js/nav.js – header, toggler e UI por auth
document.addEventListener("DOMContentLoaded", () => {
  const toggle = document.getElementById("navToggle");
  const drawer = document.getElementById("navDrawer");

  if (toggle && drawer) {
    toggle.addEventListener("click", () => {
      const isOpen = drawer.hasAttribute("hidden") ? false : true;
      if (isOpen) drawer.setAttribute("hidden", "");
      else drawer.removeAttribute("hidden");
      toggle.setAttribute("aria-expanded", String(!isOpen));
    });
  }

  // UI auth
  applyAuthUI();
});

function applyAuthUI() {
  const logged = !!(window.API && API.Auth && API.Auth.has && API.Auth.has());

  document.querySelectorAll(".requires-auth").forEach(a => a.classList.toggle("is-hidden", !logged));
  document.querySelectorAll(".link-login, .link-register").forEach(a => a.classList.toggle("is-hidden", logged));

  const btnLogout = document.querySelector(".link-logout");
  if (btnLogout) {
    btnLogout.classList.toggle("is-hidden", !logged);
    btnLogout.addEventListener("click", (e) => {
      e.preventDefault();
      API.Auth.clear();
      location.href = "index.html";
    }, { once: true });
  }
}

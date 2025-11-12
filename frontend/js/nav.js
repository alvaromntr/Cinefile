// Controle do menu hambúrguer (abre/fecha o drawer), acessível e simples.

// aguarda o DOM carregar
document.addEventListener("DOMContentLoaded", () => {
// declara variável btn
  const btn   = document.getElementById("navToggle");
// declara variável drawer
  const drawer= document.getElementById("navDrawer");
// condição
  if (!btn || !drawer) return;

  // Abre/fecha o drawer
// define função setOpen
  function setOpen(open){
    btn.setAttribute("aria-expanded", open ? "true" : "false");
// atribui valor
    drawer.hidden = !open;
// atribui valor
    document.body.style.overflow = open ? "hidden" : ""; // trava scroll da página quando aberto
  }

  // Toggle no clique do hambúrguer
// adiciona ouvinte de evento
  btn.addEventListener("click", () => {
// declara variável open
    const open = btn.getAttribute("aria-expanded") !== "true";
    setOpen(open);
  });

  // Fecha com ESC
// adiciona ouvinte de evento
  document.addEventListener("keydown", (e) => {
// condição
    if (e.key === "Escape" && btn.getAttribute("aria-expanded") === "true") {
      setOpen(false);
      btn.focus();
    }
  });

  // Fecha quando clica em um link do drawer
// adiciona ouvinte de evento
  drawer.addEventListener("click", (e) => {
// declara variável a
    const a = e.target.closest("a");
// condição
    if (a) setOpen(false);
  });

  // Auto-fecha ao redimensionar para desktop
// adiciona ouvinte de evento
  window.addEventListener("resize", () => {
// condição
    if (window.innerWidth > 480 && btn.getAttribute("aria-expanded") === "true") {
      setOpen(false);
    }
  });
});

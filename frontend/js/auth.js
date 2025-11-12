// Unifica a detecção de login e atualiza a UI do cabeçalho/links protegidos

// aguarda o DOM carregar
document.addEventListener("DOMContentLoaded", () => {
  // ===== Helpers de estado =====

// define função isLogged
  function isLogged() {
// tenta executar bloco
    try {
      // Prioridades: APIs, flags novas/antigas e usuário
// condição
      if (window.LISTS?.isLogged && window.LISTS.isLogged()) return true;
// condição
      if (window.AUTH?._is && window.AUTH._is()) return true; // reuso interno
// declara variável flagNew
      const flagNew  = localStorage.getItem("cinefile_logged") === "true";
// declara variável flagOld
      const flagOld  = localStorage.getItem("cinefile_logged_in") === "true";
// declara variável hasUser
      const hasUser  = !!localStorage.getItem("cinefile_username");
// retorna
      return flagNew || flagOld || hasUser;
// captura erro
    } catch {
// retorna
      return false;
    }
  }

// define função setLogged
  function setLogged(value, username = null) {
    // usamos sempre a chave nova, mas mantemos compat com a antiga
// acessa localStorage
    localStorage.setItem("cinefile_logged", value ? "true" : "false");
// acessa localStorage
    localStorage.setItem("cinefile_logged_in", value ? "true" : "false");
// condição
    if (value && username) {
// acessa localStorage
      localStorage.setItem("cinefile_username", username);
    } else if (!value) {
// acessa localStorage
      localStorage.removeItem("cinefile_username");
    }
  }

// define função logoutAndGoHome
  function logoutAndGoHome() {
    // limpa todas as chaves relacionadas
// acessa localStorage
    localStorage.removeItem("cinefile_logged");
// acessa localStorage
    localStorage.removeItem("cinefile_logged_in");
// acessa localStorage
    localStorage.removeItem("cinefile_username");
    // (se quiser preservar listas/ratings, não remova aqui)
    updateUI();
// redireciona navegação
    window.location.href = "index.html";
  }

  // ===== Atualiza a UI conforme login =====
// define função updateUI
  function updateUI() {
// declara variável logged
    const logged = isLogged();

    // Itens só para logados
// seleciona elemento do DOM
    document.querySelectorAll(".requires-auth").forEach(el => {
// atribui valor
      el.style.display = logged ? "" : "none";
    });

    // Entrar (mostra só quando NÃO logado)
// seleciona elemento do DOM
    document.querySelectorAll(".link-login").forEach(el => {
// atribui valor
      el.style.display = logged ? "none" : "";
    });

    // Cadastrar (NOVO) — mostra só quando NÃO logado
// seleciona elemento do DOM
    document.querySelectorAll(".link-register").forEach(el => {
// atribui valor
      el.style.display = logged ? "none" : "";
    });

    // Sair (mostra só quando logado)
// seleciona elemento do DOM
    document.querySelectorAll(".link-logout").forEach(el => {
// atribui valor
      el.style.display = logged ? "" : "none";
      // evita múltiplos listeners
// atribui valor
      el.onclick = (e) => {
        e.preventDefault();
        logoutAndGoHome();
      };
    });
  }

  // ===== Protege ações que exigem conta =====

  // Usa delegação para funcionar com elementos criados depois
// adiciona ouvinte de evento
  document.body.addEventListener("click", (e) => {
// declara variável btn
    const btn = e.target.closest(".auth-action");
// condição
    if (!btn) return;
// condição
    if (!isLogged()) {
      e.preventDefault();
// declara variável back
      const back = encodeURIComponent(location.pathname + location.search);
// redireciona navegação
      window.location.href = `login.html?redirect=${back}`;
      // se quiser oferecer cadastro direto, poderia ser:
      // window.location.href = `register.html?redirect=${back}`;
    }
  });

  // ===== Expor API simples =====
  
// atribui valor
  window.AUTH = {
    isLogged,
    _is: isLogged,          // alias interno usado acima
    setLogged,              // pode ser usado no login.js
    logout: logoutAndGoHome
  };

  // Inicializa UI
  updateUI();
});

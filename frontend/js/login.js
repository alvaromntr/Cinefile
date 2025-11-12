// Login de teste //
// username: brucewayne
// senha:    gotham

document.addEventListener("DOMContentLoaded", () => {
  // declara variável form
  const form      = document.getElementById("loginForm");
  // declara variável inputUser
  const inputUser = document.getElementById("username");
  // declara variável inputPass
  const inputPass = document.getElementById("password");
  // declara variável errorMsg
  const errorMsg  = document.getElementById("loginError");

  // Esconde o erro enquanto digita de novo
  // itera coleção
  [inputUser, inputPass].forEach(el => {
    // adiciona ouvinte de evento
    el.addEventListener("input", () => { errorMsg.hidden = true; });
  });

  // adiciona ouvinte de evento
  form.addEventListener("submit", async (e) => {
    e.preventDefault();                   // evita recarregar a página

    // declara variável user
    const userRaw = inputUser.value.trim();
    // declara variável pass
    const pass    = inputPass.value.trim();

    // normaliza o usuário para comparação (case-insensitive)
    const user = userRaw.toLowerCase();

    // chama API simulada com JSON e recebe booleano
    let okResp = false;
    try {
      okResp = await (window.API && API.login({ login: userRaw, senha: pass }));
    } catch(_) {
      okResp = false;
    }

    // condição
    if (okResp) {
      // ---- sessão/gravação de login (flags novas e antigas) ----
      try {
        // acessa localStorage
        localStorage.setItem("cinefile_logged", "true");      // flag nova (principal)
        // acessa localStorage
        localStorage.setItem("cinefile_logged_in", "true");   // compat antigo
        // acessa localStorage
        localStorage.setItem("cinefile_username", userRaw);   // guarda username com o que o usuário digitou
      } catch {}

      // ---- aplica avaliação pendente (se o usuário clicou numa estrela deslogado) ----
      // tenta executar bloco
      try {
        // declara variável raw
        const raw = localStorage.getItem("cinefile_pending_rating"); // lê pendência
        // condição
        if (raw) {
          // declara variável pending
          const pending = JSON.parse(raw);                           // parse JSON
          // condição
          if (pending && pending.id && pending.rating != null) {
            // Se ratings.js está carregado, usa API oficial
            if (window.Ratings && typeof Ratings.set === "function") {
              Ratings.set(String(pending.id), Number(pending.rating));
            } else {
              // fallback simples (se ratings.js não estiver carregado)
              // declara variável KEY
              const KEY = "cinefile_ratings";                          // chave storage
              // declara variável map
              let map = {};                                            // inicia objeto
              // tenta executar bloco
              try { map = JSON.parse(localStorage.getItem(KEY) || "{}"); } catch {}
              // atribui valor
              map[String(pending.id)] = {
                rating: Math.max(1, Math.min(5, Number(pending.rating))),
                ts: Date.now()
              };
              // acessa localStorage
              try { localStorage.setItem(KEY, JSON.stringify(map)); } catch {}
            }
          }
          // acessa localStorage
          localStorage.removeItem("cinefile_pending_rating");          // limpa pendência
        }
      } catch { /* silencia erros de parse/storage */ }

      // ---- redireciona: volta para a rota que pediu login, se existir ----
      // declara variável params
      const params   = new URLSearchParams(location.search);           // lê querystring
      // declara variável redirect
      const redirect = params.get("redirect");                         // pega ?redirect=...

      // condição
      if (redirect) {
        // Evita abrir em novo host por segurança simples (mantém só path/query locais)
        // tenta executar bloco
        try {
          // declara variável url
          const url = new URL(decodeURIComponent(redirect), location.origin); // normaliza URL
          // só permite voltar para a mesma origem
          // condição
          if (url.origin === location.origin) {
            // redireciona navegação
            location.href = url.pathname + url.search + url.hash;      // volta p/ página de origem
            // retorna
            return;                                                    // encerra handler
          }
        // captura erro
        } catch(_) { /* se der erro no decode/URL, cai pro fallback */ }
      }

      // fallback: home
      // redireciona navegação
      location.href = "index.html";                                    // vai para a home
    } else {
      errorMsg.hidden = false;                                         // mostra erro
    }
  });
});

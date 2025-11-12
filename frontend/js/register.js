// quando ligarmos ao backend, só trocar o submit para uma requisição HTTP

(function(){
  const form = document.getElementById("registerForm");
  const $ = id => document.getElementById(id);
  const err = $("registerError");
  const ok  = $("registerOk");

  // Se já estiver logado, não precisa cadastrar
  if (localStorage.getItem("cinefile_logged_in") === "true") {
    location.href = "index.html";
    return;
  }

  form.addEventListener("submit", async (e) => {
    e.preventDefault();
    hide(err); hide(ok);

    const username = $("reg_username").value.trim();
    const email    = $("reg_email").value.trim().toLowerCase();
    const pass1    = $("reg_password").value;
    const pass2    = $("reg_password2").value;

    // validações básicas
    if (username.length < 3)  return show(err, "Usuário deve ter pelo menos 3 caracteres.");
    if (!email.includes("@")) return show(err, "Digite um e-mail válido.");
    if (pass1.length < 4)     return show(err, "Senha deve ter pelo menos 4 caracteres.");
    if (pass1 !== pass2)      return show(err, "As senhas não coincidem.");

    // Chama API simulada: envia JSON e recebe booleano
    try {
      const okResp = await (window.API && API.register({ login: username, email, senha: pass1 }));
      if (!okResp) {
        return show(err, "Não foi possível cadastrar. Verifique se usuário/e-mail já existem.");
      }
    } catch(_) {
      return show(err, "Erro ao comunicar com a API simulada.");
    }

    // faz login automático (sessão de demo)
    try {
      localStorage.setItem("cinefile_logged", "true");
      localStorage.setItem("cinefile_logged_in", "true");
      localStorage.setItem("cinefile_username", username);
    } catch {}

    show(ok, "Conta criada! Redirecionando...");
    // respeita redirect se houver
    const params = new URLSearchParams(location.search);
    const to = params.get("redirect") || "index.html";
    setTimeout(() => location.href = to, 600);
  });

  function show(el, msg){ el.textContent = msg; el.hidden = false; }
  function hide(el){ el.hidden = true; }
})();


/* js/register.js
   Cadastro real integrado ao Back-end:
   - POST /api/usuarios/cadastro
   - Login automático local (persiste Basic Auth via API.Auth.setBasic)
*/

(function () {
  const form = document.getElementById("registerForm");
  const $    = (id) => document.getElementById(id);
  const err  = $("registerError");
  const ok   = $("registerOk");

  // Se já estiver logado, não precisa cadastrar
  try {
    if (API?.Auth?.has && API.Auth.has()) {
      location.href = "index.html";
      return;
    }
  } catch {}

  if (!form) return;

  form.addEventListener("submit", async (e) => {
    e.preventDefault();
    hide(err); hide(ok);

    const username = $("reg_username")?.value?.trim() || "";
    const email    = ($("reg_email")?.value || "").trim().toLowerCase();
    const pass1    = $("reg_password")?.value || "";
    const pass2    = $("reg_password2")?.value || "";

    // Validações básicas
    if (username.length < 3)  return show(err, "Usuário deve ter pelo menos 3 caracteres.");
    if (!email.includes("@")) return show(err, "Digite um e-mail válido.");
    if (pass1.length < 4)     return show(err, "Senha deve ter pelo menos 4 caracteres.");
    if (pass1 !== pass2)      return show(err, "As senhas não coincidem.");

    const submitBtn = form.querySelector('[type="submit"]');
    if (submitBtn) submitBtn.disabled = true;

    try {
      // 1) Cadastro no Back-end
      const resp = await API.Usuarios.cadastro({ username, email, senha: pass1 });
      // resp: { ok:true, username, email, auth: "Basic ..." }

      // 2) Login local (não precisa chamar /login)
      API.Auth.setBasic(username, pass1);

      // 3) Mensagem de sucesso e redirecionamento
      show(ok, "Conta criada com sucesso. Redirecionando…");

      const params   = new URLSearchParams(location.search);
      const redirect = params.get("redirect");
      const pending  = localStorage.getItem("cinefile_pending_path");

      const target =
        pending
          ? (localStorage.removeItem("cinefile_pending_path"), pending)
          : redirect
            ? safeRedirect(redirect)
            : "index.html";

      setTimeout(() => location.href = target, 600);
    } catch (e) {
      console.error(e);
      const msg = extractReadableError(e) || "Não foi possível criar sua conta. Tente novamente.";
      show(err, msg);
    } finally {
      if (submitBtn) submitBtn.disabled = false;
    }
  });

  // Helpers UI
  function show(el, msg) { if (!el) return; el.textContent = msg; el.hidden = false; }
  function hide(el)      { if (!el) return; el.hidden = true; }

  // Sanitiza e limita redirects para a mesma origem
  function safeRedirect(value) {
    try {
      const url = new URL(decodeURIComponent(value), location.origin);
      if (url.origin === location.origin) {
        return url.pathname + url.search + url.hash;
      }
    } catch {}
    return "index.html";
  }

  // Extrai mensagem legível de erros comuns de API
  function extractReadableError(err) {
    if (!err) return "";
    const text = (err.body || err.message || "").toString().toLowerCase();

    if (text.includes("username")) return "Nome de usuário já existe.";
    if (text.includes("email"))    return "E-mail já cadastrado.";
    if (text.includes("409"))      return "Usuário ou e-mail já cadastrado.";
    if (text.includes("invalid") || text.includes("400")) return "Dados inválidos. Verifique os campos e tente novamente.";
    if (text.includes("401")) return "Não autorizado. Verifique as credenciais.";
    if (text.includes("500")) return "Erro no servidor. Tente novamente em instantes.";
    return "";
  }
})();

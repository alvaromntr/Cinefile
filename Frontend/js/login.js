// js/login.js – login real (POST /api/usuarios/login) + persist Basic
document.addEventListener("DOMContentLoaded", () => {
  const form      = document.getElementById("loginForm");
  const inputUser = document.getElementById("username");
  const inputPass = document.getElementById("password");
  const errorMsg  = document.getElementById("loginError");
  const submitBtn = form?.querySelector('[type="submit"]');

  if (API.Auth.has()) { window.location.href = "index.html"; return; }

  form?.addEventListener("submit", async (e) => {
    e.preventDefault();
    const userRaw = (inputUser?.value || "").trim();
    const passRaw = (inputPass?.value || "").trim();
    if (!userRaw || !passRaw) { if (errorMsg) errorMsg.textContent = "Informe usuário/e-mail e senha."; return; }

    try {
      submitBtn && (submitBtn.disabled = true);
      errorMsg && (errorMsg.textContent = "");

      await API.Usuarios.login(userRaw, passRaw);
      API.Auth.set(userRaw, passRaw);

      const redirect = new URLSearchParams(location.search).get("redirect");
      if (redirect) {
        try {
          const url = new URL(decodeURIComponent(redirect), location.origin);
          if (url.origin === location.origin) { location.href = url.pathname + url.search + url.hash; return; }
        } catch {}
      }
      location.href = "index.html";
    } catch (err) {
      console.error(err);
      if (errorMsg) errorMsg.textContent = "Falha ao autenticar. Verifique suas credenciais.";
      else alert("Falha ao autenticar.");
    } finally {
      submitBtn && (submitBtn.disabled = false);
    }
  });
});

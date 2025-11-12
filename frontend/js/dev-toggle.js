// Dev toggle to switch API mode (mock/http) and base URL
document.addEventListener("DOMContentLoaded", () => {
  const where = document.querySelector(".nav__right") || document.querySelector(".nav__container") || document.body;
  if (!where) return;

  // Only render if API is available
  if (!window.API || typeof API.configure !== "function") return;

  const wrap = document.createElement("div");
  wrap.style.display = "flex";
  wrap.style.alignItems = "center";
  wrap.style.gap = "6px";
  wrap.style.marginLeft = "8px";
  wrap.style.fontSize = "12px";
  wrap.style.opacity = "0.8";

  const label = document.createElement("span");
  label.textContent = "API:";

  const sel = document.createElement("select");
  sel.ariaLabel = "Modo da API";
  sel.style.fontSize = "12px";
  sel.style.padding = "2px 4px";
  const optHttp = new Option("http", "http");
  sel.append(optHttp);

  const base = document.createElement("input");
  base.type = "text";
  base.placeholder = "http://localhost:8080";
  base.ariaLabel = "Base URL da API";
  base.style.fontSize = "12px";
  base.style.padding = "2px 4px";
  base.style.width = "160px";

  const savedMode = "http"; // força HTTP
  const savedBase = localStorage.getItem("cinefile_api_base") || "http://localhost:8080/";
  sel.value = savedMode;
  try { localStorage.setItem("cinefile_api_mode", "http"); } catch {}
  base.value = savedBase;
  base.style.display = (savedMode === "http") ? "inline-block" : "none";

  try { API.configure({ mode: savedMode, baseUrl: savedBase }); } catch {}

  sel.addEventListener("change", () => {
    const mode = sel.value;
    localStorage.setItem("cinefile_api_mode", mode);
    base.style.display = mode === "http" ? "inline-block" : "none";
    try { API.configure({ mode }); } catch {}
  });

  function applyBase(){
    const url = (base.value || "http://localhost:8080").trim();
    localStorage.setItem("cinefile_api_base", url);
    try { API.configure({ baseUrl: url }); } catch {}
  }

  base.addEventListener("blur", applyBase);
  base.addEventListener("keydown", (e) => { if (e.key === "Enter") { e.preventDefault(); applyBase(); } });

  wrap.append(label, sel, base);
  where.appendChild(wrap);
});

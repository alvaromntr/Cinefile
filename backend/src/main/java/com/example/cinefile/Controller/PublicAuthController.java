package com.example.cinefile.Controller;

import com.example.cinefile.Domain.Usuario.UserRole;
import com.example.cinefile.Domain.Usuario.Usuario;
import com.example.cinefile.Service.UsuarioService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@CrossOrigin(origins = {"http://127.0.0.1:5500", "http://localhost:5500", "http://localhost:3000", "http://127.0.0.1:3000"}, allowCredentials = "true")
@RestController
@RequestMapping("/auth")
public class PublicAuthController {

    private final AuthenticationManager authManager;
    private final UsuarioService usuarioService;
    private final BCryptPasswordEncoder passwordEncoder;

    public PublicAuthController(AuthenticationManager authManager,
                                UsuarioService usuarioService,
                                BCryptPasswordEncoder passwordEncoder) {
        this.authManager = authManager;
        this.usuarioService = usuarioService;
        this.passwordEncoder = passwordEncoder;
    }

    // POST /auth/login  { login, senha } -> { ok: boolean }
    @PostMapping("/login")
    public ResponseEntity<?> simpleLogin(@RequestBody Map<String, String> body) {
        String login = safe(body.get("login"));
        String senha = body.getOrDefault("senha", "");

        if (login.isBlank() || senha.isBlank()) {
            return ResponseEntity.ok(Map.of("ok", false));
        }

        // Accept username or email
        String principal = login;
        var byEmail = usuarioService.findByEmail(login);
        if (byEmail != null && byEmail.getUsername() != null) {
            principal = byEmail.getUsername();
        }

        try {
            Authentication auth = authManager.authenticate(
                    new UsernamePasswordAuthenticationToken(principal, senha)
            );
            if (auth.isAuthenticated()) {
                return ResponseEntity.ok(Map.of("ok", true));
            }
            return ResponseEntity.ok(Map.of("ok", false));
        } catch (Exception e) {
            return ResponseEntity.ok(Map.of("ok", false));
        }
    }

    // POST /auth/register  { login, email, senha } -> { ok: boolean }
    @PostMapping("/register")
    public ResponseEntity<?> simpleRegister(@RequestBody Map<String, String> body) {
        String login = safe(body.get("login")).toLowerCase();
        String email = safe(body.get("email")).toLowerCase();
        String senha = body.getOrDefault("senha", "");

        if (login.length() < 3 || !email.contains("@") || senha.length() < 4) {
            return ResponseEntity.ok(Map.of("ok", false));
        }
        if (usuarioService.existsByUsername(login) || usuarioService.existsByEmail(email)) {
            return ResponseEntity.ok(Map.of("ok", false));
        }

        Usuario novo = new Usuario(login, email, passwordEncoder.encode(senha), UserRole.USER);
        usuarioService.salvar(novo);
        return ResponseEntity.ok(Map.of("ok", true));
    }

    private static String safe(String s) { return s == null ? "" : s.trim(); }
}


package com.example.cinefile.Controller;

import com.example.cinefile.DTO.LoginRequestDTO;
import com.example.cinefile.DTO.LoginResponseDTO;
import com.example.cinefile.Domain.Usuario.Usuario;
import com.example.cinefile.Domain.Usuario.UserRole;
import com.example.cinefile.Service.UsuarioService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.Base64;
import java.util.Map;

@CrossOrigin(
        origins = {"http://127.0.0.1:5500", "http://localhost:5500"},
        allowCredentials = "true"
)
@RestController
@RequestMapping("/api/usuarios")
public class AuthController {

    private final AuthenticationManager authManager;
    private final UsuarioService usuarioService;
    private final BCryptPasswordEncoder passwordEncoder;

    public AuthController(AuthenticationManager authManager,
                          UsuarioService usuarioService,
                          BCryptPasswordEncoder passwordEncoder) {
        this.authManager = authManager;
        this.usuarioService = usuarioService;
        this.passwordEncoder = passwordEncoder;
    }

    // ---------- LOGIN ----------
    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody @Valid LoginRequestDTO dto) {
        String principal = dto.usernameOrEmail().trim();

        // Se for e-mail, tenta buscar o username correspondente
        Usuario byEmail = usuarioService.findByEmail(principal);
        if (byEmail != null && byEmail.getUsername() != null && !byEmail.getUsername().isBlank()) {
            principal = byEmail.getUsername();
        }

        try {
            Authentication auth = authManager.authenticate(
                    new UsernamePasswordAuthenticationToken(principal, dto.senha())
            );

            Usuario usuarioAutenticado = usuarioService.findByUsername(principal);
            if (usuarioAutenticado == null) {
                return ResponseEntity.status(404).body("Usuário não encontrado.");
            }

            // Monta resposta para o front
            String basicAuth = "Basic " + Base64.getEncoder()
                    .encodeToString((usuarioAutenticado.getUsername() + ":" + dto.senha()).getBytes());

            return ResponseEntity.ok(new LoginResponseDTO(
                    true,
                    usuarioAutenticado.getUsername(),
                    usuarioAutenticado.getEmail(),
                    basicAuth
            ));
        } catch (BadCredentialsException ex) {
            return ResponseEntity.status(401).body("Credenciais inválidas.");
        }
    }

    // ---------- CADASTRO ----------
    @PostMapping("/cadastro")
    public ResponseEntity<?> cadastrar(@RequestBody Map<String, String> body) {
        String username = body.getOrDefault("username", "").trim();
        String email = body.getOrDefault("email", "").trim();
        String senha = body.getOrDefault("senha", "");

        if (username.length() < 3 || !email.contains("@") || senha.length() < 4) {
            return ResponseEntity.badRequest().body("invalid");
        }

        if (usuarioService.existsByUsername(username)) {
            return ResponseEntity.status(409).body("username");
        }
        if (usuarioService.existsByEmail(email)) {
            return ResponseEntity.status(409).body("email");
        }

        Usuario novo = new Usuario(username, email, passwordEncoder.encode(senha), UserRole.USER);
        usuarioService.salvar(novo);

        // Monta Basic Auth para login automático
        String basic = "Basic " + Base64.getEncoder()
                .encodeToString((username + ":" + senha).getBytes());

        return ResponseEntity.ok(Map.of(
                "ok", true,
                "username", username,
                "email", email,
                "auth", basic
        ));
    }
}

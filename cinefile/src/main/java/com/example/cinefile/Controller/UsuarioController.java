package com.example.cinefile.Controller;

import com.example.cinefile.DTO.LoginRequest;
import com.example.cinefile.DTO.UsuarioDTO;
import com.example.cinefile.Domain.Usuario.Usuario;
import com.example.cinefile.Service.UsuarioService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/api/usuarios")
public class UsuarioController {

    private final UsuarioService usuarioService;
    private final BCryptPasswordEncoder passwordEncoder;

    public UsuarioController(UsuarioService usuarioService, BCryptPasswordEncoder passwordEncoder) {
        this.usuarioService = usuarioService;
        this.passwordEncoder = passwordEncoder;
    }

    @PostMapping("/cadastro") //cria o usuario
    public ResponseEntity<String> cadastrarUsuario(@Valid @RequestBody UsuarioDTO usuarioDTO) {
        if (usuarioService.existsByUsername(usuarioDTO.getUsername())) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body("Usuário já existe");
        }

        String senhaCriptografada = passwordEncoder.encode(usuarioDTO.getSenha());

        Usuario usuario = new Usuario(
                usuarioDTO.getUsername(),
                usuarioDTO.getEmail(),
                senhaCriptografada
        );

        usuarioService.salvar(usuario);

        return ResponseEntity.status(HttpStatus.CREATED).body("Usuário cadastrado com sucesso");
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginRequest req) {
        // procurar pelo username (ou troque para e-mail se preferir)
        Usuario usuario = usuarioService.findByUsername(req.username());
        if (usuario == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body("Usuário não encontrado");
        }

        BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
        boolean ok = passwordEncoder.matches(req.senha(), usuario.getSenha_hash());
        if (!ok) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body("Senha inválida");
        }

        return ResponseEntity.ok().body(
                new UsuarioDTO() {{
                    setUsername(usuario.getUsername());
                    setEmail(usuario.getEmail());
                    // NUNCA devolva hash/senha
                }}
        );
    }
    @DeleteMapping("/{id}") //Apaga o usuario
    public ResponseEntity<String> deletarUsuario(@PathVariable UUID id) {
        Usuario usuario = usuarioService.findById(id);
        if (usuario == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Usuário não encontrado");
        }

        usuarioService.deletar(usuario);
        return ResponseEntity.ok("Usuário deletado com sucesso");
    }
    @PutMapping("/{id}")
    public ResponseEntity<String> atualizarUsuario(@PathVariable UUID id, @Valid @RequestBody UsuarioDTO usuarioDTO) {
        Usuario usuarioExistente = usuarioService.findById(id);
        if (usuarioExistente == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Usuário não encontrado");
        }


        usuarioExistente.setUsername(usuarioDTO.getUsername());
        usuarioExistente.setEmail(usuarioDTO.getEmail());


        if (usuarioDTO.getSenha() != null && !usuarioDTO.getSenha().isBlank()) {
            String senhaCriptografada = passwordEncoder.encode(usuarioDTO.getSenha());
            usuarioExistente.setSenha_hash(senhaCriptografada);
        }

        usuarioService.salvar(usuarioExistente);

        return ResponseEntity.ok("Usuário atualizado com sucesso");
    }


}

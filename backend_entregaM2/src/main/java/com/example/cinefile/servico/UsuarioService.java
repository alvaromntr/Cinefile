package com.example.cinefile.servico;

import com.example.cinefile.modelo.entidades.Usuario;
import com.example.cinefile.repositorio.UsuarioRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class UsuarioService {

    private final UsuarioRepository usuarioRepository;
    private final BCryptPasswordEncoder codificadorSenha;

    public Usuario autenticar(String usernameOuEmail, String senha) {
        return usuarioRepository.findByUsername(usernameOuEmail)
                .or(() -> usuarioRepository.findByEmail(usernameOuEmail))
                .filter(usuario -> codificadorSenha.matches(senha, usuario.getSenhaHash()))
                .orElse(null);
    }

    public Usuario salvar(Usuario usuario) {
        return usuarioRepository.save(usuario);
    }

    public Usuario buscarPorUsername(String username) {
        return usuarioRepository.findByUsername(username).orElse(null);
    }

    public Usuario buscarPorEmail(String email) {
        return usuarioRepository.findByEmail(email).orElse(null);
    }

    public boolean existePorUsername(String username) {
        return usuarioRepository.existsByUsername(username);
    }

    public boolean existePorEmail(String email) {
        return usuarioRepository.existsByEmail(email);
    }

    public Usuario buscarPorId(UUID id) {
        return usuarioRepository.findById(id).orElse(null);
    }
}
package com.example.cinefile.Service;

import com.example.cinefile.Domain.Usuario.Usuario;
import com.example.cinefile.Domain.Usuario.UsuarioRepository;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class UsuarioServiceImpl implements UsuarioService {

    private final UsuarioRepository usuarioRepository;

    public UsuarioServiceImpl(UsuarioRepository usuarioRepository) {
        this.usuarioRepository = usuarioRepository;
    }

    @Override
    public void salvar(Usuario usuario) {
        usuarioRepository.save(usuario);
    }

    @Override
    public Usuario findByUsername(String username) {
        return usuarioRepository.findByUsername(username).orElse(null);
    }

    @Override
    public boolean existsByUsername(String username) {
        return usuarioRepository.existsByUsername(username);
    }

    // Implementação dos novos métodos
    @Override
    public Usuario findById(UUID id) {
        return usuarioRepository.findById(id).orElse(null);
    }

    @Override
    public void deletar(Usuario usuario) {
        usuarioRepository.delete(usuario);
    }
}

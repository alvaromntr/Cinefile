package com.example.cinefile.Service;

import com.example.cinefile.Domain.Usuario.Usuario;

import java.util.UUID;

public interface UsuarioService {

    void salvar(Usuario usuario);

    Usuario findByUsername(String username);

    boolean existsByUsername(String username);

    // Novos métodos
    Usuario findById(UUID id);
    void deletar(Usuario usuario);
}

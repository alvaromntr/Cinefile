package com.example.cinefile.Domain.Usuario;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface UsuarioRepository extends JpaRepository<Usuario, UUID> {

    // Buscar usuário pelo username (opcional, útil para login)
    Optional<Usuario> findByUsername(String username);

    // Buscar usuário pelo email
    Usuario findByEmail(String email);

    // Verificar se email já está em uso (útil para validação no cadastro)
    boolean existsByEmail(String email);

    // Verificar se username já existe
    boolean existsByUsername(String username);
}

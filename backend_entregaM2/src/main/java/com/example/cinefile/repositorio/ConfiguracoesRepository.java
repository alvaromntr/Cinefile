package com.example.cinefile.repositorio;

import com.example.cinefile.modelo.entidades.Configuracoes;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface ConfiguracoesRepository extends JpaRepository<Configuracoes, Long> {
    Optional<Configuracoes> findByUsuarioId(UUID usuarioId);
}
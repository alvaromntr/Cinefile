package com.example.cinefile.Domain.Configuracoes;

import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;
import java.util.UUID;

public interface ConfiguracoesRepository extends JpaRepository<Configuracoes, Long> {
    Optional<Configuracoes> findByUsuario_Id(UUID usuarioId);
}
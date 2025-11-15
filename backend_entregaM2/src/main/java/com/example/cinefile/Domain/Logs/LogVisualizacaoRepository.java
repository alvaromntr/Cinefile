package com.example.cinefile.Domain.Logs;

import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;
import java.util.UUID;

public interface LogVisualizacaoRepository extends JpaRepository<LogVisualizacao, UUID> {
    List<LogVisualizacao> findByUsuarioId(UUID usuarioId);
}

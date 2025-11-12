package com.example.cinefile.DTO;

import com.example.cinefile.Domain.Logs.LogVisualizacao;
import java.util.UUID;

public record LogVisualizacaoResponseDTO(
        UUID id,
        UUID usuarioId,
        Long obraId,
        String tituloObra
) {
    public LogVisualizacaoResponseDTO(LogVisualizacao log) {
        this(
                log.getId(),
                log.getUsuario().getId(),
                log.getObra().getObraid(),
                log.getObra().getTitulo()
        );
    }
}

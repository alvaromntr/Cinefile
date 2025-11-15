package com.example.cinefile.modelo.dtos;

import java.util.UUID;

public record RespostaLogVisualizacao(
        UUID id,
        UUID usuarioId,
        Long obraId,
        String tituloObra
) {}
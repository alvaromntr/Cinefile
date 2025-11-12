package com.example.cinefile.DTO;

import java.util.UUID;

public record AvaliacaoDTO(
        UUID avaliacaoid,
        int nota,
        Long obraId,
        UUID usuarioId
) {}

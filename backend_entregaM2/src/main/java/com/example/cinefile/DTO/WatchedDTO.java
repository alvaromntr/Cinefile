package com.example.cinefile.DTO;

import java.util.UUID;

public record WatchedDTO(
        Long id,
        Long obraId,
        UUID usuarioId
) {}

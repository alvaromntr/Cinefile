package com.example.cinefile.AvaliacaoDomain;

import jakarta.validation.constraints.*;

import java.util.UUID;

public record RequestAvaliacao(
        @NotNull UUID obraId,
        @NotNull UUID usuarioId,
        @Min(1) @Max(5) Integer nota,
        String comentario
) {}

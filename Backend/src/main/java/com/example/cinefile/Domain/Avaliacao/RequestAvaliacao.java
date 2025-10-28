package com.example.cinefile.Domain.Avaliacao;

import jakarta.validation.constraints.*;

import java.util.UUID;

public record RequestAvaliacao(
        @NotNull Long obraId,
        UUID temporadaId, // 🔥 Novo campo opcional
        @NotNull @Min(1) @Max(5) Integer nota,
        String comentario
) {}

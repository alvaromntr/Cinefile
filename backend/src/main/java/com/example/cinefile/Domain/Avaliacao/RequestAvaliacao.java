package com.example.cinefile.Domain.Avaliacao;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;

public record RequestAvaliacao(
        @NotNull Long obraId,
        @Min(1) @Max(5) Integer nota,
        String comentario,
        Long temporadaId
) {}

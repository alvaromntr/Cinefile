package com.example.cinefile.modelo.dtos;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import java.util.UUID;

public record RequisicaoAvaliacao(
        @NotNull Long obraId,
        UUID temporadaId,
        @NotNull @Min(1) @Max(5) Integer nota,
        String comentario
) {}
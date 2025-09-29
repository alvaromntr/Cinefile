package com.example.cinefile.TemporadaDomain;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.util.UUID;

public record RequestTemporada(
        @NotNull
        Integer numero,
        @NotNull
        Integer quantidadeEpisodios,
        @NotNull
        String descricao,
        @Size(max = 500)
        UUID obraId
) {}

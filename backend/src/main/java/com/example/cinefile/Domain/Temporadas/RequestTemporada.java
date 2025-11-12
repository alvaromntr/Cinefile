package com.example.cinefile.Domain.Temporadas;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public record RequestTemporada(
        @NotNull
        Integer numero,
        @NotNull
        Integer quantidadeEpisodios,
        @NotNull
        String descricao,
        @Size(max = 500)
        Long obraId
) {}

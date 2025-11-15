package com.example.cinefile.modelo.dtos;

import jakarta.validation.constraints.NotNull;
import java.util.UUID;

public record RespostaTemporada(
        UUID id,
        @NotNull Integer numero,
        @NotNull Integer quantidadeEpisodios,
        String descricao
) {}
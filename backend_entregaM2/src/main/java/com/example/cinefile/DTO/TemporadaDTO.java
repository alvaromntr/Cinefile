package com.example.cinefile.DTO;

import com.example.cinefile.Domain.Temporadas.Temporada;
import jakarta.validation.constraints.NotNull;
import java.util.UUID;

public record TemporadaDTO(
        UUID id,
        @NotNull Integer numero,
        @NotNull Integer quantidadeEpisodios,
        String descricao
) {
    public TemporadaDTO(Temporada temporada) {
        this(temporada.getId(), temporada.getNumero(), temporada.getQuantidadeEpisodios(), temporada.getDescricao());
    }
}
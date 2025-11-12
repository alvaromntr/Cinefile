package com.example.cinefile.Domain.Obra;

import jakarta.validation.constraints.NotBlank;

public record RequestObra(
        Long tmdbId,                     // <-- novo campo
        @NotBlank String titulo,
        String descricao,
        ObraTipo tipo,
        Integer anolancamento,
        String poster_url,
        Integer duracao
) {}

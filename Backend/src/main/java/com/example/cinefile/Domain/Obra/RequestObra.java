package com.example.cinefile.Domain.Obra;

import jakarta.validation.constraints.NotBlank;



public record RequestObra(
        @NotBlank String titulo,
        String descricao,
        ObraTipo tipo, // filme ou serie
        Integer anolancamento,
        String poster_url,
        Integer duracao
) {}

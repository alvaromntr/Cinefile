package com.example.cinefile.Domain.Obras;

import jakarta.validation.constraints.NotBlank;

public record RequestObra(
        Long obra_id,
        @NotBlank String titulo,
        String descricao,
        String tipo, // filme ou serie
        Integer ano_lancamento,
        String poster_url,
        Integer duracao
) {}

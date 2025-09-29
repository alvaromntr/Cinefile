package com.example.cinefile.ObraDomain;

import jakarta.validation.constraints.NotBlank;

import java.util.UUID;

public record RequestObra(
        UUID obra_id,
        @NotBlank String titulo,
        String descricao,
        String tipo, // filme ou serie
        Integer ano_lancamento,
        String poster_url,
        Integer duracao
) {}

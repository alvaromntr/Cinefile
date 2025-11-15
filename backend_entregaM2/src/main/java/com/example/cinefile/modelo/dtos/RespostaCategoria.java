package com.example.cinefile.modelo.dtos;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record RespostaCategoria(
        Long id,
        @NotBlank(message = "O nome da categoria é obrigatório")
        @Size(max = 50)
        String nome
) {}
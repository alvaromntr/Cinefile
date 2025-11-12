package com.example.cinefile.DTO;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record CategoriaDTO(
        Long id,
        @NotBlank(message = "O nome da categoria é obrigatório")
        @Size(max = 50)
        String nome
) {

}

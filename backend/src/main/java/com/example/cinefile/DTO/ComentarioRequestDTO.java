package com.example.cinefile.DTO;

import jakarta.validation.constraints.NotBlank;

public record ComentarioRequestDTO(
        @NotBlank(message = "O texto do comentário não pode estar vazio.")
        String texto
) {}
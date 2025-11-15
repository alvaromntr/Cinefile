package com.example.cinefile.modelo.dtos;

import jakarta.validation.constraints.NotBlank;

public record RequisicaoComentario(
        @NotBlank(message = "O texto do comentário não pode estar vazio.")
        String texto
) {}
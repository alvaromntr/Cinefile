package com.example.cinefile.modelo.dtos;

import jakarta.validation.constraints.NotBlank;

public record RequisicaoLogin(
        @NotBlank(message = "Username ou email é obrigatório")
        String usernameOuEmail,

        @NotBlank(message = "Senha é obrigatória")
        String senha
) {}
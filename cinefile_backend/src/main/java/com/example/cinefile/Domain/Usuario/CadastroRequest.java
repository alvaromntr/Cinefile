package com.example.cinefile.Domain.Usuario;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;


public record CadastroRequest(
        @NotBlank String username,
        @Email String email,
        @NotBlank String password,
        String fotoPerfil // opcional
) {}
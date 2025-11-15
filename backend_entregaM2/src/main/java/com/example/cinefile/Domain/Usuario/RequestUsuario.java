package com.example.cinefile.Domain.Usuario;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

import java.util.UUID;

public record RequestUsuario(

        UUID id,
        @NotBlank
        String username,
        @Email
        String email,

        String senha_hash,

        String foto_usuario) {
}

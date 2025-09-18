package com.example.cinefile.Domain.Usuario;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record RequestUsuario(

        Long id,
        @NotBlank
        String username,
        @NotNull @Email
        String email,
        @NotNull
        String senha_hash,

        String foto_usuario) {
}

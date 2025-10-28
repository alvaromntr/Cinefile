
package com.example.cinefile.DTO;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

public record UsuarioCadastroDTO(
        @NotBlank String username,
        @Email String email,
        @NotBlank String senha
) {}
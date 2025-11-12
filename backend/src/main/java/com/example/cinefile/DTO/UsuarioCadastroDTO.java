package com.example.cinefile.DTO;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record UsuarioCadastroDTO(
        @NotBlank @Size(min = 3) String username,
        @Email String email,
        @NotBlank @Size(min = 6) String password
) {}

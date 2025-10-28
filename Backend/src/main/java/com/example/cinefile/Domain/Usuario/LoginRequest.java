package com.example.cinefile.Domain.Usuario;

import jakarta.validation.constraints.NotBlank;


public record LoginRequest(
        @NotBlank String username,
        @NotBlank String password
) {}
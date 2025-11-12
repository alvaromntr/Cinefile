package com.example.cinefile.DTO;

import java.util.UUID;

public record UsuarioResponseDTO(
        UUID id,
        String username,
        String email
) {}

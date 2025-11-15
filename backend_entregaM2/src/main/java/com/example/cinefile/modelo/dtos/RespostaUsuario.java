package com.example.cinefile.modelo.dtos;

import java.util.UUID;

public record RespostaUsuario(
        UUID id,
        String username,
        String email
) {}
package com.example.cinefile.DTO;

public record LoginResponseDTO(
        boolean success,
        String username,
        String email,
        String auth
) { }

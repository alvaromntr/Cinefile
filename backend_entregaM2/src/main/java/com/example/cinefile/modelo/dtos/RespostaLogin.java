package com.example.cinefile.modelo.dtos;

public record RespostaLogin(
        boolean sucesso,
        String username,
        String email,
        String tokenAutenticacao
) {}
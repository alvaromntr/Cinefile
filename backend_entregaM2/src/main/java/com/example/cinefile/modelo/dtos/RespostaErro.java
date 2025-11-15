package com.example.cinefile.modelo.dtos;

public record RespostaErro(
        String codigo,
        String mensagem,
        String campo
) {}
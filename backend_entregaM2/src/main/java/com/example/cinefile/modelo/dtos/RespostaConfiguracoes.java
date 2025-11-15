package com.example.cinefile.modelo.dtos;

public record RespostaConfiguracoes(
        Boolean receberNotificacoesEmail,
        String temaInterface,
        String idiomaPreferido
) {}
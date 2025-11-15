package com.example.cinefile.modelo.dtos;

import com.example.cinefile.modelo.entidades.Avaliacao;

import java.time.LocalDateTime;
import java.util.UUID;

public record RespostaAvaliacao(
        UUID id,
        Integer nota,
        String comentario,
        LocalDateTime dataCriacao,
        String usuario,
        Long obra,
        UUID temporada
) {
    public RespostaAvaliacao(Avaliacao avaliacao) {
        this(
                avaliacao.getId(),
                avaliacao.getNota(),
                avaliacao.getComentario(),
                avaliacao.getDataCriacao(),
                avaliacao.getUsuario() != null ? avaliacao.getUsuario().getUsername() : null,
                avaliacao.getObra() != null ? avaliacao.getObra().getId() : null,
                avaliacao.getTemporada() != null ? avaliacao.getTemporada().getId() : null
        );
    }
}
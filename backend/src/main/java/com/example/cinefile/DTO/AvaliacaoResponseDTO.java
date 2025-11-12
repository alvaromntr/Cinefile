package com.example.cinefile.DTO;

import com.example.cinefile.Domain.Avaliacao.Avaliacao;

import java.time.LocalDateTime;
import java.util.UUID;

public record AvaliacaoResponseDTO(
        UUID id,
        Integer nota,
        String comentario,
        LocalDateTime dataCriacao,
        String usernameUsuario,
        Long obraId
) {
    public AvaliacaoResponseDTO(Avaliacao avaliacao) {
        this(
                avaliacao.getId(),
                avaliacao.getNota(),
                avaliacao.getComentario(),
                avaliacao.getDataCriacao(),
                avaliacao.getUsuario().getUsername(),
                avaliacao.getObra().getObraid()
        );
    }
}
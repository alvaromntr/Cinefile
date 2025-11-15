package com.example.cinefile.modelo.dtos;

import com.example.cinefile.modelo.entidades.Comentario;
import java.time.LocalDateTime;

public record RespostaComentario(
        Long id,
        String texto,
        LocalDateTime dataComentario,
        String username
) {
    public RespostaComentario(Comentario comentario) {
        this(
                comentario.getId(),
                comentario.getTexto(),
                comentario.getDataComentario(),
                comentario.getUsuario() != null ? comentario.getUsuario().getUsername() : "Usuário Anônimo"
        );
    }
}
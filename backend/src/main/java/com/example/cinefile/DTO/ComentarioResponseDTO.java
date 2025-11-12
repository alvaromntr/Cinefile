package com.example.cinefile.DTO;

import com.example.cinefile.Domain.Comentario.Comentario;
import java.time.LocalDateTime;

public record ComentarioResponseDTO(
        Long id,
        String texto,
        LocalDateTime dataComentario,
        String username // Para mostrar quem comentou
) {
    public ComentarioResponseDTO(Comentario comentario) {
        this(
                comentario.getComentarioid(),
                comentario.getTexto(),
                comentario.getDatacomentario(),
                comentario.getUsuario().getUsername()
        );
    }
}

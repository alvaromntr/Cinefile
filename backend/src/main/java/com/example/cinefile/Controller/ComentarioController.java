package com.example.cinefile.Controller;

import com.example.cinefile.Domain.Usuario.Usuario;
import com.example.cinefile.DTO.ComentarioRequestDTO;
import com.example.cinefile.DTO.ComentarioResponseDTO;
import com.example.cinefile.Service.ComentarioService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@RestController
@RequestMapping("/api")
public class ComentarioController {

    @Autowired private ComentarioService comentarioService;

    @GetMapping("/obras/{obraId}/comentarios")
    public ResponseEntity<List<ComentarioResponseDTO>> listarComentarios(@PathVariable Long obraid) {
        return ResponseEntity.ok(comentarioService.listarComentariosPorObra(obraid));
    }

    @PostMapping("/obras/{obraId}/comentarios")
    public ResponseEntity<ComentarioResponseDTO> criarComentario(
            @PathVariable Long obraid,
            @Valid @RequestBody ComentarioRequestDTO dto,
            @AuthenticationPrincipal Usuario usuarioLogado
    ) {
        ComentarioResponseDTO novoComentario = comentarioService.criarComentario(obraid, dto, usuarioLogado);
        return new ResponseEntity<>(novoComentario, HttpStatus.CREATED);
    }

    @DeleteMapping("/comentarios/{comentarioId}")
    public ResponseEntity<Void> deletarComentario(
            @PathVariable Long comentarioid,
            @AuthenticationPrincipal Usuario usuarioLogado
    ) {
        comentarioService.deletarComentario(comentarioid, usuarioLogado);
        return ResponseEntity.noContent().build();
    }
}
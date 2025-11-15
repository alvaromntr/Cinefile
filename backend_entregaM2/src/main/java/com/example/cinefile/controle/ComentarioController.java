package com.example.cinefile.controle;

import com.example.cinefile.modelo.dtos.RequisicaoComentario;
import com.example.cinefile.modelo.dtos.RespostaComentario;
import com.example.cinefile.modelo.entidades.Usuario;
import com.example.cinefile.servico.ComentarioService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class ComentarioController {

    private final ComentarioService comentarioService;

    @GetMapping("/obras/{obraId}/comentarios")
    public ResponseEntity<List<RespostaComentario>> listarComentarios(@PathVariable Long obraId) {
        return ResponseEntity.ok(comentarioService.listarComentariosPorObra(obraId));
    }

    @PostMapping("/obras/{obraId}/comentarios")
    public ResponseEntity<RespostaComentario> criarComentario(
            @PathVariable Long obraId,
            @Valid @RequestBody RequisicaoComentario dados,
            @AuthenticationPrincipal Usuario usuarioLogado) {

        RespostaComentario novoComentario = comentarioService.criarComentario(obraId, dados, usuarioLogado);
        return new ResponseEntity<>(novoComentario, HttpStatus.CREATED);
    }

    @DeleteMapping("/comentarios/{comentarioId}")
    public ResponseEntity<Void> deletarComentario(
            @PathVariable Long comentarioId,
            @AuthenticationPrincipal Usuario usuarioLogado) {

        comentarioService.deletarComentario(comentarioId, usuarioLogado);
        return ResponseEntity.noContent().build();
    }
}
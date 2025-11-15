package com.example.cinefile.controle;

import com.example.cinefile.modelo.dtos.RequisicaoAvaliacao;
import com.example.cinefile.modelo.dtos.RespostaAvaliacao;
import com.example.cinefile.modelo.entidades.Usuario;
import com.example.cinefile.servico.AvaliacaoService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/api/avaliacoes")
@RequiredArgsConstructor
public class AvaliacaoController {

    private final AvaliacaoService avaliacaoService;

    @PostMapping
    public ResponseEntity<RespostaAvaliacao> criarAvaliacao(
            @RequestBody @Valid RequisicaoAvaliacao dados,
            @AuthenticationPrincipal Usuario usuarioLogado) {

        if (usuarioLogado == null) {
            return ResponseEntity.status(401).build();
        }

        RespostaAvaliacao novaAvaliacao = avaliacaoService.criarAvaliacao(dados, usuarioLogado);
        return ResponseEntity.ok(novaAvaliacao);
    }

    @PutMapping("/{id}")
    public ResponseEntity<RespostaAvaliacao> atualizarAvaliacao(
            @PathVariable UUID id,
            @RequestBody @Valid RequisicaoAvaliacao dados,
            @AuthenticationPrincipal Usuario usuarioLogado) {

        if (usuarioLogado == null) {
            return ResponseEntity.status(401).build();
        }

        RespostaAvaliacao avaliacaoAtualizada = avaliacaoService.atualizarAvaliacao(id, dados, usuarioLogado);
        return ResponseEntity.ok(avaliacaoAtualizada);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletarAvaliacao(
            @PathVariable UUID id,
            @AuthenticationPrincipal Usuario usuarioLogado) {

        if (usuarioLogado == null) {
            return ResponseEntity.status(401).build();
        }

        avaliacaoService.deletarAvaliacao(id, usuarioLogado);
        return ResponseEntity.noContent().build();
    }
}
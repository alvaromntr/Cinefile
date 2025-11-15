package com.example.cinefile.controle;

import com.example.cinefile.modelo.dtos.RequisicaoLogVisualizacao;
import com.example.cinefile.modelo.dtos.RespostaLogVisualizacao;
import com.example.cinefile.modelo.entidades.Usuario;
import com.example.cinefile.servico.LogVisualizacaoService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/logs-visualizacao")
@RequiredArgsConstructor
public class LogVisualizacaoController {

    private final LogVisualizacaoService logVisualizacaoService;

    @PostMapping
    public ResponseEntity<RespostaLogVisualizacao> registrarLogVisualizacao(
            @RequestBody RequisicaoLogVisualizacao dados,
            @AuthenticationPrincipal Usuario usuario) {
        return ResponseEntity.ok(logVisualizacaoService.registrarLogVisualizacao(dados, usuario));
    }

    @GetMapping
    public ResponseEntity<List<RespostaLogVisualizacao>> listarMeusLogs(@AuthenticationPrincipal Usuario usuario) {
        return ResponseEntity.ok(logVisualizacaoService.listarLogsDoUsuario(usuario));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletarLogVisualizacao(
            @PathVariable UUID id,
            @AuthenticationPrincipal Usuario usuario) {
        logVisualizacaoService.deletarLogVisualizacao(id, usuario);
        return ResponseEntity.noContent().build();
    }
}
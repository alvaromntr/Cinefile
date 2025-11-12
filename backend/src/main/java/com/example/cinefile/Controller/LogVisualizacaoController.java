package com.example.cinefile.Controller;

import com.example.cinefile.Domain.Usuario.Usuario;
import com.example.cinefile.DTO.LogVisualizacaoRequestDTO;
import com.example.cinefile.DTO.LogVisualizacaoResponseDTO;
import com.example.cinefile.Service.LogVisualizacaoService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/logs")
public class LogVisualizacaoController {

    private final LogVisualizacaoService service;

    public LogVisualizacaoController(LogVisualizacaoService service) {
        this.service = service;
    }

    @PostMapping
    public ResponseEntity<LogVisualizacaoResponseDTO> registrar(
            @RequestBody LogVisualizacaoRequestDTO dto,
            @AuthenticationPrincipal Usuario usuario
    ) {
        return ResponseEntity.ok(service.registrarLog(dto, usuario));
    }

    @GetMapping
    public ResponseEntity<List<LogVisualizacaoResponseDTO>> listar(@AuthenticationPrincipal Usuario usuario) {
        return ResponseEntity.ok(service.listarLogsDoUsuario(usuario));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletar(
            @PathVariable UUID id,
            @AuthenticationPrincipal Usuario usuario
    ) {
        service.deletarLog(id, usuario);
        return ResponseEntity.noContent().build();
    }
}

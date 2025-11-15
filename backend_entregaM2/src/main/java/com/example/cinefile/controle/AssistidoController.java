package com.example.cinefile.controle;

import com.example.cinefile.modelo.entidades.Obra;
import com.example.cinefile.modelo.entidades.Usuario;
import com.example.cinefile.servico.AssistidoService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/assistido")
@RequiredArgsConstructor
public class AssistidoController {

    private final AssistidoService assistidoService;

    @GetMapping
    public ResponseEntity<List<Obra>> listarObrasAssistidas(@AuthenticationPrincipal Usuario usuario) {
        return ResponseEntity.ok(assistidoService.listarObrasAssistidas(usuario));
    }

    @PostMapping("/{obraId}")
    public ResponseEntity<String> marcarComoAssistido(
            @PathVariable Long obraId,
            @AuthenticationPrincipal Usuario usuario) {
        assistidoService.marcarComoAssistido(obraId, usuario);
        return ResponseEntity.ok("Obra marcada como assistida!");
    }

    @DeleteMapping("/{obraId}")
    public ResponseEntity<String> removerComoAssistido(
            @PathVariable Long obraId,
            @AuthenticationPrincipal Usuario usuario) {
        assistidoService.removerComoAssistido(obraId, usuario);
        return ResponseEntity.ok("Removida dos assistidos.");
    }
}
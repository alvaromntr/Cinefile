package com.example.cinefile.Controller;

import com.example.cinefile.DTO.WatchlistResponseDTO;
import com.example.cinefile.Domain.Usuario.Usuario;
import com.example.cinefile.Service.WatchlistService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@CrossOrigin(
        origins = {"http://127.0.0.1:5500", "http://localhost:5500"},
        allowCredentials = "true"
)
@RestController
@RequestMapping("/api/watchlist")
public class WatchlistController {

    private final WatchlistService watchlistService;

    public WatchlistController(WatchlistService watchlistService) {
        this.watchlistService = watchlistService;
    }

    @GetMapping
    public ResponseEntity<List<WatchlistResponseDTO>> listar(@AuthenticationPrincipal Usuario usuario) {
        List<WatchlistResponseDTO> lista = watchlistService.listar(usuario);
        return ResponseEntity.ok(lista);
    }

    @PostMapping("/{obraId}")
    public ResponseEntity<String> adicionar(@PathVariable Long obraId,
                                            @AuthenticationPrincipal Usuario usuario) {
        watchlistService.adicionar(obraId, usuario);
        return ResponseEntity.ok("Obra adicionada à Watchlist!");
    }

    @DeleteMapping("/{obraId}")
    public ResponseEntity<String> remover(@PathVariable Long obraId,
                                          @AuthenticationPrincipal Usuario usuario) {
        watchlistService.remover(obraId, usuario);
        return ResponseEntity.ok("Removida da Watchlist.");
    }
}

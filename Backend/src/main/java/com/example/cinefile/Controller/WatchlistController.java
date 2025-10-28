package com.example.cinefile.Controller;

import com.example.cinefile.Domain.Usuario.Usuario;
import com.example.cinefile.DTO.WatchlistResponseDTO;
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
@RequestMapping("/api/me/watchlist")
public class WatchlistController {

    private final WatchlistService watchlistService;

    public WatchlistController(WatchlistService watchlistService) {
        this.watchlistService = watchlistService;
    }

    // -------- GET: listar todos os itens da watchlist --------
    @GetMapping
    public ResponseEntity<List<WatchlistResponseDTO>> listarMinhaWatchlist(
            @AuthenticationPrincipal Usuario usuario
    ) {
        if (usuario == null) {
            return ResponseEntity.status(401).build();
        }
        return ResponseEntity.ok(watchlistService.listarWatchlist(usuario));
    }

    // -------- POST: adicionar item --------
    @PostMapping("/{obraId}")
    public ResponseEntity<Void> adicionarItemNaWatchlist(
            @PathVariable Long obraId,
            @AuthenticationPrincipal Usuario usuario
    ) {
        if (usuario == null) {
            return ResponseEntity.status(401).build();
        }
        watchlistService.adicionarItem(obraId, usuario);
        return ResponseEntity.ok().build();
    }

    // -------- DELETE: remover item --------
    @DeleteMapping("/{obraId}")
    public ResponseEntity<Void> removerItemDaWatchlist(
            @PathVariable Long obraId,
            @AuthenticationPrincipal Usuario usuario
    ) {
        if (usuario == null) {
            return ResponseEntity.status(401).build();
        }
        watchlistService.removerItem(obraId, usuario);
        return ResponseEntity.noContent().build();
    }
}

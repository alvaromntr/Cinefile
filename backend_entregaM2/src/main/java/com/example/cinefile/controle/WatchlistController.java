package com.example.cinefile.controle;

import com.example.cinefile.modelo.dtos.RespostaWatchlist;
import com.example.cinefile.modelo.entidades.Usuario;
import com.example.cinefile.servico.WatchlistService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/watchlist")
@RequiredArgsConstructor
public class WatchlistController {

    private final WatchlistService watchlistService;

    @GetMapping
    public ResponseEntity<List<RespostaWatchlist>> listarMinhaWatchlist(@AuthenticationPrincipal Usuario usuario) {
        List<RespostaWatchlist> lista = watchlistService.listarWatchlistUsuario(usuario);
        return ResponseEntity.ok(lista);
    }

    @PostMapping("/{obraId}")
    public ResponseEntity<String> adicionarNaWatchlist(
            @PathVariable Long obraId,
            @AuthenticationPrincipal Usuario usuario) {
        watchlistService.adicionarObraWatchlist(obraId, usuario);
        return ResponseEntity.ok("Obra adicionada à Watchlist!");
    }

    @DeleteMapping("/{obraId}")
    public ResponseEntity<String> removerDaWatchlist(
            @PathVariable Long obraId,
            @AuthenticationPrincipal Usuario usuario) {
        watchlistService.removerObraWatchlist(obraId, usuario);
        return ResponseEntity.ok("Removida da Watchlist.");
    }
}
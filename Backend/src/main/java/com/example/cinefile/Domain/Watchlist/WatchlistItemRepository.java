package com.example.cinefile.Domain.Watchlist;

import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface WatchlistItemRepository extends JpaRepository<WatchlistItem, WatchlistItemId> {
    // Busca todos os itens da watchlist de um usuário específico
    List<WatchlistItem> findByUsuario_Id(UUID usuarioId);

    // Busca um item específico da watchlist
    Optional<WatchlistItem> findByUsuario_IdAndObra_Obraid(UUID usuarioId, Long obraId);
}
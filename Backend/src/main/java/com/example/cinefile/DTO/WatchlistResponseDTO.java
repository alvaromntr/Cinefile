package com.example.cinefile.DTO;

import com.example.cinefile.Domain.Watchlist.WatchlistItem;

import java.time.LocalDateTime;

public record WatchlistResponseDTO(
        Long obraId,
        String obraTitulo,
        LocalDateTime dataAdicionado
) {
    public WatchlistResponseDTO(WatchlistItem item) {
        this(
                item.getObra().getObraid(),
                item.getObra().getTitulo(),
                item.getData_adicionado()
        );
    }
}
package com.example.cinefile.Domain.Watchlist;

import java.io.Serializable;
import java.util.Objects;
import java.util.UUID;

public class WatchlistItemId implements Serializable {
    private UUID usuario;
    private Long obra;


    public WatchlistItemId(UUID usuarioId, Long obraId) {
        this.usuario = usuarioId;
        this.obra = obraId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        WatchlistItemId that = (WatchlistItemId) o;
        return Objects.equals(usuario, that.usuario) && Objects.equals(obra, that.obra);
    }

    @Override
    public int hashCode() {
        return Objects.hash(usuario, obra);
    }
}
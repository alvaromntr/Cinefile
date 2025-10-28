package com.example.cinefile.Domain.Watchlist;

import com.example.cinefile.Domain.Obra.Obra;
import com.example.cinefile.Domain.Usuario.Usuario;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;

@Entity
@Table(name = "watchlist")
@IdClass(WatchlistItemId.class)
@Getter
@Setter
@NoArgsConstructor
public class WatchlistItem {

    @Id
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "usuario_id")
    private Usuario usuario;

    @Id
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "obra_id")
    private Obra obra;

    @CreationTimestamp
    @Column(updatable = false)
    private LocalDateTime data_adicionado;

    public WatchlistItem(Usuario usuario, Obra obra) {
        this.usuario = usuario;
        this.obra = obra;
    }
}
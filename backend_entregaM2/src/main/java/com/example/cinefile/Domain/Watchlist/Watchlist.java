package com.example.cinefile.Domain.Watchlist;

import com.example.cinefile.Domain.Obra.Obra;
import com.example.cinefile.Domain.Usuario.Usuario;
import jakarta.persistence.*;

@Entity
@Table(name = "watchlist")
public class Watchlist {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "obra_id")
    private Obra obra;

    @ManyToOne
    @JoinColumn(name = "usuario_id")
    private Usuario usuario;

    public Watchlist() {}

    // Construtor que liga obra e usuário
    public Watchlist(Obra obra, Usuario usuario) {
        this.obra = obra;
        this.usuario = usuario;
    }

    public Long getId() { return id; }
    public Obra getObra() { return obra; }
    public void setObra(Obra obra) { this.obra = obra; }

    public Usuario getUsuario() { return usuario; }
    public void setUsuario(Usuario usuario) { this.usuario = usuario; }
}

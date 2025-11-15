package com.example.cinefile.modelo.entidades;


import com.example.cinefile.modelo.enums.TipoObra;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "obra")
@Getter
@Setter
@NoArgsConstructor
public class Obra {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long tmdbId;

    private String titulo;

    @Column(length = 2000)
    private String descricao;

    @Enumerated(EnumType.STRING)
    private TipoObra tipo;

    private Integer anoLancamento;

    private String posterUrl;

    private Integer duracao;

    @ManyToMany
    @JoinTable(
            name = "obra_categoria",
            joinColumns = @JoinColumn(name = "obra_id"),
            inverseJoinColumns = @JoinColumn(name = "categoria_id")
    )
    private List<Categoria> categorias = new ArrayList<>();

    public Obra(String titulo, String descricao, TipoObra tipo, Integer anoLancamento, String posterUrl, Integer duracao) {
        this.titulo = titulo;
        this.descricao = descricao;
        this.tipo = tipo;
        this.anoLancamento = anoLancamento;
        this.posterUrl = posterUrl;
        this.duracao = duracao;
    }
}
package com.example.cinefile.Domain.Obra;

import com.example.cinefile.Domain.Categoria.Categoria;
import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "obras")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(of = "obraid")
public class Obra {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long obraid;

    private Long tmdbId;

    private String titulo;

    @Column(length = 2000)
    private String descricao;

    @Enumerated(EnumType.STRING)
    private ObraTipo tipo; // FILME ou SERIE

    private Integer anolancamento;

    private String poster_url;

    private Integer duracao; // minutos

    public Obra(RequestObra requestObra) {
        this.tmdbId = requestObra.tmdbId();
        this.titulo = requestObra.titulo();
        this.descricao = requestObra.descricao();
        this.tipo = requestObra.tipo();
        this.anolancamento = requestObra.anolancamento();
        this.poster_url = requestObra.poster_url();
        this.duracao = requestObra.duracao();
    }


    @ManyToMany
    @JoinTable(
            name = "obra_categoria",
            joinColumns = @JoinColumn(name = "obraid"),
            inverseJoinColumns = @JoinColumn(name = "categoria_id")
    )
    private List<Categoria> categorias = new ArrayList<>();
}

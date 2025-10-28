package com.example.cinefile.Domain.Obra;

import com.example.cinefile.Domain.Categoria.Categoria;
import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Entity(name = "obras")
@Table(name = "obras")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(of="obraid")
public class Obra {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long obraid;
    private String titulo;
    private String descricao;
    private ObraTipo tipo; // filme ou série
    private Integer anolancamento;
    private String poster_url;
    private Integer duracao; // minutos, normalmente para filmes

    public Obra(RequestObra requestObra) {
        this.titulo = requestObra.titulo();
        this.descricao = requestObra.descricao();
        this.tipo = requestObra.tipo(); // agora certo
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

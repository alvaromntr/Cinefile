package com.example.cinefile.modelo.entidades;

import jakarta.persistence.*;
import lombok.*;

import java.util.UUID;

@Entity
@Table(name = "temporada")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(of = "id")
public class Temporada {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    private Integer numero;

    private Integer quantidadeEpisodios;

    private String descricao;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "obra_id")
    private Obra obra;

    public Temporada(Integer numero, Integer quantidadeEpisodios, String descricao, Obra obra) {
        this.numero = numero;
        this.quantidadeEpisodios = quantidadeEpisodios;
        this.descricao = descricao;
        this.obra = obra;
    }
}
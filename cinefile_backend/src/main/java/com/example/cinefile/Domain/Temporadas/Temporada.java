package com.example.cinefile.Domain.Temporadas;


import com.example.cinefile.Domain.Obra.Obra;
import jakarta.persistence.*;
import lombok.*;

import java.util.UUID;

@Entity(name = "Temporada")
@Table(name = "Temporada")
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

    public Temporada(RequestTemporada request, Obra obra) {
        this.numero = request.numero();
        this.quantidadeEpisodios = request.quantidadeEpisodios();
        this.descricao = request.descricao();
        this.obra = obra;
    }
}
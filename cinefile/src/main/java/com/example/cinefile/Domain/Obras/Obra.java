package com.example.cinefile.Domain.Obras;

import jakarta.persistence.*;
import lombok.*;

@Entity(name = "Obras")
@Table(name ="Obras")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(of="id")
public class Obra {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private Long obra_id;

    private String titulo;

    private String descricao;

    private String tipo; // filme ou serie

    private Integer ano_lancamento;

    private String poster_url;

    private Integer duracao; // minutos, normalmente para filmes
    private Boolean active;

    public Obra(RequestObra requestObra) {
        this.titulo = requestObra.titulo();
        this.descricao = requestObra.descricao();
        this.tipo = requestObra.tipo();
        this.ano_lancamento = requestObra.ano_lancamento();
        this.poster_url = requestObra.poster_url();
        this.duracao = requestObra.duracao();
        this.active=true;
    }
}

package com.example.cinefile.modelo.entidades;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "avaliacao")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(of = "id")
public class Avaliacao {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    private Integer nota;

    private String comentario;

    @Column(nullable = false)
    private LocalDateTime dataCriacao;

    @ManyToOne
    @JoinColumn(name = "obra_id")
    private Obra obra;

    @ManyToOne
    @JoinColumn(name = "usuario_id")
    private Usuario usuario;

    @ManyToOne
    @JoinColumn(name = "temporada_id")
    private Temporada temporada;

    public Avaliacao(Integer nota, String comentario, Obra obra, Temporada temporada, Usuario usuario) {
        this.nota = nota;
        this.comentario = comentario;
        this.obra = obra;
        this.temporada = temporada;
        this.usuario = usuario;
        this.dataCriacao = LocalDateTime.now(); // ✅ CORRIGIDO
    }

    @PrePersist
    protected void aoCriar() {
        if (this.dataCriacao == null) {
            this.dataCriacao = LocalDateTime.now();
        }
    }
}
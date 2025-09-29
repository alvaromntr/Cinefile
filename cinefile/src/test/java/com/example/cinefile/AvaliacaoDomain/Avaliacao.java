package com.example.cinefile.AvaliacaoDomain;

import com.example.cinefile.Domain.Usuario.ObraDomain.Obra;
import com.example.cinefile.Domain.Usuario.Usuario;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.UUID;

@Entity(name = "Avaliacao")
@Table(name ="Avaliacao")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(of="id")
public class Avaliacao {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID id;

    private Integer nota;

    private String comentario;

    private LocalDateTime dataCriacao;

    @ManyToOne
    private Obra obra;

    @ManyToOne
    private Usuario usuario;

    //  Construtor único para RequestAvaliacao
    public Avaliacao(RequestAvaliacao data, Obra obra, Usuario usuario) {
        this.nota = data.nota();
        this.comentario = data.comentario();
        this.obra = obra;
        this.usuario = usuario;
        this.dataCriacao = LocalDateTime.now();
    }
}

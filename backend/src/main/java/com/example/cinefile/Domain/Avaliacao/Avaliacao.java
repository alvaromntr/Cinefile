package com.example.cinefile.Domain.Avaliacao;


import com.example.cinefile.Domain.Obra.Obra;
import com.example.cinefile.Domain.Temporadas.Temporada;
import com.example.cinefile.Domain.Usuario.Usuario;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.UUID;

@Entity(name = "Avaliacao")
@Table(name = "Avaliacao")
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

    public Avaliacao(RequestAvaliacao data, Obra obra, Temporada temporada, Usuario usuario) {
        this.nota = data.nota();
        this.comentario = data.comentario();
        this.obra = obra;
        this.temporada = temporada;
        this.usuario = usuario;
        this.dataCriacao = LocalDateTime.now();
    }
}

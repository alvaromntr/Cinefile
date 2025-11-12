package com.example.cinefile.Domain.Logs;

import com.example.cinefile.Domain.Obra.Obra;
import com.example.cinefile.Domain.Usuario.Usuario;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.UUID;

@Entity
@Table(name = "logsdevisualizacao")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class LogVisualizacao {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID id;

    @ManyToOne(optional = false)
    @JoinColumn(name = "usuario_id")
    private Usuario usuario;

    @ManyToOne(optional = false)
    @JoinColumn(name = "obra_id")
    private Obra obra;
}

package com.example.cinefile.Domain.Watched;

import com.example.cinefile.Domain.Obra.Obra;
import jakarta.persistence.*;

@Entity
@Table(name = "watched")
public class Watched {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "obra_id")
    private Obra obra;

    public Watched() {}
    public Watched(Obra obra) { this.obra = obra; }

    public Long getId() { return id; }
    public Obra getObra() { return obra; }
    public void setObra(Obra obra) { this.obra = obra; }
}

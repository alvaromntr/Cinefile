package com.example.cinefile.modelo.entidades;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "categoria")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Categoria {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "categoria_id")
    private Long id;

    @Column(length = 50, unique = true, nullable = false)
    private String nome;

    public Categoria(String nome) {
        this.nome = nome;
    }

    @ManyToMany(mappedBy = "categorias")
    @JsonIgnore
    private List<Obra> obras = new ArrayList<>();
}
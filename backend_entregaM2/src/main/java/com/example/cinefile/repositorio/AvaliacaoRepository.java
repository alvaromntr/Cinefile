package com.example.cinefile.repositorio;

import com.example.cinefile.modelo.entidades.Avaliacao;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface AvaliacaoRepository extends JpaRepository<Avaliacao, UUID> {
}
package com.example.cinefile.repositorio;

import com.example.cinefile.modelo.entidades.Temporada;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface TemporadaRepository extends JpaRepository<Temporada, UUID> {
    List<Temporada> findByObraId(Long obraId);
}
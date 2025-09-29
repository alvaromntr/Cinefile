package com.example.cinefile.TemporadaDomain;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface TemporadaRepository extends JpaRepository<Temporada, UUID> {

    // Buscar todas as temporadas de uma obra específica (série)
    List<Temporada> findByObraObra_id(UUID obraId);
}

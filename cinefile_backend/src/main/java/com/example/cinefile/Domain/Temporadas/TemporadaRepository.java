package com.example.cinefile.Domain.Temporadas;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface TemporadaRepository extends JpaRepository<Temporada, UUID> {

    // Buscar todas as temporadas de uma obra específica (série)
    List<Temporada> findByObraObraid(Long obraid);
}
package com.example.cinefile.Domain.Obra;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface ObraRepository extends JpaRepository<Obra, Long> {

    // Buscar todas as obras de um tipo específico
    List<Obra> findByTipo(ObraTipo tipo);

    // Buscar obras cujo título contenha determinada palavra
    List<Obra> findByTituloContainingIgnoreCase(String titulo);

    // Buscar obras por ano de lançamento
    List<Obra> findByAnolancamento(Integer ano);

    // Buscar obras por tipo e ano
    List<Obra> findByTipoAndAnolancamento(ObraTipo tipo, Integer ano);

    // 🔹 Novo: Buscar obra pelo ID do TMDb (para evitar duplicatas)
    Optional<Obra> findByTmdbId(Long tmdbId);
}

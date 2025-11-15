package com.example.cinefile.repositorio;

import com.example.cinefile.modelo.entidades.Obra;
import com.example.cinefile.modelo.enums.TipoObra;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface ObraRepository extends JpaRepository<Obra, Long> {
    List<Obra> findByTipo(TipoObra tipo);
    List<Obra> findByTituloContainingIgnoreCase(String titulo);
    List<Obra> findByAnoLancamento(Integer ano);
    Optional<Obra> findByTmdbId(Long tmdbId);
}
package com.example.cinefile.ObraDomain;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface ObraRepository extends JpaRepository<Obra, UUID> {

    // Buscar todas as obras de um tipo específico
    List<Obra> findByTipo(String tipo);

    // Buscar obras cujo título contenha determinada palavra
    List<Obra> findByTituloContainingIgnoreCase(String titulo);

    // Buscar obras por ano de lançamento
    List<Obra> findByAno_Lancamento(Integer ano);

    // Buscar obras ativas (active = true)
    List<Obra> findByActiveTrue();

    // Buscar obras por tipo e ano
    List<Obra> findByTipoAndAno_Lancamento(String tipo, Integer ano);
}

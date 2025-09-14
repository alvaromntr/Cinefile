package com.example.cinefile.Repository;

import domain.Obras_domain;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface ObrasRepository extends JpaRepository<Obras_domain, Integer> {
    // Busca obras cujo título contenha a string fornecida (case-insensitive)
    List<Obras_domain> findByTituloContainingIgnoreCase(String titulo);
}

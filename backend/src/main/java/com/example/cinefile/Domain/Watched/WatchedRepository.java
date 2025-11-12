package com.example.cinefile.Domain.Watched;

import org.springframework.data.jpa.repository.JpaRepository;

public interface WatchedRepository extends JpaRepository<Watched, Long> {

    // Corrigido: navega para o campo "obraid" dentro da entidade Obra
    boolean existsByObra_Obraid(Long obraid);

    void deleteByObra_Obraid(Long obraid);
}

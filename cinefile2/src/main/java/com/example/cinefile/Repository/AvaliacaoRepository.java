package com.example.cinefile.Repository;

import domain.Avaliacao_domain;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.UUID;

public interface AvaliacaoRepository extends JpaRepository<Avaliacao_domain, UUID> {

}

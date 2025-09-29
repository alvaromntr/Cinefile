package com.example.cinefile.AvaliacaoDomain;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface AvaliacaoRepository extends JpaRepository<Avaliacao, UUID> {
}

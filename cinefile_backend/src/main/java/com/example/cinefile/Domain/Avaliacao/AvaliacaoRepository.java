package com.example.cinefile.Domain.Avaliacao;

import com.example.cinefile.Domain.Obra.Obra;
import com.example.cinefile.Domain.Usuario.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface AvaliacaoRepository extends JpaRepository<Avaliacao, UUID> {
    Optional<Avaliacao> findByObraAndUsuario(Obra obra, Usuario usuario);
    List<Avaliacao> findAllByUsuario(Usuario usuario);
}

package com.example.cinefile.repositorio;

import com.example.cinefile.modelo.entidades.Comentario;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ComentarioRepository extends JpaRepository<Comentario, Long> {
    List<Comentario> findByObraIdOrderByDataComentarioDesc(Long obraId);
}
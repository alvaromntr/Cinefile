package com.example.cinefile.repositorio;

import com.example.cinefile.modelo.entidades.Assistido;
import com.example.cinefile.modelo.entidades.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface AssistidoRepository extends JpaRepository<Assistido, Long> {
    boolean existsByUsuarioAndObraId(Usuario usuario, Long obraId);

    @Modifying
    @Query("DELETE FROM Assistido a WHERE a.usuario = :usuario AND a.obra.id = :obraId")
    void deleteByUsuarioAndObraId(@Param("usuario") Usuario usuario, @Param("obraId") Long obraId);

    List<Assistido> findByUsuario(Usuario usuario);
}
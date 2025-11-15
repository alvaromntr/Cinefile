package com.example.cinefile.repositorio;

import com.example.cinefile.modelo.entidades.Usuario;
import com.example.cinefile.modelo.entidades.Watchlist;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface WatchlistRepository extends JpaRepository<Watchlist, Long> {
    boolean existsByUsuarioAndObraId(Usuario usuario, Long obraId);

    @Modifying
    @Query("DELETE FROM Watchlist w WHERE w.usuario = :usuario AND w.obra.id = :obraId")
    void deleteByUsuarioAndObraId(@Param("usuario") Usuario usuario, @Param("obraId") Long obraId);

    List<Watchlist> findByUsuario(Usuario usuario);
}
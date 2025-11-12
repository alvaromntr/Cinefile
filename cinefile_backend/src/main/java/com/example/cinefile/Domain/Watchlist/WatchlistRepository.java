package com.example.cinefile.Domain.Watchlist;

import com.example.cinefile.Domain.Usuario.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface WatchlistRepository extends JpaRepository<Watchlist, Long> {

    boolean existsByUsuarioAndObra_Obraid(Usuario usuario, Long obraid);

    void deleteByUsuarioAndObra_Obraid(Usuario usuario, Long obraid);

    List<Watchlist> findByUsuario(Usuario usuario);
}

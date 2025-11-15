package com.example.cinefile.Domain.Watched;

import com.example.cinefile.Domain.Usuario.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface WatchedRepository extends JpaRepository<Watched, Long> {

    boolean existsByObra_ObraidAndUsuario(Long obraid, Usuario usuario);

    void deleteByObra_ObraidAndUsuario(Long obraid, Usuario usuario);

    List<Watched> findByUsuario(Usuario usuario);
}

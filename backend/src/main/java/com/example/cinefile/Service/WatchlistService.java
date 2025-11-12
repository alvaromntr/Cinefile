package com.example.cinefile.Service;

import com.example.cinefile.Domain.Obra.Obra;
import com.example.cinefile.Domain.Obra.ObraRepository;
import com.example.cinefile.Domain.Usuario.Usuario;
import com.example.cinefile.Domain.Watchlist.Watchlist;
import com.example.cinefile.Domain.Watchlist.WatchlistRepository;
import com.example.cinefile.DTO.WatchlistResponseDTO;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class WatchlistService {

    private final WatchlistRepository repo;
    private final ObraRepository obraRepo;

    public WatchlistService(WatchlistRepository repo, ObraRepository obraRepo) {
        this.repo = repo;
        this.obraRepo = obraRepo;
    }

    public List<WatchlistResponseDTO> listar(Usuario usuario) {
        return repo.findByUsuario(usuario).stream()
                .map(WatchlistResponseDTO::new)
                .toList();
    }

    public void adicionar(Long obraid, Usuario usuario) {
        if (repo.existsByUsuarioAndObra_Obraid(usuario, obraid)) return;

        Obra obra = obraRepo.findById(obraid)
                .orElseThrow(() -> new RuntimeException("Obra não encontrada"));

        repo.save(new Watchlist(obra, usuario));
    }

    public void remover(Long obraid, Usuario usuario) {
        repo.deleteByUsuarioAndObra_Obraid(usuario, obraid);
    }
}

package com.example.cinefile.servico;

import com.example.cinefile.modelo.dtos.RespostaWatchlist;
import com.example.cinefile.modelo.entidades.Obra;
import com.example.cinefile.modelo.entidades.Usuario;
import com.example.cinefile.modelo.entidades.Watchlist;
import com.example.cinefile.repositorio.ObraRepository;
import com.example.cinefile.repositorio.WatchlistRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class WatchlistService {

    private final WatchlistRepository watchlistRepository;
    private final ObraRepository obraRepository;

    public List<RespostaWatchlist> listarWatchlistUsuario(Usuario usuario) {
        return watchlistRepository.findByUsuario(usuario).stream()
                .map(this::mapearParaRespostaWatchlist)
                .toList();
    }

    public void adicionarObraWatchlist(Long obraId, Usuario usuario) {
        if (watchlistRepository.existsByUsuarioAndObraId(usuario, obraId)) {
            return; // Já está na watchlist
        }

        Obra obra = buscarObraPorId(obraId);
        Watchlist itemWatchlist = new Watchlist(obra, usuario);
        watchlistRepository.save(itemWatchlist);
    }

    public void removerObraWatchlist(Long obraId, Usuario usuario) {
        watchlistRepository.deleteByUsuarioAndObraId(usuario, obraId);
    }

    private Obra buscarObraPorId(Long obraId) {
        return obraRepository.findById(obraId)
                .orElseThrow(() -> new RuntimeException("Obra não encontrada"));
    }

    private RespostaWatchlist mapearParaRespostaWatchlist(Watchlist item) {
        return new RespostaWatchlist(
                item.getObra().getId(),
                item.getObra().getTitulo()
        );
    }
}
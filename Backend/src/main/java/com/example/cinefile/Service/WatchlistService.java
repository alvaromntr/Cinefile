package com.example.cinefile.Service;

import com.example.cinefile.Domain.Obra.Obra;
import com.example.cinefile.Domain.Obra.ObraRepository;
import com.example.cinefile.Domain.Usuario.Usuario;
import com.example.cinefile.DTO.WatchlistResponseDTO;
import com.example.cinefile.Domain.Watchlist.WatchlistItem;
import com.example.cinefile.Domain.Watchlist.WatchlistItemRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class WatchlistService {

    @Autowired private WatchlistItemRepository watchlistRepository;
    @Autowired private ObraRepository obraRepository;

    public List<WatchlistResponseDTO> listarWatchlist(Usuario usuario) {
        return watchlistRepository.findByUsuario_Id(usuario.getId()).stream()
                .map(WatchlistResponseDTO::new)
                .collect(Collectors.toList());
    }

    public void adicionarItem(Long obraId, Usuario usuario) {
        Obra obra = obraRepository.findById(obraId)
                .orElseThrow(() -> new EntityNotFoundException("Obra não encontrada."));

        WatchlistItem item = new WatchlistItem(usuario, obra);
        try {
            watchlistRepository.save(item);
        } catch (DataIntegrityViolationException e) {

            throw new IllegalArgumentException("Esta obra já está na sua watchlist.");
        }
    }

    public void removerItem(Long obraId, Usuario usuario) {
        WatchlistItem item = watchlistRepository.findByUsuario_IdAndObra_Obraid(usuario.getId(), obraId)
                .orElseThrow(() -> new EntityNotFoundException("Esta obra não está na sua watchlist."));

        watchlistRepository.delete(item);
    }
}
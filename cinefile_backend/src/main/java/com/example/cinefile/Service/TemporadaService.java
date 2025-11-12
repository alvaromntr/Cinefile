package com.example.cinefile.Service;


import com.example.cinefile.DTO.TemporadaDTO;
import com.example.cinefile.Domain.Obra.Obra;
import com.example.cinefile.Domain.Obra.ObraRepository;
import com.example.cinefile.Domain.Temporadas.Temporada;
import com.example.cinefile.Domain.Temporadas.TemporadaRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class TemporadaService {

    @Autowired private TemporadaRepository temporadaRepository;
    @Autowired private ObraRepository obraRepository;

    public List<TemporadaDTO> listarTemporadasPorObra(Long obraid) {
        return temporadaRepository.findByObraObraid(obraid).stream()
                .map(TemporadaDTO::new)
                .collect(Collectors.toList());
    }

    public TemporadaDTO criarTemporada(Long obraid, TemporadaDTO dto) {
        Obra obra = obraRepository.findById(obraid)
                .orElseThrow(() -> new EntityNotFoundException("Obra não encontrada."));

        Temporada novaTemporada = new Temporada();
        novaTemporada.setNumero(dto.numero());
        novaTemporada.setQuantidadeEpisodios(dto.quantidadeEpisodios());
        novaTemporada.setDescricao(dto.descricao());
        novaTemporada.setObra(obra);

        temporadaRepository.save(novaTemporada);
        return new TemporadaDTO(novaTemporada);
    }

}
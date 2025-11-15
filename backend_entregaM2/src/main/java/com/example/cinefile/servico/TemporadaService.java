package com.example.cinefile.servico;

import com.example.cinefile.modelo.dtos.RespostaTemporada;
import com.example.cinefile.modelo.entidades.Obra;
import com.example.cinefile.modelo.entidades.Temporada;
import com.example.cinefile.repositorio.ObraRepository;
import com.example.cinefile.repositorio.TemporadaRepository;
import com.example.cinefile.shared.excecoes.EntidadeNaoEncontradaException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class TemporadaService {

    private final TemporadaRepository temporadaRepository;
    private final ObraRepository obraRepository;

    public List<RespostaTemporada> listarTemporadasPorObra(Long obraId) {
        return temporadaRepository.findByObraId(obraId).stream()
                .map(this::mapearParaRespostaTemporada)
                .toList();
    }

    public RespostaTemporada criarTemporada(Long obraId, RespostaTemporada dados) {
        Obra obra = buscarObraPorId(obraId);
        Temporada novaTemporada = new Temporada(
                dados.numero(),
                dados.quantidadeEpisodios(),
                dados.descricao(),
                obra
        );
        temporadaRepository.save(novaTemporada);
        return mapearParaRespostaTemporada(novaTemporada);
    }

    public Temporada buscarPorId(UUID id) {
        return temporadaRepository.findById(id)
                .orElseThrow(() -> new EntidadeNaoEncontradaException("Temporada não encontrada com ID: " + id));
    }

    public void deletarTemporada(UUID id) {
        Temporada temporada = buscarPorId(id);
        temporadaRepository.delete(temporada);
    }

    private Obra buscarObraPorId(Long obraId) {
        return obraRepository.findById(obraId)
                .orElseThrow(() -> new EntidadeNaoEncontradaException("Obra não encontrada."));
    }

    private RespostaTemporada mapearParaRespostaTemporada(Temporada temporada) {
        return new RespostaTemporada(
                temporada.getId(),
                temporada.getNumero(),
                temporada.getQuantidadeEpisodios(),
                temporada.getDescricao()
        );
    }
}
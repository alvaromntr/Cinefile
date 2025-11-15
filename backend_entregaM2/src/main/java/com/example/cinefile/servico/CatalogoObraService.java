package com.example.cinefile.servico;

import com.example.cinefile.modelo.dtos.RespostaObra;
import com.example.cinefile.modelo.entidades.Obra;
import com.example.cinefile.modelo.enums.TipoObra;
import com.example.cinefile.repositorio.ObraRepository;

import com.example.cinefile.shared.excecoes.ObraNaoEncontradaException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CatalogoObraService {

    private final ObraRepository obraRepository;

    @Transactional(readOnly = true)
    public List<RespostaObra> listarObrasPorTipo(TipoObra tipo) {
        List<Obra> obras = obraRepository.findByTipo(tipo);
        return mapearObrasParaResposta(obras);
    }

    @Transactional(readOnly = true)
    public RespostaObra buscarObraPorId(Long obraId) {
        Obra obra = buscarObraPorIdOuFalhar(obraId);
        return mapearObraParaResposta(obra);
    }

    @Transactional(readOnly = true)
    public List<RespostaObra> buscarTodasObras() {
        List<Obra> obras = obraRepository.findAll();
        return mapearObrasParaResposta(obras);
    }

    private Obra buscarObraPorIdOuFalhar(Long obraId) {
        return obraRepository.findById(obraId)
                .orElseThrow(() -> new ObraNaoEncontradaException(obraId));
    }

    private RespostaObra mapearObraParaResposta(Obra obra) {
        return new RespostaObra(
                obra.getId(),
                obra.getTmdbId(),
                obra.getTitulo(),
                obra.getDescricao(),
                obra.getTipo(),
                obra.getAnoLancamento(),
                obra.getPosterUrl(),
                obra.getDuracao(),
                List.of() // categorias podem ser mapeadas aqui
        );
    }

    private List<RespostaObra> mapearObrasParaResposta(List<Obra> obras) {
        return obras.stream()
                .map(this::mapearObraParaResposta)
                .toList();
    }
}
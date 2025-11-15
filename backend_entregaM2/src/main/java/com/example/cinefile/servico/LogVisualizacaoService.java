package com.example.cinefile.servico;

import com.example.cinefile.modelo.dtos.RequisicaoLogVisualizacao;
import com.example.cinefile.modelo.dtos.RespostaLogVisualizacao;
import com.example.cinefile.modelo.entidades.LogVisualizacao;
import com.example.cinefile.modelo.entidades.Obra;
import com.example.cinefile.modelo.entidades.Usuario;
import com.example.cinefile.repositorio.LogVisualizacaoRepository;
import com.example.cinefile.repositorio.ObraRepository;
import com.example.cinefile.shared.excecoes.AcessoNegadoException;
import com.example.cinefile.shared.excecoes.EntidadeNaoEncontradaException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class LogVisualizacaoService {

    private final LogVisualizacaoRepository logVisualizacaoRepository;
    private final ObraRepository obraRepository;

    public RespostaLogVisualizacao registrarLogVisualizacao(RequisicaoLogVisualizacao dados, Usuario usuario) {
        Obra obra = buscarObraPorId(dados.obraId());

        LogVisualizacao log = new LogVisualizacao();
        log.setUsuario(usuario);
        log.setObra(obra);

        logVisualizacaoRepository.save(log);
        return mapearParaRespostaLogVisualizacao(log);
    }

    public List<RespostaLogVisualizacao> listarLogsDoUsuario(Usuario usuario) {
        return logVisualizacaoRepository.findByUsuarioId(usuario.getId())
                .stream()
                .map(this::mapearParaRespostaLogVisualizacao)
                .toList();
    }

    public void deletarLogVisualizacao(UUID logId, Usuario usuario) {
        LogVisualizacao log = buscarLogPorId(logId);
        verificarPermissaoDelecao(log, usuario);
        logVisualizacaoRepository.delete(log);
    }

    private Obra buscarObraPorId(Long obraId) {
        return obraRepository.findById(obraId)
                .orElseThrow(() -> new EntidadeNaoEncontradaException("Obra não encontrada"));
    }

    private LogVisualizacao buscarLogPorId(UUID logId) {
        return logVisualizacaoRepository.findById(logId)
                .orElseThrow(() -> new EntidadeNaoEncontradaException("Log não encontrado"));
    }

    private void verificarPermissaoDelecao(LogVisualizacao log, Usuario usuario) {
        if (!log.getUsuario().getId().equals(usuario.getId())) {
            throw new AcessoNegadoException("Você não tem permissão para deletar este log.");
        }
    }

    private RespostaLogVisualizacao mapearParaRespostaLogVisualizacao(LogVisualizacao log) {
        return new RespostaLogVisualizacao(
                log.getId(),
                log.getUsuario() != null ? log.getUsuario().getId() : null,
                log.getObra() != null ? log.getObra().getId() : null,
                log.getObra() != null ? log.getObra().getTitulo() : "Obra Desconhecida"
        );
    }
}
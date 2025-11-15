package com.example.cinefile.servico;

import com.example.cinefile.modelo.dtos.RequisicaoAvaliacao;
import com.example.cinefile.modelo.dtos.RespostaAvaliacao;
import com.example.cinefile.modelo.entidades.Avaliacao;
import com.example.cinefile.modelo.entidades.Obra;
import com.example.cinefile.modelo.entidades.Temporada;
import com.example.cinefile.modelo.entidades.Usuario;
import com.example.cinefile.modelo.enums.TipoUsuario;
import com.example.cinefile.repositorio.AvaliacaoRepository;
import com.example.cinefile.repositorio.ObraRepository;
import com.example.cinefile.repositorio.TemporadaRepository;
import com.example.cinefile.shared.excecoes.AcessoNegadoException;
import com.example.cinefile.shared.excecoes.EntidadeNaoEncontradaException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class AvaliacaoService {

    private final AvaliacaoRepository avaliacaoRepository;
    private final ObraRepository obraRepository;
    private final TemporadaRepository temporadaRepository;

    @Transactional
    public RespostaAvaliacao criarAvaliacao(RequisicaoAvaliacao dados, Usuario usuarioLogado) {
        Obra obra = buscarObraPorId(dados.obraId());
        Temporada temporada = buscarTemporadaOpcional(dados.temporadaId());

        Avaliacao novaAvaliacao = new Avaliacao(dados.nota(), dados.comentario(), obra, temporada, usuarioLogado);
        avaliacaoRepository.save(novaAvaliacao);

        return mapearParaRespostaAvaliacao(novaAvaliacao);
    }

    @Transactional
    public RespostaAvaliacao atualizarAvaliacao(UUID avaliacaoId, RequisicaoAvaliacao dados, Usuario usuarioLogado) {
        Avaliacao avaliacao = buscarAvaliacaoPorId(avaliacaoId);
        verificarPermissaoEdicao(avaliacao, usuarioLogado);

        if (dados.nota() != null) avaliacao.setNota(dados.nota());
        if (dados.comentario() != null) avaliacao.setComentario(dados.comentario());

        avaliacaoRepository.save(avaliacao);
        return mapearParaRespostaAvaliacao(avaliacao);
    }

    @Transactional
    public void deletarAvaliacao(UUID avaliacaoId, Usuario usuarioLogado) {
        Avaliacao avaliacao = buscarAvaliacaoPorId(avaliacaoId);
        verificarPermissaoDelecao(avaliacao, usuarioLogado);
        avaliacaoRepository.delete(avaliacao);
    }

    private Obra buscarObraPorId(Long obraId) {
        return obraRepository.findById(obraId)
                .orElseThrow(() -> new EntidadeNaoEncontradaException("Obra não encontrada."));
    }

    private Temporada buscarTemporadaOpcional(UUID temporadaId) {
        if (temporadaId == null) return null;
        return temporadaRepository.findById(temporadaId)
                .orElseThrow(() -> new EntidadeNaoEncontradaException("Temporada não encontrada."));
    }

    private Avaliacao buscarAvaliacaoPorId(UUID avaliacaoId) {
        return avaliacaoRepository.findById(avaliacaoId)
                .orElseThrow(() -> new EntidadeNaoEncontradaException("Avaliação não encontrada."));
    }

    private void verificarPermissaoEdicao(Avaliacao avaliacao, Usuario usuario) {
        if (!avaliacao.getUsuario().getId().equals(usuario.getId())) {
            throw new AcessoNegadoException("Você não tem permissão para editar esta avaliação.");
        }
    }

    private void verificarPermissaoDelecao(Avaliacao avaliacao, Usuario usuario) {
        boolean naoEDono = !avaliacao.getUsuario().getId().equals(usuario.getId());
        boolean naoEAdmin = usuario.getRole() != TipoUsuario.ADMINISTRADOR;

        if (naoEDono && naoEAdmin) {
            throw new AcessoNegadoException("Você não tem permissão para deletar esta avaliação.");
        }
    }

    private RespostaAvaliacao mapearParaRespostaAvaliacao(Avaliacao avaliacao) {
        return new RespostaAvaliacao(
                avaliacao.getId(),
                avaliacao.getNota(),
                avaliacao.getComentario(),
                avaliacao.getDataCriacao(),
                avaliacao.getUsuario() != null ? avaliacao.getUsuario().getUsername() : null,
                avaliacao.getObra() != null ? avaliacao.getObra().getId() : null,
                avaliacao.getTemporada() != null ? avaliacao.getTemporada().getId() : null
        );
    }
}
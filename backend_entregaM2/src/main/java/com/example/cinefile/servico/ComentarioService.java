package com.example.cinefile.servico;

import com.example.cinefile.modelo.dtos.RequisicaoComentario;
import com.example.cinefile.modelo.dtos.RespostaComentario;
import com.example.cinefile.modelo.entidades.Comentario;
import com.example.cinefile.modelo.entidades.Obra;
import com.example.cinefile.modelo.entidades.Usuario;
import com.example.cinefile.modelo.enums.TipoUsuario;
import com.example.cinefile.repositorio.ComentarioRepository;
import com.example.cinefile.repositorio.ObraRepository;
import com.example.cinefile.shared.excecoes.AcessoNegadoException;
import com.example.cinefile.shared.excecoes.EntidadeNaoEncontradaException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ComentarioService {

    private final ComentarioRepository comentarioRepository;
    private final ObraRepository obraRepository;

    public List<RespostaComentario> listarComentariosPorObra(Long obraId) {
        return comentarioRepository.findByObraIdOrderByDataComentarioDesc(obraId)
                .stream()
                .map(this::mapearParaRespostaComentario)
                .toList();
    }

    public RespostaComentario criarComentario(Long obraId, RequisicaoComentario dados, Usuario usuarioLogado) {
        Obra obra = buscarObraPorId(obraId);
        Comentario comentario = new Comentario(dados.texto(), usuarioLogado, obra);
        comentarioRepository.save(comentario);
        return mapearParaRespostaComentario(comentario);
    }

    public void deletarComentario(Long comentarioId, Usuario usuarioLogado) {
        Comentario comentario = buscarComentarioPorId(comentarioId);
        verificarPermissaoDelecao(comentario, usuarioLogado);
        comentarioRepository.delete(comentario);
    }

    private Obra buscarObraPorId(Long obraId) {
        return obraRepository.findById(obraId)
                .orElseThrow(() -> new EntidadeNaoEncontradaException("Obra não encontrada"));
    }

    private Comentario buscarComentarioPorId(Long comentarioId) {
        return comentarioRepository.findById(comentarioId)
                .orElseThrow(() -> new EntidadeNaoEncontradaException("Comentário não encontrado"));
    }

    private void verificarPermissaoDelecao(Comentario comentario, Usuario usuario) {
        boolean naoEDono = !comentario.getUsuario().getId().equals(usuario.getId());
        boolean naoEAdmin = usuario.getRole() != TipoUsuario.ADMINISTRADOR;

        if (naoEDono && naoEAdmin) {
            throw new AcessoNegadoException("Você não tem permissão para deletar este comentário.");
        }
    }

    private RespostaComentario mapearParaRespostaComentario(Comentario comentario) {
        return new RespostaComentario(
                comentario.getId(),
                comentario.getTexto(),
                comentario.getDataComentario(),
                comentario.getUsuario() != null ? comentario.getUsuario().getUsername() : "Usuário Anônimo"
        );
    }
}
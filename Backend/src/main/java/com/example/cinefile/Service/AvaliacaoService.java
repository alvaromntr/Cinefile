package com.example.cinefile.Service;


import com.example.cinefile.Domain.Avaliacao.Avaliacao;
import com.example.cinefile.Domain.Avaliacao.AvaliacaoRepository;
import com.example.cinefile.Domain.Avaliacao.RequestAvaliacao;
import com.example.cinefile.Domain.Obra.Obra;
import com.example.cinefile.Domain.Obra.ObraRepository;
import com.example.cinefile.Domain.Temporadas.Temporada;
import com.example.cinefile.Domain.Temporadas.TemporadaRepository;
import com.example.cinefile.Domain.Usuario.UserRole;
import com.example.cinefile.Domain.Usuario.Usuario;
import com.example.cinefile.DTO.AvaliacaoResponseDTO;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;
import java.util.UUID;

@Service
public class AvaliacaoService {

    @Autowired private AvaliacaoRepository avaliacaoRepository;
    @Autowired private ObraRepository obraRepository;
    @Autowired private TemporadaRepository temporadaRepository;

    public AvaliacaoResponseDTO criarAvaliacao(RequestAvaliacao dto, Usuario usuarioLogado) {
        Obra obra = obraRepository.findById(dto.obraId())
                .orElseThrow(() -> new EntityNotFoundException("Obra não encontrada."));

        Temporada temporada = null;
        if (dto.temporadaId() != null) {
            temporada = temporadaRepository.findById(dto.temporadaId())
                    .orElseThrow(() -> new EntityNotFoundException("Temporada não encontrada."));
        }

        Avaliacao novaAvaliacao = new Avaliacao(dto, obra, temporada, usuarioLogado);
        avaliacaoRepository.save(novaAvaliacao);

        return new AvaliacaoResponseDTO(novaAvaliacao);
    }

    public AvaliacaoResponseDTO atualizarAvaliacao(UUID avaliacaoId, RequestAvaliacao dto, Usuario usuarioLogado) {
        Avaliacao avaliacao = avaliacaoRepository.findById(avaliacaoId)
                .orElseThrow(() -> new EntityNotFoundException("Avaliação não encontrada."));

        // REGRA DE SEGURANÇA: Só o dono da avaliação pode editar.
        if (!avaliacao.getUsuario().getId().equals(usuarioLogado.getId())) {
            throw new AccessDeniedException("Você não tem permissão para editar esta avaliação.");
        }

        if (dto.nota() != null) avaliacao.setNota(dto.nota());
        if (dto.comentario() != null) avaliacao.setComentario(dto.comentario());

        avaliacaoRepository.save(avaliacao);
        return new AvaliacaoResponseDTO(avaliacao);
    }

    public void deletarAvaliacao(UUID avaliacaoId, Usuario usuarioLogado) {
        Avaliacao avaliacao = avaliacaoRepository.findById(avaliacaoId)
                .orElseThrow(() -> new EntityNotFoundException("Avaliação não encontrada."));

        // REGRA DE SEGURANÇA: Só o dono ou um ADMIN pode deletar.
        if (!avaliacao.getUsuario().getId().equals(usuarioLogado.getId()) && usuarioLogado.getRole() != UserRole.ADMIN) {
            throw new AccessDeniedException("Você não tem permissão para deletar esta avaliação.");
        }

        avaliacaoRepository.delete(avaliacao);
    }
}
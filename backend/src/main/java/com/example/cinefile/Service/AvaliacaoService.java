package com.example.cinefile.Service;

import com.example.cinefile.DTO.AvaliacaoDTO;
import com.example.cinefile.Domain.Avaliacao.Avaliacao;
import com.example.cinefile.Domain.Avaliacao.AvaliacaoRepository;
import com.example.cinefile.Domain.Obra.Obra;
import com.example.cinefile.Domain.Obra.ObraRepository;
import com.example.cinefile.Domain.Usuario.Usuario;
import com.example.cinefile.Domain.Usuario.UsuarioRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class AvaliacaoService {

    @Autowired
    private AvaliacaoRepository avaliacaoRepository;

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Autowired
    private ObraRepository obraRepository;

    // ==========================================================
    // CRIAR OU ATUALIZAR AVALIAÇÃO
    // ==========================================================
    public AvaliacaoDTO criarAvaliacao(AvaliacaoDTO dto) {
        // Pega o usuário autenticado
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String emailUsuario = authentication.getName();

        Usuario usuario = usuarioRepository.findByEmail(emailUsuario)
                .orElseThrow(() -> new EntityNotFoundException("Usuário não encontrado"));

        Obra obra = obraRepository.findById(dto.obraId())
                .orElseThrow(() -> new EntityNotFoundException("Obra não encontrada"));

        // Verifica se já existe avaliação desse usuário para essa obra
        Avaliacao avaliacaoExistente = avaliacaoRepository.findByObraAndUsuario(obra, usuario).orElse(null);
        if (avaliacaoExistente != null) {
            avaliacaoExistente.setNota(dto.nota());
            avaliacaoRepository.save(avaliacaoExistente);
            return new AvaliacaoDTO(
                    avaliacaoExistente.getId(),
                    avaliacaoExistente.getNota(),
                    obra.getObraid(),
                    usuario.getId()
            );
        }

        // Cria nova avaliação
        Avaliacao nova = new Avaliacao();
        nova.setNota(dto.nota());
        nova.setObra(obra);
        nova.setUsuario(usuario);

        Avaliacao salva = avaliacaoRepository.save(nova);

        return new AvaliacaoDTO(
                salva.getId(),
                salva.getNota(),
                obra.getObraid(),
                usuario.getId()
        );
    }

    // ==========================================================
    // LISTAR AVALIAÇÕES POR USUÁRIO
    // ==========================================================
    public List<AvaliacaoDTO> listarAvaliacoesPorUsuario(UUID usuarioId) {
        Usuario usuario = usuarioRepository.findById(usuarioId)
                .orElseThrow(() -> new EntityNotFoundException("Usuário não encontrado"));

        return avaliacaoRepository.findAllByUsuario(usuario).stream()
                .map(a -> new AvaliacaoDTO(
                        a.getId(),
                        a.getNota(),
                        a.getObra().getObraid(),
                        usuario.getId()
                ))
                .collect(Collectors.toList());
    }
}

package com.example.cinefile.Service;

import com.example.cinefile.Domain.Comentario.Comentario;
import com.example.cinefile.Domain.Comentario.ComentarioRepository;
import com.example.cinefile.Domain.Obra.Obra;
import com.example.cinefile.Domain.Obra.ObraRepository;
import com.example.cinefile.Domain.Usuario.UserRole;
import com.example.cinefile.Domain.Usuario.Usuario;
import com.example.cinefile.DTO.ComentarioRequestDTO;
import com.example.cinefile.DTO.ComentarioResponseDTO;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class ComentarioService {

    @Autowired
    private ComentarioRepository comentarioRepository;
    @Autowired
    private ObraRepository obraRepository;

    public List<ComentarioResponseDTO> listarComentariosPorObra(Long obraid) {
        return comentarioRepository.findByObraObraidOrderByDatacomentarioDesc(obraid)
                .stream()
                .map(ComentarioResponseDTO::new)
                .collect(Collectors.toList());
    }

    public ComentarioResponseDTO criarComentario(Long obraId, ComentarioRequestDTO dto, Usuario usuarioLogado) {
        Obra obra = obraRepository.findById(obraId)
                .orElseThrow(() -> new EntityNotFoundException("Obra não encontrada"));

        Comentario comentario = new Comentario(dto.texto(), usuarioLogado, obra);
        comentarioRepository.save(comentario);
        return new ComentarioResponseDTO(comentario);
    }

    public void deletarComentario(Long comentarioId, Usuario usuarioLogado) {
        Comentario comentario = comentarioRepository.findById(comentarioId)
                .orElseThrow(() -> new EntityNotFoundException("Comentário não encontrado"));

        // Permite deletar se o usuário for o dono do comentário OU se for ADMIN
        if (!comentario.getUsuario().getId().equals(usuarioLogado.getId()) && !usuarioLogado.getRole().equals(UserRole.ADMIN)) {
            throw new AccessDeniedException("Você não tem permissão para deletar este comentário.");
        }

        comentarioRepository.delete(comentario);
    }
}
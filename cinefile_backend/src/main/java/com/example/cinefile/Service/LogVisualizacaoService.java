package com.example.cinefile.Service;

import com.example.cinefile.Domain.Logs.LogVisualizacao;
import com.example.cinefile.Domain.Logs.LogVisualizacaoRepository;
import com.example.cinefile.Domain.Obra.Obra;
import com.example.cinefile.Domain.Obra.ObraRepository;
import com.example.cinefile.Domain.Usuario.Usuario;
import com.example.cinefile.DTO.LogVisualizacaoRequestDTO;
import com.example.cinefile.DTO.LogVisualizacaoResponseDTO;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class LogVisualizacaoService {

    private final LogVisualizacaoRepository logRepository;
    private final ObraRepository obraRepository;

    public LogVisualizacaoService(LogVisualizacaoRepository logRepository, ObraRepository obraRepository) {
        this.logRepository = logRepository;
        this.obraRepository = obraRepository;
    }

    public LogVisualizacaoResponseDTO registrarLog(LogVisualizacaoRequestDTO dto, Usuario usuario) {
        Obra obra = obraRepository.findById(dto.obraId())
                .orElseThrow(() -> new EntityNotFoundException("Obra não encontrada"));

        LogVisualizacao log = new LogVisualizacao();
        log.setUsuario(usuario);
        log.setObra(obra);

        logRepository.save(log);
        return new LogVisualizacaoResponseDTO(log);
    }

    public List<LogVisualizacaoResponseDTO> listarLogsDoUsuario(Usuario usuario) {
        return logRepository.findByUsuarioId(usuario.getId())
                .stream()
                .map(LogVisualizacaoResponseDTO::new)
                .collect(Collectors.toList());
    }

    public void deletarLog(UUID logId, Usuario usuario) {
        LogVisualizacao log = logRepository.findById(logId)
                .orElseThrow(() -> new EntityNotFoundException("Log não encontrado"));

        if (!log.getUsuario().getId().equals(usuario.getId())) {
            throw new AccessDeniedException("Você não tem permissão para deletar este log.");
        }

        logRepository.delete(log);
    }
}

package com.example.cinefile.servico;

import com.example.cinefile.modelo.entidades.Assistido;
import com.example.cinefile.modelo.entidades.Obra;
import com.example.cinefile.modelo.entidades.Usuario;
import com.example.cinefile.repositorio.AssistidoRepository;
import com.example.cinefile.repositorio.ObraRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class AssistidoService {

    private final AssistidoRepository assistidoRepository;
    private final ObraRepository obraRepository;

    public List<Obra> listarObrasAssistidas(Usuario usuario) {
        return assistidoRepository.findByUsuario(usuario).stream()
                .map(Assistido::getObra)
                .toList();
    }

    public void marcarComoAssistido(Long obraId, Usuario usuario) {
        if (assistidoRepository.existsByUsuarioAndObraId(usuario, obraId)) {
            return; // Já está marcado como assistido
        }

        Obra obra = buscarObraPorId(obraId);
        Assistido assistido = new Assistido(obra, usuario);
        assistidoRepository.save(assistido);
    }

    public void removerComoAssistido(Long obraId, Usuario usuario) {
        assistidoRepository.deleteByUsuarioAndObraId(usuario, obraId);
    }

    private Obra buscarObraPorId(Long obraId) {
        return obraRepository.findById(obraId)
                .orElseThrow(() -> new RuntimeException("Obra não encontrada"));
    }
}
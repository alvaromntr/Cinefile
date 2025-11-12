package com.example.cinefile.Service;

import com.example.cinefile.DTO.CategoriaDTO;
import com.example.cinefile.DTO.ObraDTO;
import com.example.cinefile.Domain.Categoria.Categoria;
import com.example.cinefile.Domain.Categoria.CategoriaRepository;
import com.example.cinefile.Domain.Obra.Obra;
import com.example.cinefile.Domain.Obra.ObraRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ObraService {

    private final ObraRepository obraRepository;
    private final CategoriaRepository categoriaRepository;

    public ObraService(ObraRepository obraRepository, CategoriaRepository categoriaRepository) {
        this.obraRepository = obraRepository;
        this.categoriaRepository = categoriaRepository;
    }

    // -------- Converter entidade para DTO --------
    public ObraDTO mapToDTO(Obra obra) {
        List<CategoriaDTO> categoriasDTO = obra.getCategorias()
                .stream()
                .map(c -> new CategoriaDTO(c.getCategoriaid(), c.getNome()))
                .toList();

        return new ObraDTO(
                obra.getObraid(),
                obra.getTmdbId(),
                obra.getTitulo(),
                obra.getDescricao(),
                obra.getTipo(),
                obra.getAnolancamento(),
                obra.getPoster_url(),
                obra.getDuracao(),
                categoriasDTO
        );
    }

    // -------- Adicionar categorias à obra --------
    public Obra adicionarCategorias(Long obraId, List<Long> categoriaIds) {
        Obra obra = obraRepository.findById(obraId)
                .orElseThrow(() -> new RuntimeException("Obra não encontrada"));

        List<Categoria> categorias = categoriaRepository.findAllById(categoriaIds);
        obra.getCategorias().clear(); // substitui categorias anteriores
        obra.getCategorias().addAll(categorias);

        return obraRepository.save(obra);
    }

    // -------- Buscar catálogo completo --------
    public List<ObraDTO> buscarCatalogo() {
        List<Obra> obras = obraRepository.findAll();
        return obras.stream()
                .map(this::mapToDTO)
                .toList();
    }

    // -------- Buscar obra por ID --------
    public Optional<ObraDTO> buscarPorId(Long id) {
        return obraRepository.findById(id).map(this::mapToDTO);
    }
}

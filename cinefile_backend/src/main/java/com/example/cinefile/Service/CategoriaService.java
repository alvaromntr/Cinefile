package com.example.cinefile.Service;


import com.example.cinefile.DTO.CategoriaDTO;
import com.example.cinefile.Domain.Categoria.Categoria;
import com.example.cinefile.Domain.Categoria.CategoriaRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class CategoriaService {

    @Autowired
    private CategoriaRepository categoriaRepository;

    public CategoriaDTO criarCategoria(CategoriaDTO categoriaDTO) {
        Categoria categoria = new Categoria(categoriaDTO.nome());
        categoriaRepository.save(categoria);
        return new CategoriaDTO(categoria.getCategoriaid(), categoria.getNome());
    }

    public List<CategoriaDTO> listarCategorias() {
        return categoriaRepository.findAll().stream()
                .map(cat -> new CategoriaDTO(cat.getCategoriaid(), cat.getNome()))
                .collect(Collectors.toList());
    }

    public CategoriaDTO atualizarCategoria(Long id, CategoriaDTO categoriaDTO) {
        Categoria categoria = categoriaRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Categoria não encontrada com id: " + id));
        categoria.setNome(categoriaDTO.nome());
        categoriaRepository.save(categoria);
        return new CategoriaDTO(categoria.getCategoriaid(), categoria.getNome());
    }

    public void deletarCategoria(Long id) {
        if (!categoriaRepository.existsById(id)) {
            throw new EntityNotFoundException("Categoria não encontrada com id: " + id);
        }
        categoriaRepository.deleteById(id);
    }
}
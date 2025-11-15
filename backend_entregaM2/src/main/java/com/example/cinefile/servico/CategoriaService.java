package com.example.cinefile.servico;

import com.example.cinefile.modelo.dtos.RespostaCategoria;
import com.example.cinefile.modelo.entidades.Categoria;
import com.example.cinefile.repositorio.CategoriaRepository;
import com.example.cinefile.shared.excecoes.EntidadeNaoEncontradaException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CategoriaService {

    private final CategoriaRepository categoriaRepository;

    public RespostaCategoria criarCategoria(RespostaCategoria categoriaDTO) {
        Categoria categoria = new Categoria(categoriaDTO.nome());
        categoriaRepository.save(categoria);
        return new RespostaCategoria(categoria.getId(), categoria.getNome());
    }

    public List<RespostaCategoria> listarCategorias() {
        return categoriaRepository.findAll().stream()
                .map(cat -> new RespostaCategoria(cat.getId(), cat.getNome()))
                .toList();
    }

    public RespostaCategoria atualizarCategoria(Long id, RespostaCategoria categoriaDTO) {
        Categoria categoria = buscarCategoriaPorId(id);
        categoria.setNome(categoriaDTO.nome());
        categoriaRepository.save(categoria);
        return new RespostaCategoria(categoria.getId(), categoria.getNome());
    }

    public void deletarCategoria(Long id) {
        if (!categoriaRepository.existsById(id)) {
            throw new EntidadeNaoEncontradaException("Categoria não encontrada com id: " + id);
        }
        categoriaRepository.deleteById(id);
    }

    private Categoria buscarCategoriaPorId(Long id) {
        return categoriaRepository.findById(id)
                .orElseThrow(() -> new EntidadeNaoEncontradaException("Categoria não encontrada com id: " + id));
    }
}
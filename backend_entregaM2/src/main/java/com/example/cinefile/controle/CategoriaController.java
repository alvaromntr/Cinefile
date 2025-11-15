package com.example.cinefile.controle;

import com.example.cinefile.modelo.dtos.RespostaCategoria;
import com.example.cinefile.servico.CategoriaService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/categorias")
@RequiredArgsConstructor
public class CategoriaController {

    private final CategoriaService categoriaService;

    @PostMapping
    public ResponseEntity<RespostaCategoria> criarCategoria(@Valid @RequestBody RespostaCategoria categoriaDTO) {
        RespostaCategoria novaCategoria = categoriaService.criarCategoria(categoriaDTO);
        return new ResponseEntity<>(novaCategoria, HttpStatus.CREATED);
    }

    @GetMapping
    public ResponseEntity<List<RespostaCategoria>> listarCategorias() {
        List<RespostaCategoria> categorias = categoriaService.listarCategorias();
        return ResponseEntity.ok(categorias);
    }

    @PutMapping("/{id}")
    public ResponseEntity<RespostaCategoria> atualizarCategoria(
            @PathVariable Long id,
            @Valid @RequestBody RespostaCategoria categoriaDTO) {

        RespostaCategoria categoriaAtualizada = categoriaService.atualizarCategoria(id, categoriaDTO);
        return ResponseEntity.ok(categoriaAtualizada);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletarCategoria(@PathVariable Long id) {
        categoriaService.deletarCategoria(id);
        return ResponseEntity.noContent().build();
    }
}
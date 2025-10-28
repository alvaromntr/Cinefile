package com.example.cinefile.Controller;

import com.example.cinefile.Service.ObraService;
import com.example.cinefile.DTO.ObraDTO;
import com.example.cinefile.Domain.Obra.Obra;
import com.example.cinefile.Domain.Obra.ObraRepository;
import com.example.cinefile.Domain.Obra.RequestObra;

import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@CrossOrigin(
        origins = {"http://127.0.0.1:5500", "http://localhost:5500"},
        allowCredentials = "true"
)
@RestController
@RequestMapping("/api/obras")
public class ObraController {

    private final ObraRepository obraRepository;
    private final ObraService obraService;

    public ObraController(ObraRepository obraRepository, ObraService obraService) {
        this.obraRepository = obraRepository;
        this.obraService = obraService;
    }

    // -------- GET: todas as obras --------
    @GetMapping
    public ResponseEntity<List<Obra>> getAllObras() {
        return ResponseEntity.ok(obraRepository.findAll());
    }

    // -------- GET: obra por ID --------
    @GetMapping("/{id}")
    public ResponseEntity<Obra> getObraById(@PathVariable Long id) {
        return obraRepository.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    // -------- POST: criar nova obra --------
    @PostMapping
    public ResponseEntity<Obra> saveObra(@RequestBody @Valid RequestObra data) {
        Obra newObra = new Obra(data);
        obraRepository.save(newObra);
        return ResponseEntity.ok(newObra);
    }

    // -------- PUT: atualizar obra --------
    @PutMapping("/{id}")
    @Transactional
    public ResponseEntity<Obra> updateObra(@PathVariable Long id, @RequestBody RequestObra data) {
        Optional<Obra> optionalObra = obraRepository.findById(id);
        if (optionalObra.isEmpty()) {
            return ResponseEntity.notFound().build();
        }

        Obra obra = optionalObra.get();
        if (data.titulo() != null) obra.setTitulo(data.titulo());
        if (data.descricao() != null) obra.setDescricao(data.descricao());
        if (data.tipo() != null) obra.setTipo(data.tipo());
        if (data.anolancamento() != null) obra.setAnolancamento(data.anolancamento());
        if (data.poster_url() != null) obra.setPoster_url(data.poster_url());
        if (data.duracao() != null) obra.setDuracao(data.duracao());

        return ResponseEntity.ok(obra);
    }

    // -------- DELETE: excluir obra --------
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteObra(@PathVariable Long id) {
        Optional<Obra> optionalObra = obraRepository.findById(id);
        if (optionalObra.isEmpty()) {
            return ResponseEntity.notFound().build();
        }

        obraRepository.delete(optionalObra.get());
        return ResponseEntity.noContent().build();
    }

    // -------- PUT: adicionar categorias a uma obra --------
    @PutMapping("/{obraId}/categorias")
    public ResponseEntity<ObraDTO> adicionarCategorias(
            @PathVariable Long obraId,
            @RequestBody List<Long> categoriaIds
    ) {
        Obra obraAtualizada = obraService.adicionarCategorias(obraId, categoriaIds);
        return ResponseEntity.ok(obraService.mapToDTO(obraAtualizada));
    }
}

package com.example.cinefile.Controller;

import com.example.cinefile.Domain.Obra.ObraTipo;
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

@RestController
@RequestMapping("/api/obras")
@CrossOrigin(origins = {"http://127.0.0.1:5500", "http://localhost:5500"}, allowCredentials = "true")
public class ObraController {

    private final ObraRepository obraRepository;
    private final ObraService obraService;

    public ObraController(ObraRepository obraRepository, ObraService obraService) {
        this.obraRepository = obraRepository;
        this.obraService = obraService;
    }

    // GET catálogo completo ou filtrado por tipo
    @GetMapping
    public ResponseEntity<List<ObraDTO>> getAllObras(@RequestParam(required = false) String tipo) {
        List<Obra> obras;
        if (tipo != null && !tipo.isBlank()) {
            try {
                ObraTipo t = ObraTipo.valueOf(tipo.toUpperCase());
                obras = obraRepository.findByTipo(t);
            } catch (IllegalArgumentException e) {
                return ResponseEntity.badRequest().build();
            }
        } else {
            obras = obraRepository.findAll();
        }
        List<ObraDTO> dtos = obras.stream().map(obraService::mapToDTO).toList();
        return ResponseEntity.ok(dtos);
    }

    // GET obra por ID
    @GetMapping("/{id}")
    public ResponseEntity<ObraDTO> getObraById(@PathVariable Long id) {
        return obraService.buscarPorId(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<Obra> saveObra(@RequestBody @Valid RequestObra data) {
        Obra newObra = new Obra(data);
        obraRepository.save(newObra);
        return ResponseEntity.ok(newObra);
    }

    @PutMapping("/{id}")
    @Transactional
    public ResponseEntity<Obra> updateObra(@PathVariable Long id, @RequestBody RequestObra data) {
        Optional<Obra> optionalObra = obraRepository.findById(id);
        if (optionalObra.isEmpty()) return ResponseEntity.notFound().build();

        Obra obra = optionalObra.get();
        if (data.titulo() != null) obra.setTitulo(data.titulo());
        if (data.descricao() != null) obra.setDescricao(data.descricao());
        if (data.tipo() != null) obra.setTipo(data.tipo());
        if (data.anolancamento() != null) obra.setAnolancamento(data.anolancamento());
        if (data.poster_url() != null) obra.setPoster_url(data.poster_url());
        if (data.duracao() != null) obra.setDuracao(data.duracao());

        return ResponseEntity.ok(obra);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteObra(@PathVariable Long id) {
        if (!obraRepository.existsById(id)) {
            return ResponseEntity.notFound().build();
        }
        obraRepository.deleteById(id);
        return ResponseEntity.noContent().build();
    }



    @PutMapping("/{obraId}/categorias")
    public ResponseEntity<ObraDTO> adicionarCategorias(
            @PathVariable Long obraId,
            @RequestBody List<Long> categoriaIds
    ) {
        Obra obraAtualizada = obraService.adicionarCategorias(obraId, categoriaIds);
        return ResponseEntity.ok(obraService.mapToDTO(obraAtualizada));
    }
}

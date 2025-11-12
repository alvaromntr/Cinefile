package com.example.cinefile.Controller;

import com.example.cinefile.DTO.TemporadaDTO;
import com.example.cinefile.Domain.Obra.Obra;
import com.example.cinefile.Domain.Obra.ObraRepository;
import com.example.cinefile.Domain.Temporadas.RequestTemporada;
import com.example.cinefile.Domain.Temporadas.Temporada;
import com.example.cinefile.Domain.Temporadas.TemporadaRepository;
import com.example.cinefile.Service.TemporadaService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;
import java.util.UUID;


@RequestMapping("/api/obras/{obraId}/temporadas")
public class TemporadaController {

    @Autowired
    private TemporadaService temporadaService;

    @Autowired
    private TemporadaRepository temporadaRepository;

    @Autowired
    private ObraRepository obraRepository;

    @GetMapping
    public ResponseEntity<List<TemporadaDTO>> listarTemporadasDaObra(@PathVariable Long obraid) {
        return ResponseEntity.ok(temporadaService.listarTemporadasPorObra(obraid));
    }

    @PostMapping
    public ResponseEntity<TemporadaDTO> criarTemporada(
            @PathVariable Long obraid,
            @RequestBody @Valid TemporadaDTO data
    ) {
        TemporadaDTO novaTemporada = temporadaService.criarTemporada(obraid, data);
        return ResponseEntity.ok(novaTemporada);
    }

    @GetMapping("/todas")
    public ResponseEntity<List<Temporada>> listarTemporadas() {
        return ResponseEntity.ok(temporadaRepository.findAll());
    }

    @GetMapping("/obra/{obraId}")
    public ResponseEntity<List<Temporada>> listarPorObra(@PathVariable Long obraid) {
        return ResponseEntity.ok(temporadaRepository.findByObraObraid(obraid));
    }

    @GetMapping("/{id}")
    public ResponseEntity<Temporada> buscarPorId(@PathVariable UUID id) {
        Optional<Temporada> temporada = temporadaRepository.findById(id);
        return temporada.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PutMapping("/{id}")
    public ResponseEntity<Temporada> atualizarTemporada(@PathVariable UUID id, @RequestBody @Valid RequestTemporada data) {
        Optional<Temporada> optionalTemporada = temporadaRepository.findById(id);
        Optional<Obra> optionalObra = obraRepository.findById(data.obraId());

        if (optionalTemporada.isEmpty() || optionalObra.isEmpty()) {
            return ResponseEntity.notFound().build();
        }

        Temporada temporada = optionalTemporada.get();
        temporada.setNumero(data.numero());
        temporada.setQuantidadeEpisodios(data.quantidadeEpisodios());
        temporada.setDescricao(data.descricao());
        temporada.setObra(optionalObra.get());

        temporadaRepository.save(temporada);

        return ResponseEntity.ok(temporada);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletarTemporada(@PathVariable UUID id) {
        Optional<Temporada> temporada = temporadaRepository.findById(id);

        if (temporada.isPresent()) {
            temporadaRepository.delete(temporada.get());
            return ResponseEntity.noContent().build();
        }

        return ResponseEntity.notFound().build();
    }
}

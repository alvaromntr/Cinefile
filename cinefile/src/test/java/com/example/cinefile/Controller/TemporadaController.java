package com.example.cinefile.Controller;

import com.example.cinefile.Domain.Usuario.TemporadaDomain.TemporadaRepository;
import com.example.cinefile.Domain.Usuario.ObraDomain.Obra;
import com.example.cinefile.Domain.Usuario.ObraDomain.ObraRepository;
import com.example.cinefile.Domain.Usuario.TemporadaDomain.RequestTemporada;
import com.example.cinefile.Domain.Usuario.TemporadaDomain.Temporada;

import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@RestController
@RequestMapping("/temporadas")
public class TemporadaController {

    @Autowired
    private TemporadaRepository temporadaRepository;

    @Autowired
    private ObraRepository obraRepository;

    @PostMapping
    public ResponseEntity<Temporada> criarTemporada(@RequestBody @Valid RequestTemporada data) {
        Optional<Obra> optionalObra = obraRepository.findById(data.obraId());

        if (optionalObra.isEmpty()) {
            return ResponseEntity.badRequest().build();
        }

        Temporada novaTemporada = new Temporada(data, optionalObra.get());
        temporadaRepository.save(novaTemporada);

        return ResponseEntity.ok(novaTemporada);
    }

    @GetMapping
    public ResponseEntity<List<Temporada>> listarTemporadas() {
        return ResponseEntity.ok(temporadaRepository.findAll());
    }

    @GetMapping("/obra/{obraId}")
    public ResponseEntity<List<Temporada>> listarPorObra(@PathVariable UUID obraId) {
        return ResponseEntity.ok(temporadaRepository.findByObraObra_id(obraId));
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


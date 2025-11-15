package com.example.cinefile.controle;

import com.example.cinefile.modelo.dtos.RespostaTemporada;
import com.example.cinefile.modelo.entidades.Temporada;
import com.example.cinefile.servico.TemporadaService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/obras/{obraId}/temporadas")
@RequiredArgsConstructor
public class TemporadaController {

    private final TemporadaService temporadaService;

    @GetMapping
    public ResponseEntity<List<RespostaTemporada>> listarTemporadasDaObra(@PathVariable Long obraId) {
        return ResponseEntity.ok(temporadaService.listarTemporadasPorObra(obraId));
    }

    @PostMapping
    public ResponseEntity<RespostaTemporada> criarTemporada(
            @PathVariable Long obraId,
            @RequestBody @Valid RespostaTemporada dados) {

        RespostaTemporada novaTemporada = temporadaService.criarTemporada(obraId, dados);
        return ResponseEntity.ok(novaTemporada);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Temporada> buscarPorId(@PathVariable UUID id) {
        Temporada temporada = temporadaService.buscarPorId(id);
        return temporada != null ? ResponseEntity.ok(temporada) : ResponseEntity.notFound().build();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletarTemporada(@PathVariable UUID id) {
        temporadaService.deletarTemporada(id);
        return ResponseEntity.noContent().build();
    }
}
package com.example.cinefile.Controller;


import com.example.cinefile.AvaliacaoDomain.Avaliacao;
import com.example.cinefile.AvaliacaoDomain.AvaliacaoRepository;
import com.example.cinefile.AvaliacaoDomain.RequestAvaliacao;
import com.example.cinefile.Domain.Usuario.ObraDomain.Obra;
import com.example.cinefile.Domain.Usuario.ObraDomain.ObraRepository;
import com.example.cinefile.Domain.Usuario.Usuario;
import com.example.cinefile.Domain.Usuario.UsuarioRepository;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@RestController
@RequestMapping("/avaliacoes")
public class AvaliacaoController {

    @Autowired
    private AvaliacaoRepository avaliacaoRepository;

    @Autowired
    private ObraRepository obraRepository;

    @Autowired
    private UsuarioRepository usuarioRepository;

    @GetMapping
    public ResponseEntity<List<Avaliacao>> getAllAvaliacoes() {
        return ResponseEntity.ok(avaliacaoRepository.findAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<Avaliacao> getAvaliacaoById(@PathVariable UUID id) {
        Optional<Avaliacao> avaliacao = avaliacaoRepository.findById(id);
        return avaliacao.map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<Avaliacao> createAvaliacao(@RequestBody @Valid RequestAvaliacao data) {
        Optional<Obra> optionalObra = obraRepository.findById(data.obraId());
        Optional<Usuario> optionalUsuario = usuarioRepository.findById(data.usuarioId());

        if (optionalObra.isEmpty() || optionalUsuario.isEmpty()) {
            return ResponseEntity.badRequest().build();
        }

        Avaliacao novaAvaliacao = new Avaliacao(data, optionalObra.get(), optionalUsuario.get());
        avaliacaoRepository.save(novaAvaliacao);

        return ResponseEntity.ok(novaAvaliacao);
    }

    @PutMapping("/{id}")
    @Transactional
    public ResponseEntity<Avaliacao> updateAvaliacao(@PathVariable UUID id, @RequestBody @Valid RequestAvaliacao data) {
        Optional<Avaliacao> optionalAvaliacao = avaliacaoRepository.findById(id);
        if (optionalAvaliacao.isPresent()) {
            Avaliacao avaliacao = optionalAvaliacao.get();
            if (data.nota() != null) avaliacao.setNota(data.nota());
            if (data.comentario() != null) avaliacao.setComentario(data.comentario());
            return ResponseEntity.ok(avaliacao);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteAvaliacao(@PathVariable UUID id) {
        Optional<Avaliacao> optionalAvaliacao = avaliacaoRepository.findById(id);
        if (optionalAvaliacao.isPresent()) {
            avaliacaoRepository.delete(optionalAvaliacao.get());
            return ResponseEntity.noContent().build();
        } else {
            return ResponseEntity.notFound().build();
        }
    }
}

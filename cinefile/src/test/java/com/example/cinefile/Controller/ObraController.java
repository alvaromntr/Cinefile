package com.example.cinefile.Controller;

import com.example.cinefile.Domain.Usuario.ObraDomain.Obra;
import com.example.cinefile.Domain.Usuario.ObraDomain.ObraRepository;
import com.example.cinefile.Domain.Usuario.ObraDomain.RequestObra;
import jakarta.persistence.EntityNotFoundException;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import java.util.Optional;
import java.util.UUID;

@RestController
@RequestMapping("/obras")
public class ObraController {

    @Autowired
    private ObraRepository obraRepository;

    @GetMapping
    public ResponseEntity getAllObras() {
        var allObras = obraRepository.findAll();
        return ResponseEntity.ok(allObras);
    }
    @GetMapping("/{id}")
    public ResponseEntity<Obra> getobraById(@PathVariable UUID id) {
        Optional<Obra> optionalObra = obraRepository.findById(id);
        if (optionalObra.isPresent()) {
            return ResponseEntity.ok(optionalObra.get());
        } else {
            return ResponseEntity.notFound().build();
        }
    }
    @PostMapping
    public ResponseEntity saveObra(@RequestBody @Valid RequestObra data) {
        Obra newObra = new Obra(data);
        obraRepository.save(newObra);
        return ResponseEntity.ok(newObra);
    }

    @PutMapping("/{id}")
    @Transactional
    public ResponseEntity<Obra> updateObra(
            @PathVariable UUID id,
            @RequestBody RequestObra data) {

        Optional<Obra> optionalObra = obraRepository.findById(id);

        if (optionalObra.isPresent()) {
            Obra obra = optionalObra.get();

            // Atualiza apenas os campos que vierem preenchidos
            if (data.titulo() != null) obra.setTitulo(data.titulo());
            if (data.descricao() != null) obra.setDescricao(data.descricao());
            if (data.tipo() != null) obra.setTipo(data.tipo());
            if (data.ano_lancamento() != null) obra.setAno_lancamento(data.ano_lancamento());
            if (data.poster_url() != null) obra.setPoster_url(data.poster_url());
            if (data.duracao() != null) obra.setDuracao(data.duracao());

            obraRepository.save(obra); // salva alterações no banco

            return ResponseEntity.ok(obra);
        } else {
            return ResponseEntity.notFound().build();
        }
    }


    @DeleteMapping("/{id}")
    public ResponseEntity deleteObra(@PathVariable UUID id) {
        Optional<Obra> optionalObra = obraRepository.findById(id);
        if (optionalObra.isPresent()) {
            Obra obra = optionalObra.get();
            obraRepository.delete(obra);
            return ResponseEntity.noContent().build();
        } else {
            throw new EntityNotFoundException();
        }
    }
}

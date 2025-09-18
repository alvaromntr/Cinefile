package com.example.cinefile.Controller;

import com.example.cinefile.Domain.Obras.Obra;
import com.example.cinefile.Domain.Obras.ObraRepository;
import com.example.cinefile.Domain.Obras.RequestObra;
import jakarta.persistence.EntityNotFoundException;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import java.util.Optional;

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

    @PostMapping
    public ResponseEntity saveObra(@RequestBody @Valid RequestObra data) {
        Obra newObra = new Obra(data);
        obraRepository.save(newObra);
        return ResponseEntity.ok(newObra);
    }

    @PutMapping("/{id}")
    @Transactional
    public ResponseEntity updateObra(@PathVariable Long id, @RequestBody @Valid RequestObra data) {
        Optional<Obra> optionalObra = obraRepository.findById(id);
        if (optionalObra.isPresent()) {
            Obra obra = optionalObra.get();
            obra.setTitulo(data.titulo());
            obra.setDescricao(data.descricao());
            obra.setTipo(data.tipo());
            obra.setAno_lancamento(data.ano_lancamento());
            obra.setPoster_url(data.poster_url());
            obra.setDuracao(data.duracao());
            return ResponseEntity.ok(obra);
        } else {
            throw new EntityNotFoundException();
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity deleteObra(@PathVariable Long id) {
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

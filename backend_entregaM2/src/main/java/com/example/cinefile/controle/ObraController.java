package com.example.cinefile.controle;

import com.example.cinefile.modelo.dtos.RespostaObra;
import com.example.cinefile.modelo.enums.TipoObra;
import com.example.cinefile.servico.CatalogoObraService;
import com.example.cinefile.shared.excecoes.TipoObraInvalidoException;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/obras")
@RequiredArgsConstructor
public class ObraController {

    private final CatalogoObraService catalogoObraService;

    @GetMapping
    public ResponseEntity<List<RespostaObra>> listarObras(@RequestParam(required = false) String tipo) {
        List<RespostaObra> obras = obterObrasFiltradas(tipo);
        return ResponseEntity.ok(obras);
    }

    @GetMapping("/{id}")
    public ResponseEntity<RespostaObra> buscarObraPorId(@PathVariable Long id) {
        RespostaObra obra = catalogoObraService.buscarObraPorId(id);
        return ResponseEntity.ok(obra);
    }

    private List<RespostaObra> obterObrasFiltradas(String tipo) {
        if (tipo != null && !tipo.isBlank()) {
            return filtrarObrasPorTipo(tipo);
        }
        return catalogoObraService.buscarTodasObras();
    }

    private List<RespostaObra> filtrarObrasPorTipo(String tipo) {
        try {
            TipoObra tipoObra = TipoObra.valueOf(tipo.toUpperCase());
            return catalogoObraService.listarObrasPorTipo(tipoObra);
        } catch (IllegalArgumentException e) {
            throw new TipoObraInvalidoException(tipo);
        }
    }
}
package com.example.cinefile.Controller;

import com.example.cinefile.DTO.AvaliacaoDTO;
import com.example.cinefile.Service.AvaliacaoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/avaliacoes")
public class AvaliacaoController {

    @Autowired
    private AvaliacaoService avaliacaoService;

    @PostMapping
    public AvaliacaoDTO criarAvaliacao(@RequestBody AvaliacaoDTO dto) {
        return avaliacaoService.criarAvaliacao(dto);
    }

    @GetMapping("/usuario/{usuarioid}")
    public List<AvaliacaoDTO> listarPorUsuario(@PathVariable UUID usuarioid) {
        return avaliacaoService.listarAvaliacoesPorUsuario(usuarioid);
    }
}

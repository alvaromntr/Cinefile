package controller;

import com.example.cinefile.Service.AvaliacaoService;
import domain.Avaliacao_domain;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/avaliacoes")
public class AvaliacaoController {

    @Autowired
    private AvaliacaoService avaliacaoService;

    // POST /avaliacoes
    @PostMapping
    public Avaliacao_domain cadastrar(@RequestBody Avaliacao_domain avaliacao) {
        return avaliacaoService.salvarAvaliacao(avaliacao);
    }

    // GET /avaliacoes
    @GetMapping
    public List<Avaliacao_domain> listar() {
        return avaliacaoService.listarAvaliacoes();
    }
}

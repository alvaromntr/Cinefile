package com.example.cinefile.controle;

import com.example.cinefile.servico.TMDBSincronizacaoService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/tmdb")
@RequiredArgsConstructor
public class TMDBSincronizacaoController {

    private final TMDBSincronizacaoService tmdbSincronizacaoService;

    @PostMapping("/importar/filmes")
    public String importarFilmes(@RequestParam(defaultValue = "2") int paginas) {
        tmdbSincronizacaoService.popularBancoComFilmes(paginas);
        return "Filmes importados com sucesso!";
    }

    @PostMapping("/importar/series")
    public String importarSeries(@RequestParam(defaultValue = "2") int paginas) {
        tmdbSincronizacaoService.popularBancoComSeries(paginas);
        return "Séries importadas com sucesso!";
    }
}
package com.example.cinefile.Controller;

import com.example.cinefile.Service.TMDBService;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/tmdb")
public class TMDBController {

    private final TMDBService tmdbService;

    public TMDBController(TMDBService tmdbService) {
        this.tmdbService = tmdbService;
    }

    @PostMapping("/importar/filmes")
    public String importarFilmes(@RequestParam(defaultValue = "2") int paginas) {
        tmdbService.popularBancoComFilmes(paginas);
        return "Filmes importados com sucesso!";
    }

    @PostMapping("/importar/series")
    public String importarSeries(@RequestParam(defaultValue = "2") int paginas) {
        tmdbService.popularBancoComSeries(paginas);
        return "Séries importadas com sucesso!";
    }
}

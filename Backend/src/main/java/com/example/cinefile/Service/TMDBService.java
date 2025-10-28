package com.example.cinefile.Service;

import com.example.cinefile.Infra.TMDBConfig;
import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Autowired;

@Service
public class TMDBService {

    private final TMDBConfig tmdbConfig;

    @Autowired
    public TMDBService(TMDBConfig tmdbConfig) {
        this.tmdbConfig = tmdbConfig;
    }

    public void buscarFilme() {
        String url = "https://api.themoviedb.org/3/movie/550?api_key=" + tmdbConfig.getApiKey();
        // aqui você pode fazer a requisição HTTP
    }
}

package com.example.cinefile.infraestrutura.externa;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

@Configuration
public class TMDBConfig {

    @Value("${tmdb.api.key}")
    private String chaveApi;

    public String getChaveApi() {
        return chaveApi;
    }
}
package com.example.cinefile.Infra;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

@Configuration
public class TMDBConfig {

    @Value("${tmdb.api.key}")
    private String apiKey;

    public String getApiKey() {
        return apiKey;
    }
}

package com.example.cinefile.Service;

import com.example.cinefile.Domain.Obra.Obra;
import com.example.cinefile.Domain.Obra.ObraRepository;
import com.example.cinefile.Domain.Obra.ObraTipo;
import com.example.cinefile.Infra.TMDBConfig;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.List;
import java.util.Map;

@Service
public class TMDBService {

    private final TMDBConfig tmdbConfig;
    private final ObraRepository obraRepository;
    private final RestTemplate restTemplate;

    public TMDBService(TMDBConfig tmdbConfig, ObraRepository obraRepository, RestTemplate restTemplate) {
        this.tmdbConfig = tmdbConfig;
        this.obraRepository = obraRepository;
        this.restTemplate = restTemplate;
    }

    @SuppressWarnings("unchecked")
    public void popularBancoComFilmes(int paginas) {
        for (int page = 1; page <= paginas; page++) {
            String url = UriComponentsBuilder.fromHttpUrl("https://api.themoviedb.org/3/movie/popular")
                    .queryParam("api_key", tmdbConfig.getApiKey())
                    .queryParam("language", "pt-BR")
                    .queryParam("page", page)
                    .toUriString();

            Map<String, Object> resposta = restTemplate.getForObject(url, Map.class);
            if (resposta == null) continue;

            List<Map<String, Object>> results = (List<Map<String, Object>>) resposta.get("results");
            if (results == null) continue;

            for (Map<String, Object> item : results) {
                salvarDetalhes(item.get("id").toString(), ObraTipo.FILME);
            }
        }
    }

    @SuppressWarnings("unchecked")
    public void popularBancoComSeries(int paginas) {
        for (int page = 1; page <= paginas; page++) {
            String url = UriComponentsBuilder.fromHttpUrl("https://api.themoviedb.org/3/tv/popular")
                    .queryParam("api_key", tmdbConfig.getApiKey())
                    .queryParam("language", "pt-BR")
                    .queryParam("page", page)
                    .toUriString();

            Map<String, Object> resposta = restTemplate.getForObject(url, Map.class);
            if (resposta == null) continue;

            List<Map<String, Object>> results = (List<Map<String, Object>>) resposta.get("results");
            if (results == null) continue;

            for (Map<String, Object> item : results) {
                salvarDetalhes(item.get("id").toString(), ObraTipo.SERIE);
            }
        }
    }

    @SuppressWarnings("unchecked")
    private void salvarDetalhes(String id, ObraTipo tipo) {
        String baseUrl = tipo == ObraTipo.FILME
                ? "https://api.themoviedb.org/3/movie/" + id
                : "https://api.themoviedb.org/3/tv/" + id;

        String url = UriComponentsBuilder.fromHttpUrl(baseUrl)
                .queryParam("api_key", tmdbConfig.getApiKey())
                .queryParam("language", "pt-BR")
                .toUriString();

        try {
            Map<String, Object> detalhes = restTemplate.getForObject(url, Map.class);
            if (detalhes == null) return;

            Long tmdbId = Long.valueOf(detalhes.get("id").toString());
            if (obraRepository.findByTmdbId(tmdbId).isPresent()) return;

            Obra obra = new Obra();
            obra.setTmdbId(tmdbId);
            obra.setTipo(tipo);

            if (tipo == ObraTipo.FILME) {
                obra.setTitulo((String) detalhes.get("title"));
            } else {
                obra.setTitulo((String) detalhes.get("name"));
            }

            obra.setDescricao((String) detalhes.get("overview"));

            String data = tipo == ObraTipo.FILME
                    ? (String) detalhes.get("release_date")
                    : (String) detalhes.get("first_air_date");

            if (data != null && !data.isEmpty()) {
                obra.setAnolancamento(Integer.parseInt(data.split("-")[0]));
            }

            // 🔹 CORREÇÃO AQUI → concatena o endereço completo do pôster
            String posterPath = (String) detalhes.get("poster_path");
            if (posterPath != null && !posterPath.isEmpty()) {
                obra.setPoster_url("https://image.tmdb.org/t/p/w500" + posterPath);
            }

            if (tipo == ObraTipo.FILME && detalhes.get("runtime") != null) {
                obra.setDuracao((Integer) detalhes.get("runtime"));
            } else if (tipo == ObraTipo.SERIE && detalhes.get("episode_run_time") instanceof List<?> tempos) {
                if (!tempos.isEmpty() && tempos.get(0) instanceof Number tempo) {
                    obra.setDuracao(tempo.intValue());
                }
            }

            obraRepository.save(obra);
            System.out.println(" Obra salva: " + obra.getTitulo());

        } catch (Exception e) {
            System.out.println(" Erro ao importar " + tipo + " ID " + id + ": " + e.getMessage());
        }
    }
}

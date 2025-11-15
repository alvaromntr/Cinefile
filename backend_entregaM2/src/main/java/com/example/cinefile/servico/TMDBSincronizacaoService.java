package com.example.cinefile.servico;

import com.example.cinefile.infraestrutura.externa.TMDBConfig;
import com.example.cinefile.modelo.entidades.Obra;
import com.example.cinefile.modelo.enums.TipoObra;
import com.example.cinefile.repositorio.ObraRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class TMDBSincronizacaoService {

    private final TMDBConfig tmdbConfig;
    private final ObraRepository obraRepository;
    private final RestTemplate restTemplate;

    @SuppressWarnings("unchecked")
    public void popularBancoComFilmes(int paginas) {
        for (int pagina = 1; pagina <= paginas; pagina++) {
            String url = construirUrl("https://api.themoviedb.org/3/movie/popular", pagina);
            processarResultadosPagina(url, TipoObra.FILME);
        }
    }

    @SuppressWarnings("unchecked")
    public void popularBancoComSeries(int paginas) {
        for (int pagina = 1; pagina <= paginas; pagina++) {
            String url = construirUrl("https://api.themoviedb.org/3/tv/popular", pagina);
            processarResultadosPagina(url, TipoObra.SERIE);
        }
    }

    private String construirUrl(String baseUrl, int pagina) {
        return UriComponentsBuilder.fromHttpUrl(baseUrl)
                .queryParam("api_key", tmdbConfig.getChaveApi())
                .queryParam("language", "pt-BR")
                .queryParam("page", pagina)
                .toUriString();
    }

    @SuppressWarnings("unchecked")
    private void processarResultadosPagina(String url, TipoObra tipo) {
        Map<String, Object> resposta = restTemplate.getForObject(url, Map.class);
        if (resposta == null) return;

        List<Map<String, Object>> resultados = (List<Map<String, Object>>) resposta.get("results");
        if (resultados == null) return;

        for (Map<String, Object> item : resultados) {
            salvarDetalhesObra(item.get("id").toString(), tipo);
        }
    }

    @SuppressWarnings("unchecked")
    private void salvarDetalhesObra(String id, TipoObra tipo) {
        String urlBase = tipo == TipoObra.FILME
                ? "https://api.themoviedb.org/3/movie/" + id
                : "https://api.themoviedb.org/3/tv/" + id;

        String url = UriComponentsBuilder.fromHttpUrl(urlBase)
                .queryParam("api_key", tmdbConfig.getChaveApi())
                .queryParam("language", "pt-BR")
                .toUriString();

        try {
            Map<String, Object> detalhes = restTemplate.getForObject(url, Map.class);
            if (detalhes == null) return;

            Long tmdbId = Long.valueOf(detalhes.get("id").toString());
            if (obraRepository.findByTmdbId(tmdbId).isPresent()) return;

            Obra obra = criarObraAPartirDetalhes(detalhes, tipo, tmdbId);
            obraRepository.save(obra);
            System.out.println("Obra salva: " + obra.getTitulo());

        } catch (Exception e) {
            System.out.println("Erro ao importar " + tipo + " ID " + id + ": " + e.getMessage());
        }
    }

    private Obra criarObraAPartirDetalhes(Map<String, Object> detalhes, TipoObra tipo, Long tmdbId) {
        Obra obra = new Obra();
        obra.setTmdbId(tmdbId);
        obra.setTipo(tipo);

        // Definir título baseado no tipo
        String titulo = tipo == TipoObra.FILME
                ? (String) detalhes.get("title")
                : (String) detalhes.get("name");
        obra.setTitulo(titulo);

        obra.setDescricao((String) detalhes.get("overview"));

        // Extrair ano de lançamento
        String data = tipo == TipoObra.FILME
                ? (String) detalhes.get("release_date")
                : (String) detalhes.get("first_air_date");
        if (data != null && !data.isEmpty()) {
            obra.setAnoLancamento(Integer.parseInt(data.split("-")[0]));
        }

        // Configurar URL do pôster
        String caminhoPoster = (String) detalhes.get("poster_path");
        if (caminhoPoster != null && !caminhoPoster.isEmpty()) {
            obra.setPosterUrl("https://image.tmdb.org/t/p/w500" + caminhoPoster);
        }

        // Configurar duração
        configurarDuracao(obra, detalhes, tipo);

        return obra;
    }

    @SuppressWarnings("unchecked")
    private void configurarDuracao(Obra obra, Map<String, Object> detalhes, TipoObra tipo) {
        if (tipo == TipoObra.FILME && detalhes.get("runtime") != null) {
            obra.setDuracao((Integer) detalhes.get("runtime"));
        } else if (tipo == TipoObra.SERIE && detalhes.get("episode_run_time") instanceof List<?> tempos) {
            if (!tempos.isEmpty() && tempos.get(0) instanceof Number tempo) {
                obra.setDuracao(tempo.intValue());
            }
        }
    }
}
package com.example.cinefile.modelo.dtos;

import com.example.cinefile.modelo.enums.TipoObra;
import java.util.List;

public record RespostaObra(
        Long id,
        Long tmdbId,
        String titulo,
        String descricao,
        TipoObra tipo,
        Integer anoLancamento,
        String posterUrl,
        Integer duracao,
        List<RespostaCategoria> categorias
) {}
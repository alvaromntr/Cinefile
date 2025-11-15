package com.example.cinefile.DTO;

import com.example.cinefile.Domain.Obra.ObraTipo;

import java.util.List;

public record ObraDTO(
        Long obraid,
        Long tmdbId,
        String titulo,
        String descricao,
        ObraTipo tipo,
        Integer anolancamento,
        String poster_url,
        Integer duracao,
        List<CategoriaDTO> categorias
) {}

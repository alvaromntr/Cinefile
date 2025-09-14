package com.example.cinefile.Service;

import com.example.cinefile.Repository.AvaliacaoRepository;
import domain.Avaliacao_domain;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class AvaliacaoService {

    @Autowired
    private AvaliacaoRepository avaliacaoRepository;

    // Cadastrar avaliação
    public Avaliacao_domain salvarAvaliacao(Avaliacao_domain avaliacao) {
        return avaliacaoRepository.save(avaliacao);
    }

    // Listar todas as avaliações
    public List<Avaliacao_domain> listarAvaliacoes() {
        return avaliacaoRepository.findAll();
    }
}

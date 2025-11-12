package com.example.cinefile.Service;

import com.example.cinefile.Domain.Obra.Obra;
import com.example.cinefile.Domain.Watched.Watched;

import com.example.cinefile.Domain.Obra.ObraRepository;
import com.example.cinefile.Domain.Watched.WatchedRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class WatchedService {

    private final WatchedRepository repo;
    private final ObraRepository obraRepo;

    public WatchedService(WatchedRepository repo, ObraRepository obraRepo) {
        this.repo = repo;
        this.obraRepo = obraRepo;
    }

    public List<Obra> listar() {
        return repo.findAll().stream().map(Watched::getObra).toList();
    }

    public void adicionar(Long obraid) {
        if (repo.existsByObra_Obraid(obraid)) return;
        Obra obra = obraRepo.findById(obraid).orElseThrow();
        repo.save(new Watched(obra));
    }

    public void remover(Long obraid) {
        repo.deleteByObra_Obraid(obraid);
    }
}

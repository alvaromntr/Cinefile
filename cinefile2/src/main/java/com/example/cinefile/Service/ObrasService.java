package com.example.cinefile.Service;

import com.example.cinefile.Repository.ObrasRepository;
import domain.Obras_domain;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class ObrasService {

    @Autowired
    private ObrasRepository obrasRepository;

    //listar todas as obras
    public List<Obras_domain> listarObras() {
        return obrasRepository.findAll();
    }
    //buscar obras por título
    public List<Obras_domain> buscarPorTitulo(String titulo) {
        return obrasRepository.findByTituloContainingIgnoreCase(titulo);
    }

}

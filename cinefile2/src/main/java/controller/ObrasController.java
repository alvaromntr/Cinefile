package controller;

import com.example.cinefile.Service.ObrasService;
import domain.Obras_domain;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/obras")
public class ObrasController {

    @Autowired
    private ObrasService obrasService;

    @GetMapping
    public List<Obras_domain> listarObras() {
        return obrasService.listarObras();
    }

    @GetMapping("/buscar")
    public List<Obras_domain> buscarPorTitulo(@RequestParam String titulo) {
        return obrasService.buscarPorTitulo(titulo);
    }
}

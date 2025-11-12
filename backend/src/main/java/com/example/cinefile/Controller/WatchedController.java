package com.example.cinefile.Controller;

import com.example.cinefile.Domain.Obra.Obra;
import com.example.cinefile.Service.WatchedService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@CrossOrigin(
        origins = {"http://127.0.0.1:5500", "http://localhost:5500"},
        allowCredentials = "true"
)
@RestController
@RequestMapping("/api/watched")
public class WatchedController {

    private final WatchedService watchedService;

    public WatchedController(WatchedService watchedService) {
        this.watchedService = watchedService;
    }

    @GetMapping
    public ResponseEntity<List<Obra>> listar() {
        return ResponseEntity.ok(watchedService.listar());
    }

    @PostMapping("/{obraId}")
    public ResponseEntity<String> adicionar(@PathVariable Long obraId) {
        watchedService.adicionar(obraId);
        return ResponseEntity.ok("Obra marcada como assistida!");
    }

    @DeleteMapping("/{obraId}")
    public ResponseEntity<String> remover(@PathVariable Long obraId) {
        watchedService.remover(obraId);
        return ResponseEntity.ok("Removida dos assistidos.");
    }
}

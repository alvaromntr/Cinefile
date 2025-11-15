package com.example.cinefile.controle;

import com.example.cinefile.modelo.dtos.RespostaConfiguracoes;
import com.example.cinefile.modelo.entidades.Usuario;
import com.example.cinefile.servico.ConfiguracoesService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/minhas-configuracoes")
@RequiredArgsConstructor
public class ConfiguracoesController {

    private final ConfiguracoesService configuracoesService;

    @GetMapping
    public ResponseEntity<RespostaConfiguracoes> obterMinhasConfiguracoes(@AuthenticationPrincipal Usuario usuario) {
        return ResponseEntity.ok(configuracoesService.obterConfiguracoesUsuario(usuario));
    }

    @PutMapping
    public ResponseEntity<RespostaConfiguracoes> atualizarMinhasConfiguracoes(
            @AuthenticationPrincipal Usuario usuario,
            @RequestBody RespostaConfiguracoes dados) {
        return ResponseEntity.ok(configuracoesService.atualizarConfiguracoesUsuario(usuario, dados));
    }
}
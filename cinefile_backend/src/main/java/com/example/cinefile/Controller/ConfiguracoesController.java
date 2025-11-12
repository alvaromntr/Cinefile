package com.example.cinefile.Controller;

import com.example.cinefile.Domain.Usuario.Usuario;
import com.example.cinefile.DTO.ConfiguracoesDTO;
import com.example.cinefile.Service.ConfiguracoesService;
import com.fasterxml.jackson.core.JsonProcessingException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/me/configuracoes")
public class ConfiguracoesController {

    @Autowired private ConfiguracoesService configuracoesService;

    @GetMapping
    public ResponseEntity<ConfiguracoesDTO> getMinhasConfiguracoes(@AuthenticationPrincipal Usuario usuario) throws JsonProcessingException {
        return ResponseEntity.ok(configuracoesService.obterConfiguracoes(usuario));
    }

    @PutMapping
    public ResponseEntity<ConfiguracoesDTO> atualizarMinhasConfiguracoes(
            @AuthenticationPrincipal Usuario usuario,
            @RequestBody ConfiguracoesDTO dto
    ) throws JsonProcessingException {
        return ResponseEntity.ok(configuracoesService.atualizarConfiguracoes(usuario, dto));
    }
}
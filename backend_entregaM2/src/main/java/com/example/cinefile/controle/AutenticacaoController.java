package com.example.cinefile.controle;

import com.example.cinefile.modelo.dtos.RequisicaoLogin;
import com.example.cinefile.modelo.dtos.RequisicaoCriarUsuario;
import com.example.cinefile.modelo.dtos.RespostaLogin;
import com.example.cinefile.servico.AutenticacaoService;
import com.example.cinefile.shared.excecoes.CredenciaisInvalidasException;
import com.example.cinefile.shared.excecoes.EmailJaExistenteException;
import com.example.cinefile.shared.excecoes.UsernameJaExistenteException;
import com.example.cinefile.shared.excecoes.UsuarioNaoEncontradoException;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/autenticacao")
@RequiredArgsConstructor
public class AutenticacaoController {

    private final AutenticacaoService autenticacaoService;

    @PostMapping("/login")
    public ResponseEntity<RespostaLogin> realizarLogin(@RequestBody @Valid RequisicaoLogin requisicao) {
        try {
            RespostaLogin resposta = autenticacaoService.autenticarUsuario(requisicao);
            return ResponseEntity.ok(resposta);
        } catch (CredenciaisInvalidasException | UsuarioNaoEncontradoException e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(new RespostaLogin(false, null, null, null));
        }
    }

    @PostMapping("/registrar")
    public ResponseEntity<RespostaLogin> registrarUsuario(@RequestBody @Valid RequisicaoCriarUsuario comando) {
        try {
            RespostaLogin resposta = autenticacaoService.registrarNovoUsuario(comando);
            return ResponseEntity.ok(resposta);
        } catch (UsernameJaExistenteException | EmailJaExistenteException e) {
            return ResponseEntity.status(HttpStatus.CONFLICT)
                    .body(new RespostaLogin(false, null, null, null));
        }
    }
}
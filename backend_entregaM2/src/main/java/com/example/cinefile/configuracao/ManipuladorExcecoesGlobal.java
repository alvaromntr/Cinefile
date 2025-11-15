package com.example.cinefile.configuracao;

import com.example.cinefile.modelo.dtos.RespostaErro;
import com.example.cinefile.shared.excecoes.*;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class ManipuladorExcecoesGlobal {

    @ExceptionHandler(UsuarioNaoEncontradoException.class)
    public ResponseEntity<RespostaErro> manipularUsuarioNaoEncontrado(UsuarioNaoEncontradoException ex) {
        RespostaErro erro = new RespostaErro("USUARIO_NAO_ENCONTRADO", ex.getMessage(), "username");
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(erro);
    }

    @ExceptionHandler(CredenciaisInvalidasException.class)
    public ResponseEntity<RespostaErro> manipularCredenciaisInvalidas(CredenciaisInvalidasException ex) {
        RespostaErro erro = new RespostaErro("CREDENCIAIS_INVALIDAS", ex.getMessage(), "senha");
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(erro);
    }

    @ExceptionHandler(EmailJaExistenteException.class)
    public ResponseEntity<RespostaErro> manipularEmailDuplicado(EmailJaExistenteException ex) {
        RespostaErro erro = new RespostaErro("EMAIL_DUPLICADO", ex.getMessage(), "email");
        return ResponseEntity.status(HttpStatus.CONFLICT).body(erro);
    }

    @ExceptionHandler(UsernameJaExistenteException.class)
    public ResponseEntity<RespostaErro> manipularUsernameDuplicado(UsernameJaExistenteException ex) {
        RespostaErro erro = new RespostaErro("USERNAME_DUPLICADO", ex.getMessage(), "username");
        return ResponseEntity.status(HttpStatus.CONFLICT).body(erro);
    }

    @ExceptionHandler(EntidadeNaoEncontradaException.class)
    public ResponseEntity<RespostaErro> manipularEntidadeNaoEncontrada(EntidadeNaoEncontradaException ex) {
        RespostaErro erro = new RespostaErro("ENTIDADE_NAO_ENCONTRADA", ex.getMessage(), "id");
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(erro);
    }

    @ExceptionHandler(AcessoNegadoException.class)
    public ResponseEntity<RespostaErro> manipularAcessoNegado(AcessoNegadoException ex) {
        RespostaErro erro = new RespostaErro("ACESSO_NEGADO", ex.getMessage(), "permissao");
        return ResponseEntity.status(HttpStatus.FORBIDDEN).body(erro);
    }

    @ExceptionHandler(TipoObraInvalidoException.class)
    public ResponseEntity<RespostaErro> manipularTipoObraInvalido(TipoObraInvalidoException ex) {
        RespostaErro erro = new RespostaErro("TIPO_OBRA_INVALIDO", ex.getMessage(), "tipo");
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(erro);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<RespostaErro> manipularErroGenerico(Exception ex) {
        RespostaErro erro = new RespostaErro("ERRO_INTERNO", "Erro interno do servidor", "sistema");
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(erro);
    }
}
package com.example.cinefile.shared.excecoes;

public class UsuarioNaoEncontradoException extends RuntimeException {
    public UsuarioNaoEncontradoException(String username) {
        super("Usuário não encontrado: " + username);
    }
}
package com.example.cinefile.shared.excecoes;

public class UsernameJaExistenteException extends RuntimeException {
    public UsernameJaExistenteException(String username) {
        super("Já existe um usuário com o username: " + username);
    }
}
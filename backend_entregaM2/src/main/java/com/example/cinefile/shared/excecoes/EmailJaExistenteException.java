package com.example.cinefile.shared.excecoes;

public class EmailJaExistenteException extends RuntimeException {
    public EmailJaExistenteException(String email) {
        super("Já existe um usuário com o email: " + email);
    }
}
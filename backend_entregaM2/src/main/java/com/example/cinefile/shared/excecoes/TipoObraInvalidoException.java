package com.example.cinefile.shared.excecoes;

public class TipoObraInvalidoException extends RuntimeException {
    public TipoObraInvalidoException(String tipo) {
        super("Tipo de obra inválido: " + tipo);
    }
}
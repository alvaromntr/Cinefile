package com.example.cinefile.shared.excecoes;

public class ObraNaoEncontradaException extends RuntimeException {
    public ObraNaoEncontradaException(Long obraId) {
        super("Obra não encontrada com ID: " + obraId);
    }
}
package com.example.cinefile.servico;

import com.example.cinefile.modelo.dtos.RequisicaoComentario;
import com.example.cinefile.modelo.dtos.RespostaComentario;
import com.example.cinefile.modelo.entidades.Comentario;
import com.example.cinefile.modelo.entidades.Obra;
import com.example.cinefile.modelo.entidades.Usuario;
import com.example.cinefile.modelo.enums.TipoUsuario;
import com.example.cinefile.repositorio.ComentarioRepository;
import com.example.cinefile.repositorio.ObraRepository;

import com.example.cinefile.shared.excecoes.EntidadeNaoEncontradaException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ComentarioServiceTest {

    @Mock
    private ComentarioRepository comentarioRepository;

    @Mock
    private ObraRepository obraRepository;

    @InjectMocks
    private ComentarioService comentarioService;

    @Test
    void deveCriarComentarioComDadosValidos() {
        // Given
        Long obraId = 1L;
        Usuario usuario = new Usuario("user", "user@email.com", "hash", TipoUsuario.USUARIO);
        Obra obra = new Obra();
        obra.setId(obraId);

        RequisicaoComentario requisicao = new RequisicaoComentario("Ótimo filme!");

        when(obraRepository.findById(obraId)).thenReturn(Optional.of(obra));
        when(comentarioRepository.save(any(Comentario.class))).thenAnswer(invocation -> {
            Comentario comentario = invocation.getArgument(0);
            comentario.setId(1L);
            return comentario;
        });

        // When
        RespostaComentario resposta = comentarioService.criarComentario(obraId, requisicao, usuario);

        // Then
        assertNotNull(resposta);
        assertEquals("Ótimo filme!", resposta.texto());
        assertEquals("user", resposta.username());
        verify(comentarioRepository).save(any(Comentario.class));
    }

    @Test
    void deveLancarExcecaoQuandoObraNaoEncontrada() {
        // Given
        Long obraId = 999L;
        Usuario usuario = new Usuario("user", "user@email.com", "hash", TipoUsuario.USUARIO);
        RequisicaoComentario requisicao = new RequisicaoComentario("Comentário");

        when(obraRepository.findById(obraId)).thenReturn(Optional.empty());

        // When & Then
        assertThrows(EntidadeNaoEncontradaException.class, () ->
                comentarioService.criarComentario(obraId, requisicao, usuario)
        );
    }
}
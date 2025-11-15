package com.example.cinefile.servico;

import com.example.cinefile.modelo.dtos.RequisicaoLogin;
import com.example.cinefile.modelo.dtos.RespostaLogin;
import com.example.cinefile.modelo.entidades.Usuario;
import com.example.cinefile.modelo.enums.TipoUsuario;
import com.example.cinefile.repositorio.UsuarioRepository;
import com.example.cinefile.shared.excecoes.CredenciaisInvalidasException;
import com.example.cinefile.shared.excecoes.UsuarioNaoEncontradoException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AutenticacaoServiceTest {

    @Mock
    private UsuarioRepository usuarioRepository;

    // ✅ CORREÇÃO: Use instância real SEM anotações Mockito
    private BCryptPasswordEncoder codificadorSenha = new BCryptPasswordEncoder();

    private AutenticacaoService autenticacaoService;

    private Usuario usuarioValido;
    private RequisicaoLogin requisicaoLoginValida;

    @BeforeEach
    void configurar() {
        // ✅ Injeta manualmente as dependências
        autenticacaoService = new AutenticacaoService(usuarioRepository, codificadorSenha);

        // Cria senha real criptografada
        String senhaCriptografada = codificadorSenha.encode("senha123");

        usuarioValido = new Usuario(
                "usuario123",
                "usuario@email.com",
                senhaCriptografada, // Senha real criptografada
                TipoUsuario.USUARIO
        );
        usuarioValido.setId(UUID.randomUUID());

        requisicaoLoginValida = new RequisicaoLogin("usuario123", "senha123");
    }

    @Test
    void deveAutenticarUsuarioComCredenciaisValidas() {
        when(usuarioRepository.findByUsername("usuario123"))
                .thenReturn(Optional.of(usuarioValido));

        RespostaLogin resposta = autenticacaoService.autenticarUsuario(requisicaoLoginValida);

        assertTrue(resposta.sucesso());
        assertEquals("usuario123", resposta.username());
        assertNotNull(resposta.tokenAutenticacao());
        verify(usuarioRepository).findByUsername("usuario123");
    }

    @Test
    void deveLancarExcecaoQuandoUsuarioNaoEncontrado() {
        when(usuarioRepository.findByUsername("usuarioInexistente"))
                .thenReturn(Optional.empty());

        assertThrows(UsuarioNaoEncontradoException.class, () ->
                autenticacaoService.autenticarUsuario(
                        new RequisicaoLogin("usuarioInexistente", "senha123")
                )
        );
    }

    @Test
    void deveLancarExcecaoQuandoSenhaInvalida() {
        when(usuarioRepository.findByUsername("usuario123"))
                .thenReturn(Optional.of(usuarioValido));

        // Testa com senha errada - o BCrypt real vai retornar false
        assertThrows(CredenciaisInvalidasException.class, () ->
                autenticacaoService.autenticarUsuario(
                        new RequisicaoLogin("usuario123", "senhaErrada")
                )
        );
    }
}
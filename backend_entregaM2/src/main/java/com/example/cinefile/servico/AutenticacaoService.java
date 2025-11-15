package com.example.cinefile.servico;

import com.example.cinefile.modelo.dtos.RequisicaoLogin;
import com.example.cinefile.modelo.dtos.RequisicaoCriarUsuario;
import com.example.cinefile.modelo.dtos.RespostaLogin;
import com.example.cinefile.modelo.entidades.Usuario;
import com.example.cinefile.modelo.enums.TipoUsuario;
import com.example.cinefile.repositorio.UsuarioRepository;
import com.example.cinefile.shared.excecoes.CredenciaisInvalidasException;
import com.example.cinefile.shared.excecoes.EmailJaExistenteException;
import com.example.cinefile.shared.excecoes.UsernameJaExistenteException;
import com.example.cinefile.shared.excecoes.UsuarioNaoEncontradoException;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Base64;

@Service
@RequiredArgsConstructor
public class AutenticacaoService {

    private final UsuarioRepository usuarioRepository;
    private final BCryptPasswordEncoder codificadorSenha;

    @Transactional(readOnly = true)
    public RespostaLogin autenticarUsuario(RequisicaoLogin requisicao) {
        String principal = resolverUsernameDeEntrada(requisicao.usernameOuEmail());
        Usuario usuario = buscarUsuarioPorUsername(principal);

        validarSenha(requisicao.senha(), usuario.getSenhaHash());

        String tokenAutenticacao = gerarTokenAutenticacaoBasica(usuario.getUsername(), requisicao.senha());

        return new RespostaLogin(true, usuario.getUsername(), usuario.getEmail(), tokenAutenticacao);
    }

    @Transactional
    public RespostaLogin registrarNovoUsuario(RequisicaoCriarUsuario comando) {
        validarDadosNovoUsuario(comando);

        Usuario novoUsuario = criarUsuarioAPartirComando(comando);
        usuarioRepository.save(novoUsuario);

        String tokenAutenticacao = gerarTokenAutenticacaoBasica(comando.username(), comando.senha());

        return new RespostaLogin(true, comando.username(), comando.email(), tokenAutenticacao);
    }

    private String resolverUsernameDeEntrada(String entrada) {
        return usuarioRepository.findByEmail(entrada)
                .map(Usuario::getUsername)
                .orElse(entrada);
    }

    private Usuario buscarUsuarioPorUsername(String username) {
        return usuarioRepository.findByUsername(username)
                .orElseThrow(() -> new UsuarioNaoEncontradoException(username));
    }

    private void validarSenha(String senhaDigitada, String senhaCriptografada) {
        if (!codificadorSenha.matches(senhaDigitada, senhaCriptografada)) {
            throw new CredenciaisInvalidasException();
        }
    }

    private void validarDadosNovoUsuario(RequisicaoCriarUsuario comando) {
        if (usuarioRepository.existsByUsername(comando.username())) {
            throw new UsernameJaExistenteException(comando.username());
        }
        if (usuarioRepository.existsByEmail(comando.email())) {
            throw new EmailJaExistenteException(comando.email());
        }
    }

    private Usuario criarUsuarioAPartirComando(RequisicaoCriarUsuario comando) {
        String senhaCriptografada = codificadorSenha.encode(comando.senha());
        return new Usuario(comando.username(), comando.email(), senhaCriptografada, TipoUsuario.USUARIO);
    }

    private String gerarTokenAutenticacaoBasica(String username, String senha) {
        String credenciais = username + ":" + senha;
        return "Basic " + Base64.getEncoder().encodeToString(credenciais.getBytes());
    }
}
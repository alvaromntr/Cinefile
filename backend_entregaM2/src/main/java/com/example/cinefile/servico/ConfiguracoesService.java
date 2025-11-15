package com.example.cinefile.servico;

import com.example.cinefile.modelo.dtos.RespostaConfiguracoes;
import com.example.cinefile.modelo.entidades.Configuracoes;
import com.example.cinefile.modelo.entidades.Usuario;
import com.example.cinefile.repositorio.ConfiguracoesRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ConfiguracoesService {

    private final ConfiguracoesRepository configuracoesRepository;

    public RespostaConfiguracoes obterConfiguracoesUsuario(Usuario usuario) {
        Configuracoes config = buscarOuCriarConfiguracoes(usuario);

        return new RespostaConfiguracoes(
                config.getReceberNotificacoesEmail(),
                config.getTemaInterface(),
                config.getIdiomaPreferido()
        );
    }

    public RespostaConfiguracoes atualizarConfiguracoesUsuario(Usuario usuario, RespostaConfiguracoes dados) {
        Configuracoes config = buscarOuCriarConfiguracoes(usuario);

        if (dados.receberNotificacoesEmail() != null) {
            config.setReceberNotificacoesEmail(dados.receberNotificacoesEmail());
        }
        if (dados.temaInterface() != null) {
            config.setTemaInterface(dados.temaInterface());
        }
        if (dados.idiomaPreferido() != null) {
            config.setIdiomaPreferido(dados.idiomaPreferido());
        }

        configuracoesRepository.save(config);
        return dados;
    }

    private Configuracoes buscarOuCriarConfiguracoes(Usuario usuario) {
        return configuracoesRepository.findByUsuarioId(usuario.getId())
                .orElseGet(() -> {
                    Configuracoes novasConfiguracoes = new Configuracoes(usuario);
                    return configuracoesRepository.save(novasConfiguracoes);
                });
    }
}
package com.example.cinefile.Service;


import com.example.cinefile.Domain.Configuracoes.Configuracoes;
import com.example.cinefile.Domain.Configuracoes.ConfiguracoesRepository;
import com.example.cinefile.Domain.Usuario.Usuario;
import com.example.cinefile.DTO.ConfiguracoesDTO;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ConfiguracoesService {

    @Autowired private ConfiguracoesRepository configuracoesRepository;
    @Autowired private ObjectMapper objectMapper;

    public ConfiguracoesDTO obterConfiguracoes(Usuario usuario) throws JsonProcessingException {
        Configuracoes config = findOrCreateConfiguracoes(usuario);
        return objectMapper.readValue(config.getPreferencias(), ConfiguracoesDTO.class);
    }

    public ConfiguracoesDTO atualizarConfiguracoes(Usuario usuario, ConfiguracoesDTO dto) throws JsonProcessingException {
        Configuracoes config = findOrCreateConfiguracoes(usuario);
        String novasPreferencias = objectMapper.writeValueAsString(dto);
        config.setPreferencias(novasPreferencias);
        configuracoesRepository.save(config);
        return dto;
    }

    private Configuracoes findOrCreateConfiguracoes(Usuario usuario) {
        return configuracoesRepository.findByUsuario_Id(usuario.getId())
                .orElseGet(() -> {

                    ConfiguracoesDTO defaultConfigDTO = new ConfiguracoesDTO();
                    defaultConfigDTO.setReceberNotificacoesEmail(true);
                    defaultConfigDTO.setTemaInterface("light");
                    defaultConfigDTO.setIdiomaPreferido("pt-BR");
                    try {
                        String defaultConfigJson = objectMapper.writeValueAsString(defaultConfigDTO);
                        return new Configuracoes(usuario, defaultConfigJson);
                    } catch (JsonProcessingException e) {
                        throw new RuntimeException("Erro ao criar configurações padrão.", e);
                    }
                });
    }
}